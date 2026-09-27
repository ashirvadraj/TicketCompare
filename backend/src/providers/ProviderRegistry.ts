import { MovieProvider } from '../interfaces/MovieProvider';
import { BookMyShowProvider } from './BookMyShowProvider';
import { DistrictProvider } from './DistrictProvider';
import { PvrInoxProvider } from './PvrInoxProvider';
import { CinepolisProvider } from './CinepolisProvider';
import { OfferRepository } from '../offers/OfferRepository';
import { PriceCalculator } from '../offers/PriceCalculator';

export interface ProviderHealth {
  id: string;
  name: string;
  status: 'ONLINE' | 'MAINTENANCE' | 'DEGRADED';
  responseTimeMs: number;
  lastHealthCheck: number;
  successRatePercent: number;
}

export class ProviderRegistry {
  private providers: Map<string, MovieProvider> = new Map();
  private healthMap: Map<string, ProviderHealth> = new Map();

  constructor(offerRepo: OfferRepository, priceCalculator: PriceCalculator) {
    const bms = new BookMyShowProvider(offerRepo, priceCalculator);
    const district = new DistrictProvider(offerRepo, priceCalculator);
    const pvr = new PvrInoxProvider(offerRepo, priceCalculator);
    const cinepolis = new CinepolisProvider(offerRepo, priceCalculator);

    this.register(bms);
    this.register(district);
    this.register(pvr);
    this.register(cinepolis);
  }

  public register(provider: MovieProvider) {
    this.providers.set(provider.id, provider);
    this.healthMap.set(provider.id, {
      id: provider.id,
      name: provider.name,
      status: 'ONLINE',
      responseTimeMs: 85,
      lastHealthCheck: Date.now(),
      successRatePercent: 99.8
    });
  }

  public getProvider(id: string): MovieProvider | undefined {
    return this.providers.get(id);
  }

  public getAllProviders(): MovieProvider[] {
    return Array.from(this.providers.values());
  }

  public getHealthStatus(): ProviderHealth[] {
    return Array.from(this.healthMap.values());
  }

  public setProviderStatus(id: string, status: 'ONLINE' | 'MAINTENANCE' | 'DEGRADED') {
    const health = this.healthMap.get(id);
    if (health) {
      health.status = status;
      health.lastHealthCheck = Date.now();
    }
  }
}
