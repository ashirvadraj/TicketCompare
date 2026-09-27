import { OfferRepository } from '../src/offers/OfferRepository';
import { PriceCalculator } from '../src/offers/PriceCalculator';
import { PriceVerificationService } from '../src/services/PriceVerificationService';

describe('TicketCompare Pricing & Offer Engine Tests', () => {
  let offerRepo: OfferRepository;
  let calculator: PriceCalculator;
  let verificationService: PriceVerificationService;

  beforeEach(() => {
    offerRepo = new OfferRepository();
    calculator = new PriceCalculator(offerRepo);
    verificationService = new PriceVerificationService(calculator);
  });

  test('Base price calculates accurately for different seat tiers', () => {
    const classicTotal = calculator.calculateBasePrice(200, 2, 'Classic');
    expect(classicTotal).toBe(400);

    const reclinerTotal = calculator.calculateBasePrice(200, 2, 'Recliner');
    // 200 * 1.75 = 350 * 2 = 700
    expect(reclinerTotal).toBe(700);
  });

  test('Mandatory fees include convenience fee and 18% GST correctly', () => {
    const fees = calculator.calculateMandatoryFees('bms', 2, 500);
    // BMS: 28 * 2 = 56 fee, 5 * 2 = 10 internet fee, sum = 66
    // GST = 66 * 0.18 = 11.88 -> rounded to 12
    expect(fees.convenienceFee).toBe(56);
    expect(fees.internetHandlingFee).toBe(10);
    expect(fees.gstOnFees).toBe(12);
  });

  test('Instant HDFC Bank discount reduces Final Payable Amount directly', () => {
    const result = calculator.calculate({
      platformId: 'bms',
      platformName: 'BookMyShow',
      ticketPricePerUnit: 250,
      ticketCount: 2,
      seatCategory: 'Classic',
      cinemaId: 'cinema-pvr-moi',
      movieId: 'movie-avatar',
      dateStr: '2026-10-05',
      userPaymentMethods: [
        { id: '1', category: 'CREDIT_CARD', bank: 'HDFC', cardType: 'CREDIT' }
      ]
    });

    // Gross: 500 base + 56 + 10 + 12 GST = 578
    // HDFC discount: 25% of 578 = 144.5 -> 145 (capped at 150)
    expect(result.bankDiscount).toBeGreaterThan(0);
    expect(result.finalPayableAmount).toBe(result.grossAmount - result.totalDiscount);
    expect(result.potentialCashback).toBe(0);
  });

  test('Cashback offers (Google Pay) are strictly separated from Final Payable Amount', () => {
    const result = calculator.calculate({
      platformId: 'district',
      platformName: 'District',
      ticketPricePerUnit: 250,
      ticketCount: 2,
      seatCategory: 'Classic',
      cinemaId: 'cinema-pvr-moi',
      movieId: 'movie-avatar',
      dateStr: '2026-10-05',
      explicitOfferId: 'offer-gpay-cashback'
    });

    // Final payable MUST NOT be reduced by cashback
    expect(result.upiDiscount).toBe(0);
    expect(result.finalPayableAmount).toBe(result.grossAmount);
    // Potential cashback must be tracked separately
    expect(result.potentialCashback).toBe(75);
    // Effective cost = finalPayable - cashback
    expect(result.effectiveCost).toBe(result.finalPayableAmount - 75);
  });

  test('Coupon SAVE100 applies flat ₹100 discount when minimum spend is met', () => {
    const result = calculator.calculate({
      platformId: 'pvr',
      platformName: 'PVR INOX',
      ticketPricePerUnit: 250,
      ticketCount: 2,
      seatCategory: 'Classic',
      cinemaId: 'cinema-pvr-moi',
      movieId: 'movie-avatar',
      dateStr: '2026-10-05',
      enteredCouponCode: 'SAVE100'
    });

    expect(result.couponDiscount).toBe(100);
    expect(result.finalPayableAmount).toBe(result.grossAmount - 100);
  });

  test('PriceVerificationService detects price fluctuations and alerts user', () => {
    const input = {
      platformId: 'district',
      platformName: 'District',
      ticketPricePerUnit: 250,
      ticketCount: 1,
      seatCategory: 'Classic' as const,
      cinemaId: 'cinema-pvr-moi',
      movieId: 'movie-avatar',
      dateStr: '2026-10-05'
    };

    // First check baseline
    const initial = verificationService.verifyPrice(input);
    expect(initial.isVerified).toBe(true);
    expect(initial.hasPriceChanged).toBe(false);

    // Simulate price jump (+₹25)
    verificationService.simulatePriceSurge('district', 25);

    const recheck = verificationService.verifyPrice(input, initial.currentPrice);
    expect(recheck.hasPriceChanged).toBe(true);
    expect(recheck.changeNotice).toContain('The ticket price changed');
    expect(recheck.currentPrice).toBe(initial.currentPrice + 25);
  });
});
