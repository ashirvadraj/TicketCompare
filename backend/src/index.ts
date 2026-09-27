import express, { Request, Response } from 'express';
import cors from 'cors';
import { OfferRepository } from './offers/OfferRepository';
import { PriceCalculator } from './offers/PriceCalculator';
import { ProviderRegistry } from './providers/ProviderRegistry';
import { MovieDataService } from './services/MovieDataService';
import { PriceVerificationService } from './services/PriceVerificationService';
import { getAdminDashboardHtml } from './admin/adminHtml';
import { BookingRequest } from './models/types';

const app = express();
const PORT = process.env.PORT || 4000;

app.use(cors());
app.use(express.json());

// Initialize Core Singletons
const offerRepository = new OfferRepository();
const priceCalculator = new PriceCalculator(offerRepository);
const providerRegistry = new ProviderRegistry(offerRepository, priceCalculator);
const movieDataService = new MovieDataService(priceCalculator, offerRepository);
const priceVerificationService = new PriceVerificationService(priceCalculator);

// --- ADMIN DASHBOARD ---
app.get('/admin', (req: Request, res: Response) => {
  res.setHeader('Content-Type', 'text/html');
  res.send(getAdminDashboardHtml());
});

// --- API ENDPOINTS ---

// 1. Movies
app.get('/api/movies', (req: Request, res: Response) => {
  const city = (req.query.city as string) || 'Noida';
  const query = req.query.query as string | undefined;
  const movies = movieDataService.getMovies(city, query);
  res.json(movies);
});

app.get('/api/movies/:id', (req: Request, res: Response) => {
  const movie = movieDataService.getMovieDetails(req.params.id);
  if (!movie) {
    return res.status(404).json({ error: 'Movie not found' });
  }
  res.json(movie);
});

// 2. Cinemas
app.get('/api/cinemas', (req: Request, res: Response) => {
  const city = (req.query.city as string) || 'Noida';
  const movieId = req.query.movieId as string | undefined;
  const cinemas = movieDataService.getCinemas(city, movieId);
  res.json(cinemas);
});

// 3. Shows with Multi-Platform Comparison
app.get('/api/shows', (req: Request, res: Response) => {
  const city = (req.query.city as string) || 'Noida';
  const movieId = (req.query.movieId as string) || 'movie-war2';
  const cinemaId = req.query.cinemaId as string | undefined;
  const dateStr = (req.query.date as string) || undefined;

  const shows = movieDataService.getShowsForMovie(city, movieId, cinemaId, dateStr);
  res.json(shows);
});

// 4. Seat Layout (PVR INOX or NOT_SUPPORTED)
app.get('/api/shows/:showId/seat-layout', async (req: Request, res: Response) => {
  const platformId = (req.query.platform as string) || 'pvr';
  const provider = providerRegistry.getProvider(platformId);

  if (!provider) {
    return res.status(404).json({ error: `Provider ${platformId} not found` });
  }

  const layout = await provider.getSeatLayout(req.params.showId);
  if (layout === 'NOT_SUPPORTED') {
    return res.json({
      supported: false,
      message: 'Seat selection will continue on the provider official booking page.'
    });
  }

  res.json(layout);
});

