import { Movie, Cinema, Show, SeatLayout, PriceBreakdown, BookingRequest, BookingResult, SeatCategory } from '../models/types';
import { Offer } from '../offers/OfferTypes';

export type NotSupported = 'NOT_SUPPORTED';

export interface MovieProvider {
  readonly id: string;
  readonly name: string;
  readonly logoUrl: string;
  readonly isOfficialPartner: boolean;
  readonly supportsSeatLayout: boolean;
  readonly supportsDirectBooking: boolean;

  searchMovies(city: string, query?: string): Promise<Movie[]>;
  getMovieDetails(movieId: string): Promise<Movie | null>;
  getCinemas(city: string, movieId?: string): Promise<Cinema[]>;
  getShows(city: string, movieId: string, cinemaId: string, date: string): Promise<Show[]>;
  getSeatLayout(showId: string): Promise<SeatLayout | NotSupported>;
  getPricing(showId: string, ticketCount: number, seatType: SeatCategory): Promise<PriceBreakdown>;
  getOffers(): Promise<Offer[]>;
  createBooking(request: BookingRequest): Promise<BookingResult | NotSupported>;
  getBookingStatus(bookingId: string): Promise<{ status: string; message: string } | NotSupported>;
  generateDeepLink(showId: string, cinemaId: string, movieId: string): string;
  generateWebUrl(showId: string, cinemaId: string, movieId: string): string;
}
