export type OfferCategory = 
  | 'CREDIT_CARD'
  | 'DEBIT_CARD'
  | 'UPI'
  | 'WALLET'
  | 'COUPON'
  | 'MEMBERSHIP';

export type CardNetwork = 'VISA' | 'MASTERCARD' | 'RUPAY' | 'AMEX' | 'DINERS' | 'ANY';

export interface Offer {
  id: string;
  category: OfferCategory;
  title: string;
  description: string;
  bank?: string; // HDFC, ICICI, SBI, Axis, Kotak, IDFC FIRST, RBL, IndusInd, YES BANK, Amex
  cardNetwork?: CardNetwork;
  cardType?: 'CREDIT' | 'DEBIT' | 'ANY';
  cardTiers?: string[]; // e.g. ["Millennia", "Regalia", "Infinia", "Coral", "Sapphiro"]
  
  // UPI / Wallet Specific
  providerApp?: 'GOOGLE_PAY' | 'PHONEPE' | 'PAYTM' | 'AMAZON_PAY' | 'BHIM' | 'MOBIKWIK' | 'ANY';
  
  // Membership Specific
  membershipProgram?: 'PVR_PASSPORT' | 'INOX_REWARDS' | 'BMS_SUPERSTAR' | 'CLUB_CINEPOLIS';
  
  // Financial Rules
  minTransaction: number;
  maxDiscount: number;
  discountPercentage?: number; // e.g. 20 for 20%
  flatDiscount?: number; // e.g. 100 for ₹100 flat
  buyXGetYFree?: { buy: number; get: number; maxFreeTicketValue: number }; // BOGO offers
  
  // Strictly Distinguish Instant Discount from Post-Payment Cashback
  isCashback: boolean; // TRUE = Post-payment credit, FALSE = Immediate price reduction at gateway
  cashbackAmount?: number;
  cashbackConditions?: string;

  // Validity and Filters
  startDate: string; // ISO
  expiryDate: string; // ISO
  applicablePlatforms: string[]; // ['bms', 'district', 'pvr', 'cinepolis', 'ALL']
  applicableCinemas: string[]; // ['ALL'] or specific cinema IDs
  applicableMovies: string[]; // ['ALL'] or specific movie IDs
  applicableDays: string[]; // ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN']
  applicableShowtimes?: { beforeHour?: number; afterHour?: number };
  
  // Coupon
  couponCodeRequired: boolean;
  couponCode?: string;
  
  // Stacking & Anti-fraud Rules
  canStackWithBankOffers: boolean;
  canStackWithCoupons: boolean;
  canStackWithUPI: boolean;
  exclusiveOfferIds?: string[];
  
  // Meta
  terms: string[];
  source: string; // e.g. "Official Bank Partner Agreement", "Public Partner Feed"
  lastVerifiedTimestamp: number;
  verificationStatus: 'VERIFIED' | 'COMMUNITY_REPORTED' | 'EXPIRED';
}

export interface UserPaymentMethod {
  id: string;
  category: OfferCategory;
  bank?: string;
  cardType?: 'CREDIT' | 'DEBIT';
  cardNetwork?: CardNetwork;
  cardTier?: string;
  providerApp?: 'GOOGLE_PAY' | 'PHONEPE' | 'PAYTM' | 'AMAZON_PAY' | 'BHIM' | 'MOBIKWIK';
  membershipProgram?: string;
}

export interface OfferEvaluationResult {
  offer: Offer;
  isEligible: boolean;
  ineligibilityReason?: string;
  discountAmount: number; // Applied immediately
  cashbackAmount: number; // Potential post-payment
  label: 'Eligible' | 'Potential offer — eligibility must be confirmed at checkout' | 'Ineligible';
}