// 5. Final Price Calculation
app.post('/api/pricing/calculate', (req: Request, res: Response) => {
  try {
    const {
      platformId = 'district',
      platformName = 'District',
      ticketPricePerUnit = 250,
      ticketCount = 1,
      seatCategory = 'Classic',
      cinemaId = 'cinema-pvr-logix',
      movieId = 'movie-avatar',
      dateStr = new Date().toISOString().split('T')[0],
      enteredCouponCode,
      userPaymentMethods,
      explicitOfferId
    } = req.body;

    const breakdown = priceCalculator.calculate({
      platformId,
      platformName,
      ticketPricePerUnit,
      ticketCount,
      seatCategory,
      cinemaId,
      movieId,
      dateStr,
      enteredCouponCode,
      userPaymentMethods,
      explicitOfferId
    });

    res.json(breakdown);
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

// 6. Pre-Booking Price Verification (Fresh check & Price Change Alert)
app.post('/api/pricing/verify', (req: Request, res: Response) => {
  try {
    const { input, clientExpectedPrice } = req.body;
    const result = priceVerificationService.verifyPrice(input, clientExpectedPrice);
    res.json(result);
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

// 7. Offers Directory & Live Synchronization
app.get('/api/offers', (req: Request, res: Response) => {
  const category = req.query.category as string | undefined;
  const bank = req.query.bank as string | undefined;
  let offers = offerRepository.getAllOffers();

  if (category && category !== 'ALL') {
    offers = offers.filter(o => o.category === category);
  }

  if (bank && bank !== 'ALL') {
    const cleanBank = bank.toUpperCase().replace(/\b(BANK|CARD)\b/g, '').trim();
    offers = offers.filter(o => o.bank && o.bank.toUpperCase().includes(cleanBank));
  }

  res.json(offers);
});

// Real-Time Bank & Platform Offer Sync
app.post('/api/offers/sync', (req: Request, res: Response) => {
  const syncResult = offerRepository.syncLiveOffers();
  res.json({
    success: true,
    message: `Synchronized ${syncResult.count} live bank & partner offers in real time.`,
    count: syncResult.count,
    syncedAt: syncResult.syncedAt,
    offers: syncResult.offers
  });
});

app.get('/api/offers/live', (req: Request, res: Response) => {
  const offers = offerRepository.getAllOffers();
  const banks = Array.from(new Set(offers.map(o => o.bank).filter(Boolean)));
  res.json({
    totalOffers: offers.length,
    lastSyncTimestamp: offerRepository.getLastSyncTimestamp(),
    supportedBanks: banks,
    supportedProviders: ['BookMyShow', 'District by Zomato', 'PVR INOX', 'Cinepolis'],
    offers
  });
});

// 8. Coupon Code Validation
app.post('/api/offers/validate-coupon', (req: Request, res: Response) => {
  const { code } = req.body;
  if (!code) {
    return res.status(400).json({ valid: false, message: 'Please provide a coupon code' });
  }

  const coupon = offerRepository.findCoupon(code);
  if (!coupon) {
    return res.json({ valid: false, message: 'Invalid or expired coupon code' });
  }

  res.json({
    valid: true,
    coupon,
    message: `Coupon ${coupon.couponCode} is valid! ₹${coupon.flatDiscount || coupon.maxDiscount} benefit applies.`
  });
});

// 9. Provider Status
app.get('/api/providers/status', (req: Request, res: Response) => {
  res.json(providerRegistry.getHealthStatus());
});

// 10. Booking Dispatch
app.post('/api/booking/initiate', async (req: Request, res: Response) => {
  const request: BookingRequest = req.body;
  const provider = providerRegistry.getProvider(request.platformId);
  if (!provider) {
    return res.status(400).json({ error: `Unknown provider: ${request.platformId}` });
  }

  const result = await provider.createBooking(request);
  res.json(result);
});

// --- ADMIN MANAGEMENT ROUTES ---

app.put('/api/admin/providers/:id/status', (req: Request, res: Response) => {
  const { status } = req.body;
  providerRegistry.setProviderStatus(req.params.id, status);
  res.json({ success: true, status });
});

app.put('/api/admin/offers/:id/verify', (req: Request, res: Response) => {
  const { status } = req.body;
  const success = offerRepository.updateVerification(req.params.id, status);
  res.json({ success });
});

app.post('/api/admin/offers', (req: Request, res: Response) => {
  const newOffer = req.body;
  const created = offerRepository.addOffer(newOffer);
  res.status(201).json(created);
});

app.post('/api/admin/simulate-price-change', (req: Request, res: Response) => {
  const { platformId, delta } = req.body;
  priceVerificationService.simulatePriceSurge(platformId, delta);
  res.json({ success: true, platformId, delta });
});

app.post('/api/admin/simulate-price-change/reset', (req: Request, res: Response) => {
  priceVerificationService.clearSimulation();
  res.json({ success: true, message: 'Price simulation reset to baseline' });
});

export { app, offerRepository, priceCalculator, providerRegistry, movieDataService, priceVerificationService };

if (process.env.NODE_ENV !== 'test') {
  app.listen(PORT, () => {
    console.log(`[TicketCompare API] Server running on http://localhost:${PORT}`);
    console.log(`[TicketCompare Admin] Dashboard at http://localhost:${PORT}/admin`);
  });
}
