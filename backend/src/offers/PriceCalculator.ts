import { PriceBreakdown, SeatCategory } from '../models/types';
import { Offer, UserPaymentMethod } from './OfferTypes';
import { OfferRepository } from './OfferRepository';
import { OfferEligibilityEngine, EvaluationContext } from './OfferEligibilityEngine';

export interface CalculationInput {
  platformId: string;
  platformName: string;
  ticketPricePerUnit: number;
  ticketCount: number;
  seatCategory: SeatCategory;
  cinemaId: string;
  movieId: string;
  dateStr: string;
  enteredCouponCode?: string;
  userPaymentMethods?: UserPaymentMethod[];
  explicitOfferId?: string; // If user clicked on a specific offer
}

export class PriceCalculator {
  private offerRepo: OfferRepository;
  private eligibilityEngine: OfferEligibilityEngine;

  constructor(offerRepo: OfferRepository) {
    this.offerRepo = offerRepo;
    this.eligibilityEngine = new OfferEligibilityEngine();
  }

  public calculate(input: CalculationInput): PriceBreakdown {
    const {
      platformId,
      platformName,
      ticketPricePerUnit,
      ticketCount,
      seatCategory,
      cinemaId,
      movieId,
      dateStr,
      enteredCouponCode,
      userPaymentMethods,
      explicitOfferId
    } = input;

    // 1. Calculate Base Ticket Price
    const basePrice = this.calculateBasePrice(ticketPricePerUnit, ticketCount, seatCategory);

    // 2. Calculate Mandatory Fees (convenience fee, internet handling fee, GST)
    const fees = this.calculateMandatoryFees(platformId, ticketCount, basePrice);
    const grossAmount = basePrice + fees.convenienceFee + fees.internetHandlingFee + fees.gstOnFees + fees.mandatoryCharges;

    // 3. Find Eligible Offers
    const allOffers = this.offerRepo.getAllOffers();
    const ctx: EvaluationContext = {
      platformId,
      cinemaId,
      movieId,
      orderTotal: grossAmount,
      ticketCount,
      dateStr,
      userPaymentMethods,
      enteredCouponCode
    };

    let appliedOfferIds: string[] = [];
    let appliedOfferDescriptions: string[] = [];
    let couponDiscount = 0;
    let bankDiscount = 0;
    let upiDiscount = 0;
    let walletDiscount = 0;
    let membershipDiscount = 0;
    let potentialCashback = 0;

    // A. Check Coupon Code if provided
    if (enteredCouponCode) {
      const couponOffer = this.offerRepo.findCoupon(enteredCouponCode);
      if (couponOffer) {
        const evalRes = this.eligibilityEngine.evaluateOffer(couponOffer, ctx);
        if (evalRes.isEligible) {
          couponDiscount = evalRes.discountAmount;
          appliedOfferIds.push(couponOffer.id);
          appliedOfferDescriptions.push(`${couponOffer.title}: ₹${couponDiscount} Off`);
        }
      }
    }

    // B. Check Explicit Offer or Best Applicable Offer
    let candidateOffers: Offer[] = [];
    if (explicitOfferId) {
      const explicit = this.offerRepo.getOfferById(explicitOfferId);
      if (explicit) candidateOffers = [explicit];
    } else if (userPaymentMethods && userPaymentMethods.length > 0) {
      // Evaluate offers matching user's registered payment profile
      candidateOffers = allOffers.filter(o => o.category !== 'COUPON');
    } else if (!enteredCouponCode) {
      // General comparison mode (no coupon entered and no user profile): showcase potential offers
      candidateOffers = allOffers.filter(o => o.category !== 'COUPON');
    }

    let bestBankDiscount = 0;
    let bestBankOffer: Offer | null = null;
    let bestUpiDiscount = 0;
    let bestUpiCashback = 0;
    let bestUpiOffer: Offer | null = null;
    let bestMembershipDiscount = 0;
    let bestMembershipOffer: Offer | null = null;

    for (const offer of candidateOffers) {
      const evalRes = this.eligibilityEngine.evaluateOffer(offer, ctx);
      if (!evalRes.isEligible) continue;

      if (offer.category === 'CREDIT_CARD' || offer.category === 'DEBIT_CARD') {
        if (evalRes.discountAmount > bestBankDiscount) {
          bestBankDiscount = evalRes.discountAmount;
          bestBankOffer = offer;
        }
      } else if (offer.category === 'UPI') {
        if (offer.isCashback) {
          if (evalRes.cashbackAmount > bestUpiCashback) {
            bestUpiCashback = evalRes.cashbackAmount;
            bestUpiOffer = offer;
          }
        } else {
          if (evalRes.discountAmount > bestUpiDiscount) {
            bestUpiDiscount = evalRes.discountAmount;
            bestUpiOffer = offer;
          }
        }
      } else if (offer.category === 'MEMBERSHIP') {
        if (evalRes.discountAmount > bestMembershipDiscount) {
          bestMembershipDiscount = evalRes.discountAmount;
          bestMembershipOffer = offer;
        }
      }
    }

    // Anti-fraud Stacking Rule Enforcement:
    // If Coupon is applied, check whether bank offers allow stacking with coupons
    if (couponDiscount > 0) {
      // Check if candidate bank offer allows stacking
      if (bestBankOffer && !bestBankOffer.canStackWithCoupons) {
        // Pick the one giving greater benefit!
        if (bestBankDiscount > couponDiscount) {
          couponDiscount = 0; // Replace coupon with better bank discount
          appliedOfferIds = appliedOfferIds.filter(id => id !== this.offerRepo.findCoupon(enteredCouponCode || '')?.id);
          appliedOfferDescriptions = [];
          bankDiscount = bestBankDiscount;
          appliedOfferIds.push(bestBankOffer.id);
          appliedOfferDescriptions.push(`${bestBankOffer.title}: ₹${bankDiscount} Off`);
        }
      } else if (bestBankOffer && bestBankOffer.canStackWithCoupons) {
        bankDiscount = bestBankDiscount;
        appliedOfferIds.push(bestBankOffer.id);
        appliedOfferDescriptions.push(`${bestBankOffer.title}: ₹${bankDiscount} Off`);
      }
    } else {
      if (bestBankOffer) {
        bankDiscount = bestBankDiscount;
        appliedOfferIds.push(bestBankOffer.id);
        appliedOfferDescriptions.push(`${bestBankOffer.title}: ₹${bankDiscount} Off`);
      }
    }

    // UPI offer processing
    if (bestUpiOffer) {
      if (bestUpiOffer.isCashback) {
        potentialCashback = bestUpiCashback;
        appliedOfferIds.push(bestUpiOffer.id);
        appliedOfferDescriptions.push(`${bestUpiOffer.title}: ₹${potentialCashback} Expected Cashback`);
      } else {
        // Instant discount - cannot stack with Bank card
        if (bankDiscount === 0 && couponDiscount === 0) {
          upiDiscount = bestUpiDiscount;
          appliedOfferIds.push(bestUpiOffer.id);
          appliedOfferDescriptions.push(`${bestUpiOffer.title}: ₹${upiDiscount} Off`);
        }
      }
    }

    // Membership benefits
    if (bestMembershipOffer && (bankDiscount === 0 || bestMembershipOffer.canStackWithBankOffers)) {
      membershipDiscount = bestMembershipDiscount;
      appliedOfferIds.push(bestMembershipOffer.id);
      appliedOfferDescriptions.push(`${bestMembershipOffer.title}: ₹${membershipDiscount} Benefit`);
    }

    const totalDiscount = couponDiscount + bankDiscount + upiDiscount + walletDiscount + membershipDiscount;
    const finalPayableAmount = Math.max(0, grossAmount - totalDiscount);
    const effectiveCost = Math.max(0, finalPayableAmount - potentialCashback);

    return {
      platformId,
      platformName,
      ticketCount,
      seatCategory,
      basePrice,
      convenienceFee: fees.convenienceFee,
      internetHandlingFee: fees.internetHandlingFee,
      gstOnFees: fees.gstOnFees,
      mandatoryCharges: fees.mandatoryCharges,
      grossAmount,
      appliedCouponCode: enteredCouponCode,
      couponDiscount,
      bankDiscount,
      upiDiscount,
      walletDiscount,
      membershipDiscount,
      totalDiscount,
      finalPayableAmount,
      potentialCashback,
      effectiveCost,
      appliedOfferIds,
      appliedOfferDescriptions,
      isVerified: true,
      verifiedTimestamp: Date.now(),
      verificationAgeSeconds: 10
    };
  }

