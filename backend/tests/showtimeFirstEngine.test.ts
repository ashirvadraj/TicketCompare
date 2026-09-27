import { MovieDataService, parseShowTimestamp, getTodayDateStr } from '../src/services/MovieDataService';
import { PriceCalculator } from '../src/offers/PriceCalculator';
import { OfferRepository } from '../src/offers/OfferRepository';

describe('Showtime-First Engine & Bookability Tests', () => {
  let service: MovieDataService;
  let priceCalculator: PriceCalculator;
  let offerRepo: OfferRepository;

  beforeEach(() => {
    offerRepo = new OfferRepository();
    priceCalculator = new PriceCalculator(offerRepo);
    service = new MovieDataService(priceCalculator, offerRepo);
  });

  test('Test 1: Movie with no future cinema shows DOES NOT appear in booking results', () => {
    // In our catalog, 'movie-avatar' has no cinema screen slots scheduled
    const movies = service.getMovies('Noida');
    const avatarMovie = movies.find(m => m.id === 'movie-avatar');
    expect(avatarMovie).toBeUndefined(); // NEVER displayed in bookable results!
  });

  test('Test 2: Movie from earlier year (Tumbbad) with legitimate future show DOES appear', () => {
    // Tumbbad was originally released in 2024, but has a verified 2026 IMAX re-release screening
    // Future shows scheduled at 04:30 PM. Let's check with a mock 'now' earlier in the day
    const morningNow = new Date('2026-09-28T09:00:00Z');
    const movies = service.getMovies('Noida', '2026-09-28', undefined, morningNow);
    const tumbbad = movies.find(m => m.id === 'movie-tumbbad-re');
    expect(tumbbad).toBeDefined();
    expect(tumbbad?.title).toContain('Tumbbad');
  });

  test('Test 3: Shows yesterday or in the past are strictly hidden', () => {
    const today = new Date('2026-09-28T12:00:00Z');
    const yesterdayStr = '2026-09-27';
    const pastShows = service.getValidShows('Noida', undefined, undefined, yesterdayStr, today);
    expect(pastShows.length).toBe(0);
  });

  test('Test 4: Shows today that already started (past current time) are removed', () => {
    // Set time to 7:00 PM (19:00) on 2026-09-28
    const eveningNow = new Date(2026, 8, 28, 19, 0, 0); // month is 0-indexed: 8 is September
    const shows = service.getValidShows('Noida', undefined, undefined, '2026-09-28', eveningNow);

    // Verify all returned shows have startTimestamp > eveningNow
    for (const show of shows) {
      expect(show.startTimestamp).toBeGreaterThan(eveningNow.getTime());
    }

    // Specifically verify 10:30 AM and 02:00 PM shows are GONE
    const morningShow = shows.find(s => s.time === '10:30 AM' || s.time === '02:00 PM');
    expect(morningShow).toBeUndefined();
  });

  test('Test 5: Shows tomorrow appear completely', () => {
    const now = new Date(2026, 8, 28, 23, 0, 0); // 11:00 PM today
    const tomorrowStr = '2026-09-29';
    const tomorrowShows = service.getValidShows('Noida', undefined, undefined, tomorrowStr, now);
    expect(tomorrowShows.length).toBeGreaterThan(0);
    // Morning shows tomorrow MUST be present
    expect(tomorrowShows.some(s => s.time === '10:30 AM' || s.time === '10:00 AM')).toBe(true);
  });

  test('Test 6: Provider shows have verified bookability, screen name, and valid pricing', () => {
    const morningNow = new Date(2026, 8, 28, 6, 0, 0); // 6 AM
    const shows = service.getValidShows('Noida', 'movie-war2', 'cinema-pvr-moi', '2026-09-28', morningNow);
    expect(shows.length).toBeGreaterThan(0);

    const firstShow = shows[0];
    expect(firstShow.isBookable).toBe(true);
    expect(firstShow.availableSeats).toBeGreaterThan(0);
    expect(firstShow.screenName).toContain('Screen 1 (IMAX Laser)');
    expect(firstShow.pricing.length).toBeGreaterThanOrEqual(3); // BMS, District, PVR
    expect(firstShow.cheapestFinalPrice).toBeGreaterThan(0);
  });

  test('Test 7: verifyShow returns isBookable=false for non-existent or past shows', () => {
    const morningNow = new Date(2026, 8, 28, 6, 0, 0);
    const validResult = service.verifyShow('show-cinema-pvr-moi-movie-war2-1030AM-2026-09-28', morningNow);
    expect(validResult.isBookable).toBe(true);

    // Non-existent show
    const invalidResult = service.verifyShow('show-invalid-999', morningNow);
    expect(invalidResult.isBookable).toBe(false);
    expect(invalidResult.message).toContain('no longer available');

    // Show that is now in the past
    const midnightNow = new Date(2026, 8, 28, 23, 59, 0);
    const expiredResult = service.verifyShow('show-cinema-pvr-moi-movie-war2-1030AM-2026-09-28', midnightNow);
    expect(expiredResult.isBookable).toBe(false);
  });
});
