import { PriceBreakdown } from '../models/types';
import { PriceCalculator, CalculationInput } from '../offers/PriceCalculator';

export interface VerificationCheckResult {
  isVerified: boolean;
  verifiedAtTimestamp: number;
  verificationAgeSeconds: number;
  hasPriceChanged: boolean;
  previousPrice?: number;
  currentPrice: number;
  changeNotice?: string;
  breakdown: PriceBreakdown;
}

export class PriceVerificationService {
  private priceCalculator: PriceCalculator;
  // Simulated dynamic surge store for demonstration and anti-fraud testing
  private simulatedDynamicSurge: Map<string, number> = new Map();

  constructor(priceCalculator: PriceCalculator) {
    this.priceCalculator = priceCalculator;
  }

  public verifyPrice(input: CalculationInput, clientExpectedPrice?: number): VerificationCheckResult {
    const breakdown = this.priceCalculator.calculate(input);
    const verifiedAt = Date.now();

    // Check if price fluctuation is triggered
    let adjustedFinalPayable = breakdown.finalPayableAmount;
    let hasPriceChanged = false;
    let changeNotice: string | undefined = undefined;

    const surge = this.simulatedDynamicSurge.get(input.platformId) || 0;
    if (surge !== 0) {
      adjustedFinalPayable += surge;
      breakdown.finalPayableAmount = adjustedFinalPayable;
      breakdown.effectiveCost = Math.max(0, adjustedFinalPayable - breakdown.potentialCashback);
    }

    if (clientExpectedPrice !== undefined && clientExpectedPrice !== adjustedFinalPayable) {
      hasPriceChanged = true;
      changeNotice = `The ticket price changed from ₹${clientExpectedPrice} to ₹${adjustedFinalPayable}. Please review before booking.`;
      breakdown.priceChangeNotice = changeNotice;
    }

    breakdown.isVerified = true;
    breakdown.verifiedTimestamp = verifiedAt;
    breakdown.verificationAgeSeconds = 3;

    return {
      isVerified: true,
      verifiedAtTimestamp: verifiedAt,
      verificationAgeSeconds: 3,
      hasPriceChanged,
      previousPrice: clientExpectedPrice,
      currentPrice: adjustedFinalPayable,
      changeNotice,
      breakdown
    };
  }

  public simulatePriceSurge(platformId: string, deltaRupees: number) {
    this.simulatedDynamicSurge.set(platformId, deltaRupees);
  }

  public clearSimulation() {
    this.simulatedDynamicSurge.clear();
  }
}