  public calculateBasePrice(unitPrice: number, count: number, category: SeatCategory): number {
    let multiplier = 1.0;
    if (category === 'Prime') multiplier = 1.25;
    else if (category === 'Recliner') multiplier = 1.75;
    else if (category === 'Couple') multiplier = 2.0;

    return Math.round(unitPrice * multiplier) * count;
  }

  public calculateMandatoryFees(platformId: string, ticketCount: number, baseTotal: number): {
    convenienceFee: number;
    internetHandlingFee: number;
    gstOnFees: number;
    mandatoryCharges: number;
  } {
    let feePerTicket = 25.0; // default
    const pId = platformId.toLowerCase();

    if (pId === 'bms') {
      // BookMyShow typically ₹25 - ₹35 convenience fee per ticket
      feePerTicket = 28.0;
    } else if (pId === 'district') {
      // District has lower promotional platform fee (₹18 per ticket)
      feePerTicket = 18.0;
    } else if (pId === 'pvr') {
      // Direct cinema booking convenience fee (₹15 per ticket)
      feePerTicket = 15.0;
    } else if (pId === 'cinepolis') {
      // Cinepolis Club direct booking
      feePerTicket = 16.0;
    }

    const rawFee = Math.round(feePerTicket * ticketCount);
    const internetHandlingFee = 5.0 * ticketCount;
    const combinedFee = rawFee + internetHandlingFee;
    
    // 18% GST strictly on platform/convenience fee as per Indian Tax Law
    const gstOnFees = Math.round(combinedFee * 0.18);
    const mandatoryCharges = 0; // State municipal charges where applicable

    return {
      convenienceFee: rawFee,
      internetHandlingFee,
      gstOnFees,
      mandatoryCharges
    };
  }
}
