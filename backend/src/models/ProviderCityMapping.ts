export interface ProviderCity {
  providerId: 'bms' | 'district' | 'pvr' | 'cinepolis';
  displayName: string;
  providerCityId: string;
  regionCode?: string;
}

export const CITY_MAPPINGS: Record<string, ProviderCity[]> = {
  noida: [
    { providerId: 'bms', displayName: 'Noida', providerCityId: 'NOIDA', regionCode: 'NCR' },
    { providerId: 'district', displayName: 'Noida', providerCityId: 'noida', regionCode: 'NCR' },
    { providerId: 'pvr', displayName: 'Noida', providerCityId: '101', regionCode: 'NCR' },
    { providerId: 'cinepolis', displayName: 'Noida', providerCityId: 'noida', regionCode: 'NCR' }
  ],
  delhi: [
    { providerId: 'bms', displayName: 'Delhi', providerCityId: 'NCR', regionCode: 'NCR' },
    { providerId: 'district', displayName: 'Delhi', providerCityId: 'delhi', regionCode: 'NCR' },
    { providerId: 'pvr', displayName: 'Delhi', providerCityId: '102', regionCode: 'NCR' },
    { providerId: 'cinepolis', displayName: 'Delhi', providerCityId: 'delhi', regionCode: 'NCR' }
  ],
  mumbai: [
    { providerId: 'bms', displayName: 'Mumbai', providerCityId: 'MUMBAI', regionCode: 'MUM' },
    { providerId: 'district', displayName: 'Mumbai', providerCityId: 'mumbai', regionCode: 'MUM' },
    { providerId: 'pvr', displayName: 'Mumbai', providerCityId: '201', regionCode: 'MUM' },
    { providerId: 'cinepolis', displayName: 'Mumbai', providerCityId: 'mumbai', regionCode: 'MUM' }
  ],
  bengaluru: [
    { providerId: 'bms', displayName: 'Bengaluru', providerCityId: 'BANG', regionCode: 'BLR' },
    { providerId: 'district', displayName: 'Bengaluru', providerCityId: 'bangalore', regionCode: 'BLR' },
    { providerId: 'pvr', displayName: 'Bengaluru', providerCityId: '301', regionCode: 'BLR' },
    { providerId: 'cinepolis', displayName: 'Bengaluru', providerCityId: 'bangalore', regionCode: 'BLR' }
  ]
};

export function resolveProviderCityId(city: string, providerId: string): string {
  const normalized = city.trim().toLowerCase();
  const mappings = CITY_MAPPINGS[normalized] || CITY_MAPPINGS['noida'];
  const match = mappings.find(m => m.providerId === providerId);
  return match ? match.providerCityId : normalized;
}
