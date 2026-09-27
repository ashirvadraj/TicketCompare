import { Offer } from './OfferTypes';
import { RealTimeOfferFetcher } from '../services/RealTimeOfferFetcher';

export class OfferRepository {
  private offers: Offer[] = RealTimeOfferFetcher.getLiveSynchronizedOffers();
  private lastSyncTimestamp: number = Date.now();

  /**
   * Synchronize the complete offer directory in real time from partner feeds
   * across BookMyShow, District by Zomato, PVR INOX, and Cinepolis.
   */
  public syncLiveOffers(): { count: number; syncedAt: number; offers: Offer[] } {
    this.offers = RealTimeOfferFetcher.getLiveSynchronizedOffers();
    this.lastSyncTimestamp = Date.now();
    return {
      count: this.offers.length,
      syncedAt: this.lastSyncTimestamp,
      offers: this.getAllOffers()
    };
  }

  public getLastSyncTimestamp(): number {
    return this.lastSyncTimestamp;
  }

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
