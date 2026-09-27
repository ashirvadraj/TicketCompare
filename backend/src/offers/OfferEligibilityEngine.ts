import { Offer, UserPaymentMethod, OfferEvaluationResult } from './OfferTypes';

export interface EvaluationContext {
  platformId: string;
  cinemaId: string;
  movieId: string;
  orderTotal: number; // ticket price + fees before discount
  ticketCount: number;
  dateStr: string; // YYYY-MM-DD
  userPaymentMethods?: UserPaymentMethod[];
  enteredCouponCode?: string;
}

export class OfferEligibilityEngine {
  /**
   * Evaluates if a given offer is applicable in this context.
   */
  public evaluateOffer(offer: Offer, ctx: EvaluationContext): OfferEvaluationResult {
    const now = new Date();
    const expiry = new Date(offer.expiryDate);
    const start = new Date(offer.startDate);

    // 1. Check Date Validity
    if (now > expiry || offer.verificationStatus === 'EXPIRED') {
      return {
        offer,
        isEligible: false,
        ineligibilityReason: 'Offer has expired.',
        discountAmount: 0,
        cashbackAmount: 0,
        label: 'Ineligible'
      };
    }

    if (now < start) {
      return {
        offer,
        isEligible: false,
        ineligibilityReason: 'Offer has not started yet.',
        discountAmount: 0,
        cashbackAmount: 0,
        label: 'Ineligible'
      };
    }

    // 2. Check Platform
    const isPlatformAllowed = offer.applicablePlatforms.includes('ALL') || 
                              offer.applicablePlatforms.includes(ctx.platformId.toLowerCase());
    if (!isPlatformAllowed) {
      return {
        offer,
        isEligible: false,
        ineligibilityReason: `Offer is not valid on ${ctx.platformId.toUpperCase()}.`,
        discountAmount: 0,
        cashbackAmount: 0,
        label: 'Ineligible'
      };
    }

    // 3. Check Minimum Transaction
    if (ctx.orderTotal < offer.minTransaction) {
      return {
        offer,
        isEligible: false,
        ineligibilityReason: `Minimum order amount of ₹${offer.minTransaction} required. Current: ₹${ctx.orderTotal}.`,
        discountAmount: 0,
        cashbackAmount: 0,
        label: 'Ineligible'
      };
    }

    // 4. Check Day of Week
    const dateObj = new Date(ctx.dateStr || Date.now());
    const dayNames = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'];
    const currentDay = dayNames[dateObj.getDay()];
    if (offer.applicableDays && offer.applicableDays.length > 0) {
      if (!offer.applicableDays.includes(currentDay)) {
        return {
          offer,
          isEligible: false,
          ineligibilityReason: `Offer valid only on ${offer.applicableDays.join(', ')}.`,
          discountAmount: 0,
          cashbackAmount: 0,
          label: 'Ineligible'
        };
      }
    }

    // 5. Coupon code validation
    if (offer.category === 'COUPON') {
      if (offer.couponCodeRequired) {
        if (!ctx.enteredCouponCode || ctx.enteredCouponCode.trim().toUpperCase() !== offer.couponCode?.toUpperCase()) {
          return {
            offer,
            isEligible: false,
            ineligibilityReason: 'Coupon code does not match.',
            discountAmount: 0,
            cashbackAmount: 0,
            label: 'Ineligible'
          };
        }
      }
    }

    // 6. User Payment Method Match
    let label: 'Eligible' | 'Potential offer — eligibility must be confirmed at checkout' = 'Potential offer — eligibility must be confirmed at checkout';
    let isMethodConfirmed = false;

    if (ctx.userPaymentMethods && ctx.userPaymentMethods.length > 0) {
      for (const pm of ctx.userPaymentMethods) {
        if (offer.category === 'CREDIT_CARD' && pm.category === 'CREDIT_CARD') {
          if (offer.bank && pm.bank?.toUpperCase() === offer.bank.toUpperCase()) {
            isMethodConfirmed = true;
            break;
          }
        } else if (offer.category === 'DEBIT_CARD' && pm.category === 'DEBIT_CARD') {
          if (offer.bank && pm.bank?.toUpperCase() === offer.bank.toUpperCase()) {
            isMethodConfirmed = true;
            break;
          }
        } else if (offer.category === 'UPI' && pm.category === 'UPI') {
          if (offer.providerApp && pm.providerApp === offer.providerApp) {
            isMethodConfirmed = true;
            break;
          }
        } else if (offer.category === 'MEMBERSHIP' && pm.category === 'MEMBERSHIP') {
          if (offer.membershipProgram && pm.membershipProgram === offer.membershipProgram) {
            isMethodConfirmed = true;
            break;
          }
        }
      }

      if (!isMethodConfirmed && offer.category !== 'COUPON') {
        return {
          offer,
          isEligible: false,
          ineligibilityReason: 'User does not possess the required payment method.',
          discountAmount: 0,
          cashbackAmount: 0,
          label: 'Ineligible'
        };
      }
    }

    if (isMethodConfirmed || offer.category === 'COUPON') {
      label = 'Eligible';
    }

    // 7. Calculate Discount Amount & Cashback Amount
    let discount = 0;
    let cashback = 0;

    if (offer.isCashback) {
      // POST-PAYMENT CASHBACK ONLY
      cashback = offer.cashbackAmount || 0;
      discount = 0; // Does not reduce checkout payment!
    } else {
      // INSTANT DISCOUNT
      if (offer.flatDiscount) {
        discount = offer.flatDiscount;
      } else if (offer.discountPercentage) {
        discount = Math.round((ctx.orderTotal * offer.discountPercentage) / 100);
      } else if (offer.buyXGetYFree) {
        // e.g. Buy 1 Get 1 free: 1 ticket price up to maxFreeTicketValue
        if (ctx.ticketCount >= (offer.buyXGetYFree.buy + offer.buyXGetYFree.get)) {
          const estimatedPerTicket = ctx.orderTotal / ctx.ticketCount;
          discount = Math.min(estimatedPerTicket, offer.buyXGetYFree.maxFreeTicketValue);
        }
      }

      if (offer.maxDiscount > 0) {
        discount = Math.min(discount, offer.maxDiscount);
      }
    }

    return {
      offer,
      isEligible: true,
      discountAmount: discount,
      cashbackAmount: cashback,
      label
    };
  }
}
