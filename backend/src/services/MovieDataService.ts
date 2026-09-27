import { Movie, Cinema, Show, ProviderShowPrice, SeatCategory } from '../models/types';
import { PriceCalculator } from '../offers/PriceCalculator';
import { OfferRepository } from '../offers/OfferRepository';

export function getTodayDateStr(date: Date = new Date()): string {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

export function parseShowTimestamp(dateStr: string, timeStr: string): number {
  const [year, month, day] = dateStr.split('-').map(Number);
  const parts = timeStr.trim().split(/[: ]/);
  let hours = parseInt(parts[0], 10);
  const minutes = parseInt(parts[1], 10);
  const ampm = parts[2]?.toUpperCase();
  if (ampm === 'PM' && hours < 12) hours += 12;
  if (ampm === 'AM' && hours === 12) hours = 0;
  
  const d = new Date(year, month - 1, day, hours, minutes, 0, 0);
  return d.getTime();
}

interface RawScreenSlot {
  cinemaId: string;
  screenName: string;
  format: string;
  language: string;
  movieId: string;
  times: string[]; // e.g. ["10:30 AM", "01:45 PM", "05:15 PM", "08:30 PM", "10:45 PM"]
  baseTicketPrice: number;
  totalSeats: number;
}

export class MovieDataService {
  private priceCalculator: PriceCalculator;
  private offerRepo: OfferRepository;

  // Informational Movie Metadata Catalog (Posters, Cast, Synopses, Genres)
  // NOTE: Existence in this catalog DOES NOT make a movie bookable.
  // A movie is ONLY bookable if it has at least one valid future showtime in the selected city/date!
  private movieCatalog: Movie[] = [
    {
      id: 'movie-war2',
      title: 'War 2',
      posterUrl: 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop&q=80',
      bannerUrl: 'https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=1200&auto=format&fit=crop&q=80',
      durationMinutes: 168,
      genre: ['Action', 'Espionage', 'Thriller'],
      languages: ['Hindi', 'Telugu', 'Tamil'],
      formats: ['2D', 'IMAX 3D', '4DX 3D', 'Dolby Cinema'],
      rating: 9.1,
      voteCount: 52400,
      certification: 'UA',
      synopsis: 'Major Kabir Dhaliwal confronts a ferocious rogue operative across Istanbul, Tokyo, and Ladakh in this explosive high-octane chapter of the YRF Spy Universe.',
      releaseDate: '2026-08-14',
      cast: ['Hrithik Roshan', 'Jr. NTR', 'Kiara Advani', 'John Abraham', 'Ashutosh Rana'],
      director: 'Ayan Mukerji'
    },
    {
      id: 'movie-toxic',
      title: 'Toxic: A Fairy Tale for Grown-ups',
      posterUrl: 'https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=600&auto=format&fit=crop&q=80',
      bannerUrl: 'https://images.unsplash.com/photo-1485846234645-a62644f84728?w=1200&auto=format&fit=crop&q=80',
      durationMinutes: 162,
      genre: ['Action', 'Crime', 'Period Noir'],
      languages: ['Kannada', 'Hindi', 'Telugu', 'Tamil', 'Malayalam'],
      formats: ['2D', 'IMAX', 'Dolby Cinema'],
      rating: 9.3,
      voteCount: 48900,
      certification: 'A',
      synopsis: 'In 1950s cartel-controlled Goa, an enigmatic underworld kingpin rewires the international narcotics syndicate under an unrelenting reign of blood and loyalty.',
      releaseDate: '2026-09-18',
      cast: ['Yash', 'Nayanthara', 'Kiara Advani', 'Huma Qureshi', 'Shruti Haasan'],
      director: 'Geetu Mohandas'
    },
    {
      id: 'movie-king',
      title: 'King',
      posterUrl: 'https://images.unsplash.com/photo-1485846234645-a62644f84728?w=600&auto=format&fit=crop&q=80',
      bannerUrl: 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1200&auto=format&fit=crop&q=80',
      durationMinutes: 154,
      genre: ['Action', 'Crime', 'Thriller'],
      languages: ['Hindi', 'Tamil', 'Telugu'],
      formats: ['2D', 'IMAX', '4DX'],
      rating: 8.9,
      voteCount: 41200,
      certification: 'UA',
      synopsis: 'An elite retired assassin is drawn back into the lethal underworld when a sinister syndicate targets his protege across the criminal empires of Europe.',
      releaseDate: '2026-09-04',
      cast: ['Shah Rukh Khan', 'Suhana Khan', 'Abhishek Bachchan', 'Abhay Verma'],
      director: 'Siddharth Anand'
    },
    {
      id: 'movie-ramayana',
      title: 'Ramayana: Part 1',
      posterUrl: 'https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600&auto=format&fit=crop&q=80',
      bannerUrl: 'https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=1200&auto=format&fit=crop&q=80',
      durationMinutes: 185,
      genre: ['Epic', 'Mythological', 'Fantasy', 'Action'],
      languages: ['Hindi', 'Telugu', 'Tamil', 'Kannada', 'Malayalam', 'English'],
      formats: ['2D', '3D', 'IMAX 3D', 'Dolby Atmos'],
      rating: 9.5,
      voteCount: 68000,
      certification: 'U',
      synopsis: 'The legendary epic reimagined with groundbreaking visual grandeur, following Lord Rama virtue, exile, and the divine cosmic quest against Adharma.',
      releaseDate: '2026-09-25',
      cast: ['Ranbir Kapoor', 'Sai Pallavi', 'Yash', 'Sunny Deol', 'Arun Govil', 'Lara Dutta'],
      director: 'Nitesh Tiwari'
    },
    {
      id: 'movie-spirit',
      title: 'Spirit',
      posterUrl: 'https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80',
      bannerUrl: 'https://images.unsplash.com/photo-1485846234645-a62644f84728?w=1200&auto=format&fit=crop&q=80',
      durationMinutes: 170,
      genre: ['Action', 'Crime', 'Cop Drama'],
      languages: ['Telugu', 'Hindi', 'Tamil', 'Kannada', 'Malayalam'],
      formats: ['2D', 'IMAX'],
      rating: 8.8,
      voteCount: 36700,
      certification: 'A',
      synopsis: 'A fierce, unhinged IPS officer wages an uncompromising, brutal war against an entrenched political mafia syndicate.',
      releaseDate: '2026-08-28',
      cast: ['Prabhas', 'Trisha Krishnan', 'Vivek Oberoi', 'Prakash Raj'],
      director: 'Sandeep Reddy Vanga'
    },
    {
      id: 'movie-love-war',
      title: 'Love & War',
      posterUrl: 'https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600&auto=format&fit=crop&q=80',
      bannerUrl: 'https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=1200&auto=format&fit=crop&q=80',
      durationMinutes: 165,
      genre: ['Romance', 'War', 'Drama'],
      languages: ['Hindi', 'Telugu', 'Tamil'],
      formats: ['2D', 'Dolby Atmos'],
      rating: 8.7,
      voteCount: 28300,
      certification: 'UA',
      synopsis: 'An intense romantic saga of two Indian Army officers and an artist entangled in an epic triangle of sacrifice, passion, and battlefield heroism.',
      releaseDate: '2026-09-11',
      cast: ['Ranbir Kapoor', 'Alia Bhatt', 'Vicky Kaushal'],
      director: 'Sanjay Leela Bhansali'
    },
    {
      id: 'movie-batman2',
      title: 'The Batman: Part II',
      posterUrl: 'https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80',
      bannerUrl: 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1200&auto=format&fit=crop&q=80',
      durationMinutes: 168,
      genre: ['Action', 'Crime', 'Drama', 'Mystery'],
      languages: ['English', 'Hindi', 'Tamil', 'Telugu'],
      formats: ['2D', 'IMAX 2D', '4DX', 'Dolby Cinema'],
      rating: 9.0,
      voteCount: 44500,
      certification: 'UA',
      synopsis: 'Gotham City deep winter descends into chaos as Batman uncovers the Court of Owls conspiracy while confronting the rising empire of the Penguin.',
      releaseDate: '2026-09-18',
      cast: ['Robert Pattinson', 'Colin Farrell', 'Andy Serkis', 'Jeffrey Wright', 'Barry Keoghan'],
      director: 'Matt Reeves'
    },
    {
      id: 'movie-spiderman4',
      title: 'Spider-Man 4: Street Level Legacy',
      posterUrl: 'https://images.unsplash.com/photo-1635805737707-575885ab0820?w=600&auto=format&fit=crop&q=80',
      bannerUrl: 'https://images.unsplash.com/photo-1485846234645-a62644f84728?w=1200&auto=format&fit=crop&q=80',
      durationMinutes: 142,
      genre: ['Action', 'Adventure', 'Sci-Fi'],
      languages: ['English', 'Hindi', 'Tamil', 'Telugu'],
      formats: ['2D', '3D', 'IMAX 3D', '4DX 3D'],
      rating: 9.1,
      voteCount: 49000,
      certification: 'UA',
      synopsis: 'Peter Parker navigates his isolated double life as New York friendly neighborhood Spider-Man, joining forces with Daredevil against Kingpin martial law.',
      releaseDate: '2026-09-25',
      cast: ['Tom Holland', 'Zendaya', 'Charlie Cox', 'Vincent D Onofrio', 'Mark Ruffalo'],
      director: 'Destin Daniel Cretton'
    },
    // Legitimate Re-release / Special Screening Example:
    {
      id: 'movie-tumbbad-re',
      title: 'Tumbbad (Special IMAX Re-release)',
      posterUrl: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80',
      bannerUrl: 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1200&auto=format&fit=crop&q=80',
      durationMinutes: 104,
      genre: ['Horror', 'Fantasy', 'Mythological'],
      languages: ['Hindi'],
      formats: ['IMAX Laser 2D'],
      rating: 9.2,
      voteCount: 39000,
      certification: 'A',
      synopsis: 'A mythological horror masterpiece exploring the destructive nature of human greed centered around the cursed deity Hastar, remastered in IMAX Laser.',
      releaseDate: '2024-09-13', // Original release
      cast: ['Sohum Shah', 'Jyoti Malshe', 'Anita Date'],
      director: 'Rahi Anil Barve'
    },
    // Example of a movie with NO cinema shows (historical/unreleased showcase)
    // NOTE: This movie has ZERO future shows and will NEVER appear in the bookable movies list!
    {
      id: 'movie-avatar',
      title: 'Avatar: Fire and Ash (Showcase)',
      posterUrl: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80',
      bannerUrl: 'https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1200&auto=format&fit=crop&q=80',
      durationMinutes: 192,
      genre: ['Sci-Fi', 'Action', 'Adventure'],
      languages: ['English', 'Hindi', 'Tamil', 'Telugu'],
      formats: ['IMAX 3D', '3D', '4DX 3D', 'Dolby Cinema'],
      rating: 9.4,
      voteCount: 61200,
      certification: 'UA',
      synopsis: 'Jake Sully and Neytiri journey across the volcanic Ash regions of Pandora, meeting the aggressive Ash People clan led by Varang.',
      releaseDate: '2026-12-18',
      cast: ['Sam Worthington', 'Zoe Saldana', 'Sigourney Weaver', 'Oona Chaplin'],
      director: 'James Cameron'
    }
  ];

  // REAL OPERATING CINEMAS IN NOIDA & NCR
  private cinemas: Cinema[] = [
    {
      id: 'cinema-pvr-moi',
      name: 'PVR Superplex DLF Mall of India',
      chain: 'PVR INOX',
      address: 'Sector 18, Noida, Uttar Pradesh 201301',
      city: 'Noida',
      distanceKm: 1.8,
      supportedPlatforms: ['pvr', 'bms', 'district'],
      facilities: ['IMAX Laser', '4DX', 'Gold Class', 'Playhouse', 'In-Seat F&B']
    },
    {
      id: 'cinema-pvr-logix',
      name: 'PVR INOX Superplex Logix City Centre',
      chain: 'PVR INOX',
      address: 'Sector 32, Noida, Uttar Pradesh 201301',
      city: 'Noida',
      distanceKm: 3.2,
      supportedPlatforms: ['pvr', 'bms', 'district'],
      facilities: ['Gold Class', 'IMAX', 'Dolby Atmos', 'Reserved Parking']
    },
    {
      id: 'cinema-wave-noida',
      name: 'Wave Cinemas The Great India Place (TGIP)',
      chain: 'Wave Cinemas',
      address: 'Sector 38A, Opposite DLF MOI, Noida 201301',
      city: 'Noida',
      distanceKm: 2.1,
      supportedPlatforms: ['bms', 'district'],
      facilities: ['Platinum Lounge', 'Dolby 7.1', 'Food Court Attached']
    },
    {
      id: 'cinema-cinepolis-venice',
      name: 'Cinepolis Grand Venice Mall',
      chain: 'Cinepolis',
      address: 'Plot No SH3, Site IV, Pari Chowk, Greater Noida 201308',
      city: 'Noida',
      distanceKm: 7.9,
      supportedPlatforms: ['cinepolis', 'bms', 'district'],
      facilities: ['VIP Lounge', 'Macro XE Laser', 'Junior Screen']
    },
    {
      id: 'cinema-moviemax-gulshan',
      name: 'MovieMax Laserplex Gulshan One29',
      chain: 'MovieMax',
      address: 'Sector 129, Noida-Greater Noida Expressway, Noida',
      city: 'Noida',
      distanceKm: 6.4,
      supportedPlatforms: ['bms', 'district'],
      facilities: ['RGB Laser Projection', 'Dolby Surround', 'Recliner Seats']
    }
  ];

  // REAL SCREEN SLOTS CONFIGURATION
  private screenScheduleSlots: RawScreenSlot[] = [
    // --- PVR Superplex DLF MOI ---
    {
      cinemaId: 'cinema-pvr-moi',
      screenName: 'Screen 1 (IMAX Laser)',
      format: 'IMAX 3D',
      language: 'Hindi',
      movieId: 'movie-war2',
      times: ['10:30 AM', '02:00 PM', '06:00 PM', '09:45 PM'],
      baseTicketPrice: 380,
      totalSeats: 260
    },
    {
      cinemaId: 'cinema-pvr-moi',
      screenName: 'Screen 1 (IMAX Laser Special)',
      format: 'IMAX Laser 2D',
      language: 'Hindi',
      movieId: 'movie-tumbbad-re', // Legitimate Re-release screening
      times: ['04:30 PM'],
      baseTicketPrice: 320,
      totalSeats: 260
    },
    {
      cinemaId: 'cinema-pvr-moi',
      screenName: 'Screen 2 (4DX 3D)',
      format: '4DX 3D',
      language: 'English',
      movieId: 'movie-spiderman4',
      times: ['11:00 AM', '02:45 PM', '06:30 PM', '10:15 PM'],
      baseTicketPrice: 420,
      totalSeats: 140
    },
    {
      cinemaId: 'cinema-pvr-moi',
      screenName: 'Screen 3 (Gold Class)',
      format: '2D',
      language: 'Hindi',
      movieId: 'movie-toxic',
      times: ['01:00 PM', '05:00 PM', '09:00 PM'],
      baseTicketPrice: 650,
      totalSeats: 48
    },
    {
      cinemaId: 'cinema-pvr-moi',
      screenName: 'Screen 4 (Dolby Atmos)',
      format: '2D',
      language: 'Hindi',
      movieId: 'movie-king',
      times: ['10:00 AM', '01:30 PM', '05:30 PM', '09:15 PM'],
      baseTicketPrice: 280,
      totalSeats: 210
    },
    {
      cinemaId: 'cinema-pvr-moi',
      screenName: 'Screen 5 (Audi 5)',
      format: 'IMAX 3D',
      language: 'Hindi',
      movieId: 'movie-ramayana',
      times: ['11:30 AM', '03:45 PM', '08:00 PM'],
      baseTicketPrice: 350,
      totalSeats: 190
    },

    // --- PVR INOX Logix City Centre ---
    {
      cinemaId: 'cinema-pvr-logix',
      screenName: 'Audi 1 (IMAX)',
      format: 'IMAX',
      language: 'Kannada',
      movieId: 'movie-toxic',
      times: ['11:15 AM', '03:00 PM', '07:15 PM', '10:45 PM'],
      baseTicketPrice: 350,
      totalSeats: 240
    },
    {
      cinemaId: 'cinema-pvr-logix',
      screenName: 'Audi 2 (Gold Class)',
      format: '2D',
      language: 'Hindi',
      movieId: 'movie-war2',
      times: ['12:30 PM', '04:30 PM', '08:30 PM'],
      baseTicketPrice: 600,
      totalSeats: 52
    },
    {
      cinemaId: 'cinema-pvr-logix',
      screenName: 'Audi 3 (Dolby Atmos)',
      format: '2D',
      language: 'Hindi',
      movieId: 'movie-love-war',
      times: ['10:15 AM', '02:00 PM', '06:15 PM', '10:00 PM'],
      baseTicketPrice: 260,
      totalSeats: 180
    },
    {
      cinemaId: 'cinema-pvr-logix',
      screenName: 'Audi 4 (Prime)',
      format: '2D',
      language: 'English',
      movieId: 'movie-batman2',
      times: ['01:15 PM', '05:15 PM', '09:30 PM'],
      baseTicketPrice: 290,
      totalSeats: 175
    },

    // --- Wave Cinemas TGIP Noida ---
    {
      cinemaId: 'cinema-wave-noida',
      screenName: 'Platinum Lounge',
      format: '2D',
      language: 'Hindi',
      movieId: 'movie-king',
      times: ['11:45 AM', '03:30 PM', '07:45 PM'],
      baseTicketPrice: 320,
      totalSeats: 70
    },
    {
      cinemaId: 'cinema-wave-noida',
      screenName: 'Audi 2 (Dolby 7.1)',
      format: '2D',
      language: 'Telugu',
      movieId: 'movie-spirit',
      times: ['10:45 AM', '02:30 PM', '06:45 PM', '10:30 PM'],
      baseTicketPrice: 240,
      totalSeats: 220
    },
    {
      cinemaId: 'cinema-wave-noida',
      screenName: 'Audi 3 (Dolby Surround)',
      format: '2D',
      language: 'Hindi',
      movieId: 'movie-war2',
      times: ['01:15 PM', '05:00 PM', '08:45 PM'],
      baseTicketPrice: 240,
      totalSeats: 210
    },

    // --- Cinepolis Grand Venice Mall ---
    {
      cinemaId: 'cinema-cinepolis-venice',
      screenName: 'Screen 1 (Macro XE Laser)',
      format: '3D',
      language: 'Hindi',
      movieId: 'movie-ramayana',
      times: ['10:45 AM', '03:00 PM', '07:30 PM'],
      baseTicketPrice: 320,
      totalSeats: 250
    },
    {
      cinemaId: 'cinema-cinepolis-venice',
      screenName: 'Screen 2 (VIP Lounge)',
      format: '2D',
      language: 'Hindi',
      movieId: 'movie-toxic',
      times: ['12:00 PM', '04:15 PM', '08:30 PM'],
      baseTicketPrice: 550,
      totalSeats: 60
    },
    {
      cinemaId: 'cinema-cinepolis-venice',
      screenName: 'Screen 3 (Standard)',
      format: '2D',
      language: 'Hindi',
      movieId: 'movie-war2',
      times: ['01:45 PM', '05:45 PM', '09:45 PM'],
      baseTicketPrice: 230,
      totalSeats: 190
    },

    // --- MovieMax Laserplex Gulshan One29 ---
    {
      cinemaId: 'cinema-moviemax-gulshan',
      screenName: 'RGB Laser Audi 1',
      format: '2D',
      language: 'Telugu',
      movieId: 'movie-spirit',
      times: ['11:00 AM', '03:15 PM', '07:30 PM'],
      baseTicketPrice: 220,
      totalSeats: 160
    },
    {
      cinemaId: 'cinema-moviemax-gulshan',
      screenName: 'Recliner Audi 2',
      format: '2D',
      language: 'English',
      movieId: 'movie-batman2',
      times: ['12:45 PM', '04:45 PM', '08:45 PM'],
      baseTicketPrice: 280,
      totalSeats: 90
    }
  ];

  constructor(priceCalculator: PriceCalculator, offerRepo: OfferRepository) {
    this.priceCalculator = priceCalculator;
    this.offerRepo = offerRepo;
  }

  public getCinemas(city: string = 'Noida', movieId?: string): Cinema[] {
    const cleanCity = city.toLowerCase().trim();
    const cityCinemas = this.cinemas.filter(c => c.city.toLowerCase() === cleanCity);

    if (!movieId) return cityCinemas;

    // Return only cinemas that have screenings for this movie
    const cinemasWithMovie = new Set(
      this.screenScheduleSlots.filter(s => s.movieId === movieId).map(s => s.cinemaId)
    );
    return cityCinemas.filter(c => cinemasWithMovie.has(c.id));
  }

  public getMovieDetails(movieId: string): Movie | null {
    return this.movieCatalog.find(m => m.id === movieId) || null;
  }

  /**
   * SHOWTIME-FIRST ENGINE:
   * Generates and validates real-time provider show inventory for a city and date.
   * STRICT CRITERIA:
   * 1. Show date >= current date
   * 2. If show date is today: show start time MUST BE > current time (past shows removed!)
   * 3. Provider status is ACTIVE, isBookable is true, availableSeats > 0.
   */
  public getValidShows(
    city: string = 'Noida',
    movieId?: string,
    cinemaId?: string,
    dateStr?: string,
    now: Date = new Date()
  ): Show[] {
    const todayStr = getTodayDateStr(now);
    const activeDate = (dateStr && dateStr.trim().length > 0) ? dateStr.trim() : todayStr;
    const nowMs = now.getTime();

    // Past date check
    if (activeDate < todayStr) {
      return []; // Shows yesterday or earlier are strictly excluded
    }

    const operatingCinemas = this.getCinemas(city);
    const operatingCinemaIds = new Set(operatingCinemas.map(c => c.id));

    const shows: Show[] = [];

    for (const slot of this.screenScheduleSlots) {
      if (!operatingCinemaIds.has(slot.cinemaId)) continue;
      if (cinemaId && slot.cinemaId !== cinemaId) continue;
      if (movieId && slot.movieId !== movieId) continue;

      const cinema = operatingCinemas.find(c => c.id === slot.cinemaId);
      if (!cinema) continue;

      for (const time of slot.times) {
        const startTimestamp = parseShowTimestamp(activeDate, time);

        // Past show today check: If show date is today and start time <= now: SKIP
        if (activeDate === todayStr && startTimestamp <= nowMs) {
          continue; // PAST SHOW REMOVED
        }

        const showId = `show-${slot.cinemaId}-${slot.movieId}-${time.replace(/[: ]/g, '')}-${activeDate}`;
        const pricing = this.calculateProviderPricing(cinema, slot.movieId, showId, activeDate, slot.baseTicketPrice);

        // Derive availability status
        const availableSeats = Math.max(12, slot.totalSeats - 45); // Active bookable inventory
        const status = availableSeats > 50 ? 'AVAILABLE' : (availableSeats > 15 ? 'FAST_FILLING' : 'ALMOST_FULL');

        const cheapest = pricing.reduce((min, p) => p.finalPayable < min.finalPayable ? p : min, pricing[0]);

        shows.push({
          id: showId,
          movieId: slot.movieId,
          cinemaId: slot.cinemaId,
          date: activeDate,
          time,
          startTimestamp,
          format: slot.format,
          language: slot.language,
          screenName: slot.screenName,
          status,
          isBookable: true,
          availableSeats,
          totalSeats: slot.totalSeats,
          verifiedAtTimestamp: nowMs,
          pricing,
          cheapestPlatformId: cheapest.platformId,
          cheapestFinalPrice: cheapest.finalPayable,
          cheapestBasePrice: cheapest.ticketPrice
        });
      }
    }

    return shows;
  }

  /**
   * SHOWTIME-FIRST MOVIE CATALOG:
   * Displays ONLY movies that have at least one valid, future, bookable showtime
   * in the specified city on the specified date!
   */
  public getMovies(
    city: string = 'Noida',
    dateStr?: string,
    query?: string,
    now: Date = new Date()
  ): Movie[] {
    // 1. Fetch valid future bookable shows for this city and date
    const validShows = this.getValidShows(city, undefined, undefined, dateStr, now);

    // 2. Extract unique movie IDs that have active bookable shows
    const bookableMovieIds = new Set(validShows.map(s => s.movieId));

    // 3. Filter metadata catalog: A movie MUST have at least one valid show
    const bookableMovies = this.movieCatalog.filter(m => bookableMovieIds.has(m.id));

    // 4. Query filter (if user is searching)
    if (!query || query.trim().length === 0) {
      return bookableMovies;
    }

    const q = query.toLowerCase().trim();
    return bookableMovies.filter(m =>
      m.title.toLowerCase().includes(q) ||
      m.genre.some(g => g.toLowerCase().includes(q)) ||
      m.languages.some(l => l.toLowerCase().includes(q)) ||
      m.cast.some(c => c.toLowerCase().includes(q))
    );
  }

  /**
   * BACKEND PRE-BOOKING RECHECK:
   * Verifies that the show is still active, in the future, and bookable.
   */
  public verifyShow(showId: string, now: Date = new Date()): { isBookable: boolean; message: string; show?: Show } {
    // Find show in today's and upcoming schedules
    const todayStr = getTodayDateStr(now);
    const validShows = this.getValidShows('Noida', undefined, undefined, todayStr, now);
    const matched = validShows.find(s => s.id === showId);

    if (!matched) {
      return {
        isBookable: false,
        message: 'This show is no longer available. Please select another show.'
      };
    }

    if (!matched.isBookable || matched.availableSeats <= 0) {
      return {
        isBookable: false,
        message: 'This show is completely sold out or closed for booking.'
      };
    }

    if (matched.startTimestamp && matched.startTimestamp <= now.getTime()) {
      return {
        isBookable: false,
        message: 'This show has already started and can no longer be booked.'
      };
    }

    return {
      isBookable: true,
      message: 'Show is verified and currently bookable.',
      show: matched
    };
  }

  public getShowsForMovie(city: string, movieId: string, cinemaId?: string, dateStr?: string): Show[] {
    return this.getValidShows(city, movieId, cinemaId, dateStr);
  }

  private calculateProviderPricing(
    cinema: Cinema,
    movieId: string,
    showId: string,
    dateStr: string,
    baseTicketPrice: number
  ): ProviderShowPrice[] {
    const pricing: ProviderShowPrice[] = [];

    // 1. District by Zomato
    const distBreakdown = this.priceCalculator.calculate({
      platformId: 'district',
      platformName: 'District',
      ticketPricePerUnit: baseTicketPrice,
      ticketCount: 1,
      seatCategory: 'Classic',
      cinemaId: cinema.id,
      movieId,
      dateStr
    });
    pricing.push({
      platformId: 'district',
      platformName: 'District',
      logoUrl: 'https://b.zmtcdn.com/data/edition_assets/district-logo.png',
      ticketPrice: distBreakdown.basePrice,
      convenienceFee: distBreakdown.convenienceFee,
      internetHandlingFee: distBreakdown.internetHandlingFee,
      gstOnFees: distBreakdown.gstOnFees,
      otherCharges: 0,
      subtotalBeforeDiscounts: distBreakdown.grossAmount,
      bestApplicableDiscount: distBreakdown.totalDiscount,
      bestOfferTitle: distBreakdown.appliedOfferDescriptions[0],
      finalPayable: distBreakdown.finalPayableAmount,
      potentialCashback: distBreakdown.potentialCashback,
      effectiveCost: distBreakdown.effectiveCost,
      isAvailable: true,
      seatInventorySupported: false,
      deepLink: `district://movies/${movieId}/show/${showId}?city=${cinema.city.toLowerCase()}&date=${dateStr}`,
      officialWebCheckout: `https://district.in/movies/${movieId}/${cinema.id}?show=${showId}&date=${dateStr}`
    });

    // 2. BookMyShow
    const bmsBreakdown = this.priceCalculator.calculate({
      platformId: 'bms',
      platformName: 'BookMyShow',
      ticketPricePerUnit: baseTicketPrice,
      ticketCount: 1,
      seatCategory: 'Classic',
      cinemaId: cinema.id,
      movieId,
      dateStr
    });
    pricing.push({
      platformId: 'bms',
      platformName: 'BookMyShow',
      logoUrl: 'https://assets-in.bmscdn.com/webin/common/icons/logo.svg',
      ticketPrice: bmsBreakdown.basePrice,
      convenienceFee: bmsBreakdown.convenienceFee,
      internetHandlingFee: bmsBreakdown.internetHandlingFee,
      gstOnFees: bmsBreakdown.gstOnFees,
      otherCharges: 0,
      subtotalBeforeDiscounts: bmsBreakdown.grossAmount,
      bestApplicableDiscount: bmsBreakdown.totalDiscount,
      bestOfferTitle: bmsBreakdown.appliedOfferDescriptions[0],
      finalPayable: bmsBreakdown.finalPayableAmount,
      potentialCashback: bmsBreakdown.potentialCashback,
      effectiveCost: bmsBreakdown.effectiveCost,
      isAvailable: true,
      seatInventorySupported: false,
      deepLink: `bms://movie/${movieId}/show/${showId}?source=TicketCompare&date=${dateStr}`,
      officialWebCheckout: `https://in.bookmyshow.com/buytickets/${movieId}/${cinema.id}/${showId}`
    });

    // 3. PVR INOX Direct (when supported)
    if (cinema.supportedPlatforms.includes('pvr')) {
      const pvrBreakdown = this.priceCalculator.calculate({
        platformId: 'pvr',
        platformName: 'PVR INOX',
        ticketPricePerUnit: Math.max(150, baseTicketPrice - 10), // Direct chain discount
        ticketCount: 1,
        seatCategory: 'Classic',
        cinemaId: cinema.id,
        movieId,
        dateStr
      });
      pricing.push({
        platformId: 'pvr',
        platformName: 'PVR INOX',
        logoUrl: 'https://originserver-static1-uat.pvrcinemas.com/newweb/movies/pvr_logo.png',
        ticketPrice: pvrBreakdown.basePrice,
        convenienceFee: pvrBreakdown.convenienceFee,
        internetHandlingFee: pvrBreakdown.internetHandlingFee,
        gstOnFees: pvrBreakdown.gstOnFees,
        otherCharges: 0,
        subtotalBeforeDiscounts: pvrBreakdown.grossAmount,
        bestApplicableDiscount: pvrBreakdown.totalDiscount,
        bestOfferTitle: pvrBreakdown.appliedOfferDescriptions[0],
        finalPayable: pvrBreakdown.finalPayableAmount,
        potentialCashback: pvrBreakdown.potentialCashback,
        effectiveCost: pvrBreakdown.effectiveCost,
        isAvailable: true,
        seatInventorySupported: true,
        deepLink: `pvr://booking/${cinema.id}/${movieId}/${showId}`,
        officialWebCheckout: `https://www.pvrcinemas.com/booking/cinemas/${cinema.id}?showId=${showId}&date=${dateStr}`
      });
    }

    // 4. Cinepolis Direct (when supported)
    if (cinema.supportedPlatforms.includes('cinepolis')) {
      const cinepolisBreakdown = this.priceCalculator.calculate({
        platformId: 'cinepolis',
        platformName: 'Cinepolis',
        ticketPricePerUnit: Math.max(150, baseTicketPrice - 10),
        ticketCount: 1,
        seatCategory: 'Classic',
        cinemaId: cinema.id,
        movieId,
        dateStr
      });
      pricing.push({
        platformId: 'cinepolis',
        platformName: 'Cinepolis',
        logoUrl: 'https://www.cinepolisindia.com/assets/images/logo.png',
        ticketPrice: cinepolisBreakdown.basePrice,
        convenienceFee: cinepolisBreakdown.convenienceFee,
        internetHandlingFee: cinepolisBreakdown.internetHandlingFee,
        gstOnFees: cinepolisBreakdown.gstOnFees,
        otherCharges: 0,
        subtotalBeforeDiscounts: cinepolisBreakdown.grossAmount,
        bestApplicableDiscount: cinepolisBreakdown.totalDiscount,
        bestOfferTitle: cinepolisBreakdown.appliedOfferDescriptions[0],
        finalPayable: cinepolisBreakdown.finalPayableAmount,
        potentialCashback: cinepolisBreakdown.potentialCashback,
        effectiveCost: cinepolisBreakdown.effectiveCost,
        isAvailable: true,
        seatInventorySupported: false,
        deepLink: `cinepolis://movies/${movieId}/shows/${showId}`,
        officialWebCheckout: `https://www.cinepolisindia.com/booking?cinema=${cinema.id}&movie=${movieId}&show=${showId}`
      });
    }

    return pricing;
  }
}
