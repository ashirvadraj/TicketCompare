export interface Movie {
  id: string;
  title: string;
  posterUrl: string;
  bannerUrl: string;
  durationMinutes: number;
  genre: string[];
  languages: string[];
  formats: string[]; // 2D, 3D, IMAX, 4DX, Dolby, ICE
  rating: number; // e.g. 8.8
  voteCount: number;
  certification: string; // U, UA, A
  synopsis: string;
  releaseDate: string;
  cast: string[];
  director: string;
}

export interface Cinema {
  id: string;
  name: string;
  chain: string; // PVR INOX, Cinepolis, Wave, Carnival, etc.
  address: string;
  city: string;
  distanceKm: number;
  supportedPlatforms: string[]; // ['bms', 'district', 'pvr', 'cinepolis']
  facilities: string[]; // Recliner, Dolby Atmos, Parking, F&B
}

export type SeatCategory = 'Classic' | 'Prime' | 'Recliner' | 'Couple' | 'Wheelchair';

export interface Seat {
  id: string;
  row: string;
  number: number;
  category: SeatCategory;
  price: number;
  isAvailable: boolean;
  isSelected?: boolean;
}

export interface SeatLayout {
  supported: boolean;
  rows: string[];
  seats: Seat[];
  message?: string;
}

export interface ProviderShowPrice {
  platformId: string;
  platformName: string;
  logoUrl: string;
  ticketPrice: number;
  convenienceFee: number;
  internetHandlingFee: number;
  gstOnFees: number;
  otherCharges: number;
  subtotalBeforeDiscounts: number;
  bestApplicableDiscount: number;
  bestOfferId?: string;
  bestOfferTitle?: string;
  finalPayable: number;
  potentialCashback: number;
  effectiveCost: number;
  isAvailable: boolean;
  seatInventorySupported: boolean;
  deepLink: string;
  officialWebCheckout: string;
  isSponsored?: boolean;
}

export interface Show {
  id: string;
  movieId: string;
  cinemaId: string;
  date: string; // YYYY-MM-DD
  time: string; // e.g. "10:30 AM"
  startTimestamp?: number;
  format: string; // "IMAX 3D", "2D", etc.
  language: string; // "Hindi", "English"
  screenName: string;
  status: 'AVAILABLE' | 'FAST_FILLING' | 'ALMOST_FULL' | 'SOLD_OUT';
  isBookable: boolean;
  availableSeats: number;
  totalSeats?: number;
  verifiedAtTimestamp?: number;
  sourceProvider?: string; // e.g. "PVR INOX", "BookMyShow", "District"
  fetchedAt?: string; // Formatted in Asia/Kolkata
  providerShowId?: string;
  pricing: ProviderShowPrice[];
  cheapestPlatformId: string;
  cheapestFinalPrice: number;
  cheapestBasePrice: number;
}

export interface PriceBreakdown {
  platformId: string;
  platformName: string;
  ticketCount: number;
  seatCategory: SeatCategory;
  basePrice: number;
  convenienceFee: number;
  internetHandlingFee: number;
  gstOnFees: number; // 18% on convenience fees
  mandatoryCharges: number;
  grossAmount: number;
  
  // Deductions
  appliedCouponCode?: string;
  couponDiscount: number;
  bankDiscount: number;
  upiDiscount: number;
  walletDiscount: number;
  membershipDiscount: number;
  totalDiscount: number;
  
  // Final numbers
  finalPayableAmount: number; // The exact amount user pays right now
  potentialCashback: number; // Separate: credited post-payment
  effectiveCost: number; // finalPayableAmount - potentialCashback
  
  appliedOfferIds: string[];
  appliedOfferDescriptions: string[];
  isVerified: boolean;
  verifiedTimestamp: number;
  verificationAgeSeconds: number;
  priceChangeNotice?: string;
}

export interface BookingRequest {
  showId: string;
  movieId: string;
  cinemaId: string;
  platformId: string;
  ticketCount: number;
  selectedSeats?: string[];
  seatCategory: SeatCategory;
  couponCode?: string;
  paymentMethod?: {
    type: 'CREDIT_CARD' | 'DEBIT_CARD' | 'UPI' | 'WALLET' | 'MEMBERSHIP';
    bank?: string;
    cardNetwork?: string;
    upiApp?: string;
    walletProvider?: string;
    membershipTier?: string;
  };
}

export interface BookingResult {
  bookingId: string;
  status: 'INITIATED' | 'DEEP_LINK_DISPATCHED' | 'NOT_SUPPORTED';
  platformId: string;
  deepLink: string;
  checkoutUrl: string;
  finalPayable: number;
  instructions: string;
}

export interface ProviderDiagnosticItem {
  id: string;
  name: string;
  isConnected: boolean;
  httpStatus: number;
  responseTimeMs: number;
  cinemasCount: number;
  showsCount: number;
  validShowsCount: number;
  errorMessage?: string;
  lastSuccessfulFetch?: string;
  requiresCredentials?: boolean;
}

export interface DiagnosticsSummary {
  city: string;
  date: string;
  timezone: string;
  providers: ProviderDiagnosticItem[];
  totalProviders: number;
  successfulProviders: number;
  totalShows: number;
  bookableShows: number;
  moviesWithBookableShows: number;
  isLiveDataConnected: boolean;
  message?: string;
}

