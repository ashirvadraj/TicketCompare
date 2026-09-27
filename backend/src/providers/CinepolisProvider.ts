import { MovieProvider, NotSupported } from '../interfaces/MovieProvider';
import { Movie, Cinema, Show, SeatLayout, PriceBreakdown, BookingRequest, BookingResult, SeatCategory } from '../models/types';
import { Offer } from '../offers/OfferTypes';
import { OfferRepository } from '../offers/OfferRepository';
import { PriceCalculator } from '../offers/PriceCalculator';

export class CinepolisProvider implements MovieProvider {
  public readonly id = 'cinepolis';
  public readonly name = 'Cinepolis';
  public readonly logoUrl = 'https://www.cinepolisindia.com/assets/img/logo.png';
  public readonly isOfficialPartner = true;
  public readonly supportsSeatLayout = false;
  public readonly supportsDirectBooking = false;

  private offerRepo: OfferRepository;
  private priceCalculator: PriceCalculator;

  constructor(offerRepo: OfferRepository, priceCalculator: PriceCalculator) {
    this.offerRepo = offerRepo;
    this.priceCalculator = priceCalculator;
  }

  public async searchMovies(city: string, query?: string): Promise<Movie[]> {
    return [];
  }

  public async getMovieDetails(movieId: string): Promise<Movie | null> {
    return null;
  }

  public async getCinemas(city: string, movieId?: string): Promise<Cinema[]> {
    return [];
  }

  public async getShows(city: string, movieId: string, cinemaId: string, date: string): Promise<Show[]> {
    return [];
  }

  public async getSeatLayout(showId: string): Promise<SeatLayout | NotSupported> {
    return 'NOT_SUPPORTED';
  }

  public async getPricing(showId: string, ticketCount: number, seatType: SeatCategory): Promise<PriceBreakdown> {
    return this.priceCalculator.calculate({
      platformId: this.id,
      platformName: this.name,
      ticketPricePerUnit: 250,
      ticketCount,
      seatCategory: seatType,
      cinemaId: 'cinema-cinepolis-venice',
      movieId: 'movie-avatar',
      dateStr: new Date().toISOString().split('T')[0]
    });
  }

  public async getOffers(): Promise<Offer[]> {
    return this.offerRepo.getAllOffers().filter(o => 
      o.applicablePlatforms.includes('ALL') || o.applicablePlatforms.includes(this.id)
    );
  }

  public async createBooking(request: BookingRequest): Promise<BookingResult | NotSupported> {
    return {
      bookingId: `CINEPOLIS-${Date.now()}`,
      status: 'DEEP_LINK_DISPATCHED',
      platformId: this.id,
      deepLink: this.generateDeepLink(request.showId, request.cinemaId, request.movieId),
      checkoutUrl: this.generateWebUrl(request.showId, request.cinemaId, request.movieId),
      finalPayable: 0,
      instructions: 'Opening Club Cinepolis booking portal.'
    };
  }

  public async getBookingStatus(bookingId: string): Promise<{ status: string; message: string } | NotSupported> {
    return 'NOT_SUPPORTED';
  }

  public generateDeepLink(showId: string, cinemaId: string, movieId: string): string {
    return `cinepolis://show/${showId}?partner=ticketcompare`;
  }

  public generateWebUrl(showId: string, cinemaId: string, movieId: string): string {
    return `https://www.cinepolisindia.com/showtimes/${showId}`;
  }
}
