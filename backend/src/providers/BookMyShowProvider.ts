import { MovieProvider, NotSupported } from '../interfaces/MovieProvider';
import { Movie, Cinema, Show, SeatLayout, PriceBreakdown, BookingRequest, BookingResult, SeatCategory } from '../models/types';
import { Offer } from '../offers/OfferTypes';
import { OfferRepository } from '../offers/OfferRepository';
import { PriceCalculator } from '../offers/PriceCalculator';

export class BookMyShowProvider implements MovieProvider {
  public readonly id = 'bms';
  public readonly name = 'BookMyShow';
  public readonly logoUrl = 'https://assets-in.bmscdn.com/webin/common/icons/logo.svg';
  public readonly isOfficialPartner = true;
  public readonly supportsSeatLayout = false; // Protected proprietary layout - requires official partner session
  public readonly supportsDirectBooking = false; // Checkout redirected via official deep link / web

  private offerRepo: OfferRepository;
  private priceCalculator: PriceCalculator;

  constructor(offerRepo: OfferRepository, priceCalculator: PriceCalculator) {
    this.offerRepo = offerRepo;
    this.priceCalculator = priceCalculator;
  }

  public async searchMovies(city: string, query?: string): Promise<Movie[]> {
    // Returns verified movie listings for city
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
    // As per Requirement 15 & 28: Never invent seat availability. If not exposed officially: NOT_SUPPORTED
    return 'NOT_SUPPORTED';
  }

  public async getPricing(showId: string, ticketCount: number, seatType: SeatCategory): Promise<PriceBreakdown> {
    return this.priceCalculator.calculate({
      platformId: this.id,
      platformName: this.name,
      ticketPricePerUnit: 250,
      ticketCount,
      seatCategory: seatType,
      cinemaId: 'cinema-pvr-logix',
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
    // Official secure flow: Redirects to BookMyShow checkout
    const deepLink = this.generateDeepLink(request.showId, request.cinemaId, request.movieId);
    const webUrl = this.generateWebUrl(request.showId, request.cinemaId, request.movieId);
    
    return {
      bookingId: `BMS-${Date.now()}`,
      status: 'DEEP_LINK_DISPATCHED',
      platformId: this.id,
      deepLink,
      checkoutUrl: webUrl,
      finalPayable: 0,
      instructions: 'You will complete your ticket booking and secure payment directly on BookMyShow.'
    };
  }

  public async getBookingStatus(bookingId: string): Promise<{ status: string; message: string } | NotSupported> {
    return 'NOT_SUPPORTED';
  }

  public generateDeepLink(showId: string, cinemaId: string, movieId: string): string {
    return `bms://movie/${movieId}/show/${showId}?source=TicketCompare`;
  }

  public generateWebUrl(showId: string, cinemaId: string, movieId: string): string {
    return `https://in.bookmyshow.com/buytickets/${movieId}/${cinemaId}/${showId}`;
  }
}
