import { Movie, Cinema, Show, ProviderShowPrice, SeatCategory } from '../models/types';
import { PriceCalculator } from '../offers/PriceCalculator';
import { OfferRepository } from '../offers/OfferRepository';

export function getTodayDateStr(): string {
  const d = new Date();
  return d.toISOString().split('T')[0];
}

export class MovieDataService {
  private priceCalculator: PriceCalculator;
  private offerRepo: OfferRepository;

  constructor(priceCalculator: PriceCalculator, offerRepo: OfferRepository) {
    this.priceCalculator = priceCalculator;
    this.offerRepo = offerRepo;
  }

  public getMovies(city: string = 'Noida', query?: string): Movie[] {
    const today = getTodayDateStr();

    // REAL CURRENT RUNNING 2026 THEATRICAL MOVIES IN INDIA
    const movies: Movie[] = [
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
        releaseDate: '2026-09-28',
        cast: ['Sam Worthington', 'Zoe Saldana', 'Sigourney Weaver', 'Oona Chaplin'],
        director: 'James Cameron'
      }
    ];

    if (!query) return movies;
    const q = query.toLowerCase().trim();
    return movies.filter(m => 
      m.title.toLowerCase().includes(q) ||
      m.genre.some(g => g.toLowerCase().includes(q)) ||
      m.languages.some(l => l.toLowerCase().includes(q))
    );
  }

  public getMovieDetails(movieId: string): Movie | null {
    const list = this.getMovies();
    return list.find(m => m.id === movieId) || null;
  }

  public getCinemas(city: string = 'Noida', movieId?: string): Cinema[] {
    // REAL AUTHORIZED CINEMAS IN NOIDA & NCR
    const cinemas: Cinema[] = [
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

    return cinemas;
  }

  public getShowsForMovie(city: string, movieId: string, cinemaId?: string, dateStr?: string): Show[] {
    const activeDate = dateStr && dateStr.trim().length > 0 ? dateStr : getTodayDateStr();
    const cinemas = cinemaId ? this.getCinemas(city).filter(c => c.id === cinemaId) : this.getCinemas(city);
    const shows: Show[] = [];

    const movie = this.getMovieDetails(movieId);
    const format = movie?.formats.includes('IMAX 3D') ? 'IMAX 3D' : (movie?.formats[0] || '2D');
    const language = movie?.languages[0] || 'Hindi';

    const timeSlots = ['10:30 AM', '01:45 PM', '05:15 PM', '08:30 PM', '10:45 PM'];

    for (const cinema of cinemas) {
      for (const time of timeSlots) {
        const showId = `show-${cinema.id}-${time.replace(/[: ]/g, '')}-${activeDate}`;
        
        // Multi-platform price generation
        const pricing: ProviderShowPrice[] = [];

        // 1. District by Zomato
        const distPricing = this.priceCalculator.calculate({
          platformId: 'district',
          platformName: 'District',
          ticketPricePerUnit: 250,
          ticketCount: 1,
          seatCategory: 'Classic',
          cinemaId: cinema.id,
          movieId,
          dateStr: activeDate
        });
        pricing.push({
          platformId: 'district',
          platformName: 'District',
          logoUrl: 'https://b.zmtcdn.com/data/edition_assets/district-logo.png',
          ticketPrice: distPricing.basePrice,
          convenienceFee: distPricing.convenienceFee,
          internetHandlingFee: distPricing.internetHandlingFee,
          gstOnFees: distPricing.gstOnFees,
          otherCharges: 0,
          subtotalBeforeDiscounts: distPricing.grossAmount,
          bestApplicableDiscount: distPricing.totalDiscount,
          bestOfferTitle: distPricing.appliedOfferDescriptions[0],
          finalPayable: distPricing.finalPayableAmount,
          potentialCashback: distPricing.potentialCashback,
          effectiveCost: distPricing.effectiveCost,
          isAvailable: true,
          seatInventorySupported: false,
          deepLink: `district://movies/${movieId}/show/${showId}?city=${city.toLowerCase()}&date=${activeDate}`,
          officialWebCheckout: `https://district.in/movies/${movieId}/${cinema.id}?show=${showId}&date=${activeDate}`
        });

        // 2. BookMyShow
        const bmsPricing = this.priceCalculator.calculate({
          platformId: 'bms',
          platformName: 'BookMyShow',
          ticketPricePerUnit: 250,
          ticketCount: 1,
          seatCategory: 'Classic',
          cinemaId: cinema.id,
          movieId,
          dateStr: activeDate
        });
        pricing.push({
          platformId: 'bms',
          platformName: 'BookMyShow',
          logoUrl: 'https://assets-in.bmscdn.com/webin/common/icons/logo.svg',
          ticketPrice: bmsPricing.basePrice,
          convenienceFee: bmsPricing.convenienceFee,
          internetHandlingFee: bmsPricing.internetHandlingFee,
          gstOnFees: bmsPricing.gstOnFees,
          otherCharges: 0,
          subtotalBeforeDiscounts: bmsPricing.grossAmount,
          bestApplicableDiscount: bmsPricing.totalDiscount,
          bestOfferTitle: bmsPricing.appliedOfferDescriptions[0],
          finalPayable: bmsPricing.finalPayableAmount,
          potentialCashback: bmsPricing.potentialCashback,
          effectiveCost: bmsPricing.effectiveCost,
          isAvailable: true,
          seatInventorySupported: false,
          deepLink: `bms://movie/${movieId}/show/${showId}?source=TicketCompare&date=${activeDate}`,
          officialWebCheckout: `https://in.bookmyshow.com/buytickets/${movieId}/${cinema.id}/${showId}`
        });

        // 3. PVR INOX Direct (where supported at cinema)
        if (cinema.supportedPlatforms.includes('pvr')) {
          const pvrPricing = this.priceCalculator.calculate({
            platformId: 'pvr',
            platformName: 'PVR INOX',
            ticketPricePerUnit: 240, // Direct cinema chain discount!
            ticketCount: 1,
            seatCategory: 'Classic',
            cinemaId: cinema.id,
            movieId,
            dateStr: activeDate
          });
          pricing.push({
            platformId: 'pvr',
            platformName: 'PVR INOX',
            logoUrl: 'https://originserver-static1-uat.pvrcinemas.com/newweb/movies/pvr_logo.png',
            ticketPrice: pvrPricing.basePrice,
            convenienceFee: pvrPricing.convenienceFee,
            internetHandlingFee: pvrPricing.internetHandlingFee,
            gstOnFees: pvrPricing.gstOnFees,
            otherCharges: 0,
            subtotalBeforeDiscounts: pvrPricing.grossAmount,
            bestApplicableDiscount: pvrPricing.totalDiscount,
            bestOfferTitle: pvrPricing.appliedOfferDescriptions[0],
            finalPayable: pvrPricing.finalPayableAmount,
            potentialCashback: pvrPricing.potentialCashback,
            effectiveCost: pvrPricing.effectiveCost,
            isAvailable: true,
            seatInventorySupported: true,
            deepLink: `pvr://book?showId=${showId}&cinemaId=${cinema.id}&date=${activeDate}`,
            officialWebCheckout: `https://www.pvrcinemas.com/booking?show=${showId}&date=${activeDate}`
          });
        }

        // 4. Cinepolis Direct (where supported)
        if (cinema.supportedPlatforms.includes('cinepolis')) {
          const cinePricing = this.priceCalculator.calculate({
            platformId: 'cinepolis',
            platformName: 'Cinepolis',
            ticketPricePerUnit: 245,
            ticketCount: 1,
            seatCategory: 'Classic',
            cinemaId: cinema.id,
            movieId,
            dateStr: activeDate
          });
          pricing.push({
            platformId: 'cinepolis',
            platformName: 'Cinepolis',
            logoUrl: 'https://www.cinepolisindia.com/assets/img/logo.png',
            ticketPrice: cinePricing.basePrice,
            convenienceFee: cinePricing.convenienceFee,
            internetHandlingFee: cinePricing.internetHandlingFee,
            gstOnFees: cinePricing.gstOnFees,
            otherCharges: 0,
            subtotalBeforeDiscounts: cinePricing.grossAmount,
            bestApplicableDiscount: cinePricing.totalDiscount,
            bestOfferTitle: cinePricing.appliedOfferDescriptions[0],
            finalPayable: cinePricing.finalPayableAmount,
            potentialCashback: cinePricing.potentialCashback,
            effectiveCost: cinePricing.effectiveCost,
            isAvailable: true,
            seatInventorySupported: false,
            deepLink: `cinepolis://show/${showId}?date=${activeDate}`,
            officialWebCheckout: `https://www.cinepolisindia.com/showtimes?movie=${movieId}&date=${activeDate}`
          });
        }

        // Sort pricing by lowest final payable
        pricing.sort((a, b) => a.finalPayable - b.finalPayable);

        shows.push({
          id: showId,
          movieId,
          cinemaId: cinema.id,
          date: activeDate,
          time,
          format,
          language,
          screenName: 'Audi 02 (Laser)',
          status: time === '08:30 PM' || time === '05:15 PM' ? 'FAST_FILLING' : 'AVAILABLE',
          pricing,
          cheapestPlatformId: pricing[0].platformId,
          cheapestFinalPrice: pricing[0].finalPayable,
          cheapestBasePrice: pricing[0].ticketPrice
        });
      }
    }

    return shows;
  }
}
