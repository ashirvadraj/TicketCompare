import { Offer } from './OfferTypes';

export class OfferRepository {
  private offers: Offer[] = [
    // --- CREDIT CARDS ---
    {
      id: 'offer-hdfc-cc-1',
      category: 'CREDIT_CARD',
      title: 'HDFC Bank Credit Card 25% Off',
      description: 'Get 25% instant discount up to ₹150 on movie tickets with HDFC Credit Cards.',
      bank: 'HDFC',
      cardNetwork: 'ANY',
      cardType: 'CREDIT',
      cardTiers: ['Regalia', 'Millennia', 'Infinia', 'Tata Neu', 'MoneyBack+'],
      minTransaction: 400,
      maxDiscount: 150,
      discountPercentage: 25,
      isCashback: false,
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['bms', 'district', 'pvr', 'cinepolis'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: false,
      terms: [
        'Valid on HDFC Bank Retail Credit Cards only.',
        'Maximum discount is ₹150 per transaction.',
        'Minimum transaction amount ₹400.',
        'Offer valid once per card per calendar month.'
      ],
      source: 'HDFC Bank SmartBuy Movie Partner Program',
      lastVerifiedTimestamp: Date.now() - 5 * 60 * 1000, // 5 min ago
      verificationStatus: 'VERIFIED'
    },
    {
      id: 'offer-icici-cc-coral',
      category: 'CREDIT_CARD',
      title: 'ICICI Bank 25% Off (Coral & Sapphiro)',
      description: 'Buy movie tickets and get 25% discount up to ₹100 on ICICI Bank Gemstone Credit Cards.',
      bank: 'ICICI',
      cardNetwork: 'ANY',
      cardType: 'CREDIT',
      cardTiers: ['Coral', 'Rubyx', 'Sapphiro', 'Emeralde'],
      minTransaction: 300,
      maxDiscount: 100,
      discountPercentage: 25,
      isCashback: false,
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['bms', 'pvr', 'cinepolis'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: false,
      terms: [
        'Valid for ICICI Bank Coral, Rubyx, Sapphiro, and Emeralde Credit Cardholders.',
        'Minimum booking of 2 tickets required.',
        'Maximum discount of ₹100 per transaction.',
        'Limited quota per day on first-come, first-served basis.'
      ],
      source: 'ICICI Bank Culminate Entertainment Offers',
      lastVerifiedTimestamp: Date.now() - 12 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },
    {
      id: 'offer-sbi-simplyclick',
      category: 'CREDIT_CARD',
      title: 'SBI Card SimplyCLICK ₹100 Flat Off',
      description: 'Get flat ₹100 off on minimum purchase of ₹500 using SBI SimplyCLICK Credit Card.',
      bank: 'SBI Card',
      cardNetwork: 'VISA',
      cardType: 'CREDIT',
      cardTiers: ['SimplyCLICK', 'AURUM', 'Elite', 'Prime'],
      minTransaction: 500,
      maxDiscount: 100,
      flatDiscount: 100,
      isCashback: false,
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-11-30T23:59:59Z',
      applicablePlatforms: ['bms', 'district', 'pvr'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['FRI', 'SAT', 'SUN'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: false,
      terms: [
        'Applicable only on Weekend shows (Friday to Sunday).',
        'Minimum transaction value ₹500.',
        'Cannot be clubbed with another card offer.'
      ],
      source: 'SBI Card Partner Portal',
      lastVerifiedTimestamp: Date.now() - 25 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },
    {
      id: 'offer-axis-neo',
      category: 'CREDIT_CARD',
      title: 'Axis Bank MyZone Buy 1 Get 1 Free',
      description: 'Buy 1 ticket and get 2nd ticket free up to ₹200 on Axis Bank MyZone Credit Card.',
      bank: 'Axis Bank',
      cardNetwork: 'ANY',
      cardType: 'CREDIT',
      cardTiers: ['MyZone', 'Magnus', 'Burgundy Private'],
      minTransaction: 250,
      maxDiscount: 200,
      buyXGetYFree: { buy: 1, get: 1, maxFreeTicketValue: 200 },
      isCashback: false,
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['district', 'pvr', 'cinepolis'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: false,
      terms: [
        'Eligible on Axis Bank MyZone Credit Card.',
        'User must select a minimum of 2 seats to avail the free ticket.',
        'Second ticket value discount capped at ₹200.'
      ],
      source: 'Axis Bank Entertainment Desk',
      lastVerifiedTimestamp: Date.now() - 8 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },
    {
      id: 'offer-kotak-everyday',
      category: 'CREDIT_CARD',
      title: 'Kotak Mahindra Bank PVR Card 20% Off',
      description: 'Get 20% instant discount up to ₹150 on PVR INOX movie tickets with Kotak Cards.',
      bank: 'Kotak',
      cardNetwork: 'ANY',
      cardType: 'CREDIT',
      cardTiers: ['Kotak PVR INOX', 'League', 'Zen'],
      minTransaction: 350,
      maxDiscount: 150,
      discountPercentage: 20,
      isCashback: false,
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['pvr', 'bms'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: false,
      terms: [
        'Valid on Weekday shows (Monday to Thursday).',
        'Valid for Kotak Credit Cardholders only.'
      ],
      source: 'Kotak Everyday Delights',
      lastVerifiedTimestamp: Date.now() - 40 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },

    // --- DEBIT CARDS ---
    {
      id: 'offer-hdfc-debit-1',
      category: 'DEBIT_CARD',
      title: 'HDFC Bank Debit Card 10% Off',
      description: 'Get 10% off up to ₹100 on movie bookings with HDFC Debit Cards (Min. ₹500).',
      bank: 'HDFC',
      cardNetwork: 'ANY',
      cardType: 'DEBIT',
      minTransaction: 500,
      maxDiscount: 100,
      discountPercentage: 10,
      isCashback: false,
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['bms', 'district', 'pvr', 'cinepolis'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: false,
      terms: [
        'Valid on HDFC Bank Platinum and Millennia Debit Cards.',
        'Minimum transaction of ₹500.',
        'Maximum instant discount ₹100.'
      ],
      source: 'HDFC Debit Card Privileges',
      lastVerifiedTimestamp: Date.now() - 15 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },
    {
      id: 'offer-icici-debit-1',
      category: 'DEBIT_CARD',
      title: 'ICICI Bank Debit Card ₹75 Off',
      description: 'Flat ₹75 instant discount on minimum spend of ₹450 with ICICI Debit Cards.',
      bank: 'ICICI',
      cardNetwork: 'ANY',
      cardType: 'DEBIT',
      minTransaction: 450,
      maxDiscount: 75,
      flatDiscount: 75,
      isCashback: false,
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['bms', 'pvr'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: false,
      terms: ['Valid once per month per ICICI Debit card account.'],
      source: 'ICICI Bank Expressions',
      lastVerifiedTimestamp: Date.now() - 30 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },

    // --- UPI OFFERS ---
    {
      id: 'offer-gpay-cashback',
      category: 'UPI',
      title: 'Google Pay UPI Scratch Card (Up to ₹75 Cashback)',
      description: 'Pay with Google Pay and get an assured scratch card with up to ₹75 cashback.',
      providerApp: 'GOOGLE_PAY',
      minTransaction: 300,
      maxDiscount: 0, // Instant discount is 0!
      isCashback: true, // STRICT SEPARATION!
      cashbackAmount: 75,
      cashbackConditions: 'Credited directly to linked bank account within 24-48 hours after successful transaction.',
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['bms', 'district', 'pvr', 'cinepolis'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: true,
      canStackWithUPI: false,
      terms: [
        'Cashback is NOT an instant discount. Full final payable amount is deducted at checkout.',
        'Scratch card will unlock in Google Pay rewards section upon transaction completion.',
        'Min transaction ₹300.'
      ],
      source: 'Google Pay Rewards Partner API',
      lastVerifiedTimestamp: Date.now() - 10 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },
    {
      id: 'offer-phonepe-instant',
      category: 'UPI',
      title: 'PhonePe Instant ₹50 Off on District',
      description: 'Get flat ₹50 instant discount when paying via PhonePe UPI on District.',
      providerApp: 'PHONEPE',
      minTransaction: 350,
      maxDiscount: 50,
      flatDiscount: 50,
      isCashback: false, // Instant!
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['district'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: false,
      terms: [
        'Instant deduction applied directly on the PhonePe UPI intent screen.',
        'Valid on first 2 bookings of the month.'
      ],
      source: 'PhonePe Switch Integration',
      lastVerifiedTimestamp: Date.now() - 18 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },
    {
      id: 'offer-paytm-upi-cashback',
      category: 'UPI',
      title: 'Paytm UPI ₹40 Cashback',
      description: 'Get flat ₹40 cashback into Paytm wallet on booking movie tickets with Paytm UPI.',
      providerApp: 'PAYTM',
      minTransaction: 250,
      maxDiscount: 0,
      isCashback: true,
      cashbackAmount: 40,
      cashbackConditions: 'Cashback added to Paytm Wallet balance within 2 hours.',
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['bms', 'pvr'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: true,
      canStackWithUPI: false,
      terms: ['Cashback reflects in Paytm Passbook after showtime confirmation.'],
      source: 'Paytm Cashback Points Program',
      lastVerifiedTimestamp: Date.now() - 60 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },

    // --- WALLETS ---
    {
      id: 'offer-amazonpay-wallet',
      category: 'WALLET',
      title: 'Amazon Pay Balance 10% Cashback',
      description: 'Pay using Amazon Pay Balance and get 10% cashback up to ₹60.',
      providerApp: 'AMAZON_PAY',
      minTransaction: 300,
      maxDiscount: 0,
      isCashback: true,
      cashbackAmount: 60,
      cashbackConditions: 'Added as Amazon Pay Gift Card balance within 3 working days.',
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['bms', 'pvr', 'cinepolis'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: true,
      canStackWithUPI: false,
      terms: ['Requires KYC-verified Amazon Pay wallet account.'],
      source: 'Amazon Pay Merchant Agreement',
      lastVerifiedTimestamp: Date.now() - 45 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },

    // --- COUPONS ---
    {
      id: 'offer-coupon-save100',
      category: 'COUPON',
      title: 'Coupon SAVE100',
      description: 'Flat ₹100 instant discount on minimum booking of ₹500 across platforms.',
      couponCode: 'SAVE100',
      couponCodeRequired: true,
      minTransaction: 500,
      maxDiscount: 100,
      flatDiscount: 100,
      isCashback: false,
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-10-31T23:59:59Z',
      applicablePlatforms: ['bms', 'district', 'pvr', 'cinepolis'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: true,
      terms: [
        'Valid coupon code provided by authorized cinema distributors.',
        'Minimum order value ₹500.',
        'Not valid on Recliner or IMAX luxury lounges.'
      ],
      source: 'Authorized Cinema Promo Feed',
      lastVerifiedTimestamp: Date.now() - 15 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },
    {
      id: 'offer-coupon-movie50',
      category: 'COUPON',
      title: 'Coupon MOVIE50',
      description: 'Flat ₹50 off on minimum purchase of ₹250.',
      couponCode: 'MOVIE50',
      couponCodeRequired: true,
      minTransaction: 250,
      maxDiscount: 50,
      flatDiscount: 50,
      isCashback: false,
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['bms', 'district', 'pvr'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU'],
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: true,
      terms: ['Valid on weekday screenings Monday through Thursday.'],
      source: 'Platform Promotional Code',
      lastVerifiedTimestamp: Date.now() - 10 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },
    {
      id: 'offer-coupon-pvrpass',
      category: 'COUPON',
      title: 'Coupon PVRPASS',
      description: 'Get ₹75 off on convenience fees on PVR INOX official bookings.',
      couponCode: 'PVRPASS',
      couponCodeRequired: true,
      minTransaction: 300,
      maxDiscount: 75,
      flatDiscount: 75,
      isCashback: false,
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['pvr'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'],
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: true,
      terms: ['Direct discount on PVR INOX convenience fee.'],
      source: 'PVR INOX Direct Booking Promotion',
      lastVerifiedTimestamp: Date.now() - 5 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    },

    // --- MEMBERSHIPS ---
    {
      id: 'offer-mem-pvr-passport',
      category: 'MEMBERSHIP',
      title: 'PVR INOX Passport Privilege',
      description: 'Zero convenience fee and flat ₹75 discount per ticket for PVR Passport subscribers.',
      membershipProgram: 'PVR_PASSPORT',
      minTransaction: 200,
      maxDiscount: 75,
      flatDiscount: 75,
      isCashback: false,
      startDate: '2026-01-01T00:00:00Z',
      expiryDate: '2026-12-31T23:59:59Z',
      applicablePlatforms: ['pvr'],
      applicableCinemas: ['ALL'],
      applicableMovies: ['ALL'],
      applicableDays: ['MON', 'TUE', 'WED', 'THU'],
      couponCodeRequired: false,
      canStackWithBankOffers: false,
      canStackWithCoupons: false,
      canStackWithUPI: true,
      terms: [
        'Exclusive to active PVR Passport monthly/quarterly pass holders.',
        'Applicable Monday to Thursday only.',
        'Waives ₹20 platform fee + grants ₹75 ticket discount.'
      ],
      source: 'PVR Passport Official Membership API',
      lastVerifiedTimestamp: Date.now() - 2 * 60 * 1000,
      verificationStatus: 'VERIFIED'
    }
  ];

  public getAllOffers(): Offer[] {
    return this.offers.filter(o => o.verificationStatus !== 'EXPIRED');
  }

  public getOffersByCategory(category: string): Offer[] {
    return this.offers.filter(o => o.category === category && o.verificationStatus !== 'EXPIRED');
  }

  public getOfferById(id: string): Offer | undefined {
    return this.offers.find(o => o.id === id);
  }

  public findCoupon(code: string): Offer | undefined {
    const clean = code.trim().toUpperCase();
    return this.offers.find(o => o.category === 'COUPON' && o.couponCode === clean && o.verificationStatus !== 'EXPIRED');
  }

  public addOffer(newOffer: Offer): Offer {
    this.offers.push(newOffer);
    return newOffer;
  }

  public updateVerification(id: string, status: 'VERIFIED' | 'COMMUNITY_REPORTED' | 'EXPIRED'): boolean {
    const offer = this.offers.find(o => o.id === id);
    if (!offer) return false;
    offer.verificationStatus = status;
    offer.lastVerifiedTimestamp = Date.now();
    return true;
  }
}
