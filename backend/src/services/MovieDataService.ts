import { Movie, Cinema, Show, ProviderShowPrice, SeatCategory } from '../models/types';
import { PriceCalculator } from '../offers/PriceCalculator';
import { OfferRepository } from '../offers/OfferRepository';

export class MovieDataService {
  private priceCalculator: PriceCalculator;
  private offerRepo: OfferRepository;

  constructor(priceCalculator: PriceCalculator, offerRepo: OfferRepository) {
    this.priceCalculator = priceCalculator;
    this.offerRepo = offerRepo;
  }

  public getMovies(city: string = 'Noida', query?: string): Movie[] {
    const movies: Movie[] = [
      {
        id: 'movie-avatar',
        title: 'Avatar: The Way of Water',
        posterUrl: 'https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/8YFL5QQVPy3AgrEQxNYvsgiPEbe.jpg',
        durationMinutes: 192,
        genre: ['Sci-Fi', 'Action', 'Adventure'],
        languages: ['Hindi', 'English', 'Tamil', 'Telugu'],
        formats: ['2D', '3D', 'IMAX 3D', '4DX 3D'],
        rating: 8.9,
        voteCount: 14250,
        certification: 'UA',
        synopsis: 'Jake Sully lives with his newfound family formed on the extrasolar moon Pandora. Once a familiar threat returns to finish what was previously started, Jake must work with Neytiri and the army of the Na\'vi race to protect their home.',
        releaseDate: '2026-10-05',
        cast: ['Sam Worthington', 'Zoe Saldana', 'Sigourney Weaver', 'Stephen Lang', 'Kate Winslet'],
        director: 'James Cameron'
      },
      {
        id: 'movie-kalki',
        title: 'Kalki 2898 AD',
        posterUrl: 'https://image.tmdb.org/t/p/w500/z0T0q7uM0D99q1aX3x90m4q0p.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/w2R3x1lZ2u7A9V7nQ90m4q0p.jpg',
        durationMinutes: 181,
        genre: ['Mythology', 'Sci-Fi', 'Action'],
        languages: ['Hindi', 'Telugu', 'Tamil', 'Malayalam', 'Kannada'],
        formats: ['2D', '3D', 'IMAX 3D'],
        rating: 8.7,
        voteCount: 22100,
        certification: 'UA',
        synopsis: 'A modern avatar of Vishnu, a Hindu god, who is believed to have descended to the earth to protect the world from evil forces.',
        releaseDate: '2026-09-20',
        cast: ['Prabhas', 'Amitabh Bachchan', 'Kamal Haasan', 'Deepika Padukone'],
        director: 'Nag Ashwin'
      },
      {
        id: 'movie-stree2',
        title: 'Stree 2: Sarkate Ka Aatank',
        posterUrl: 'https://image.tmdb.org/t/p/w500/4q25XgT7q9vW3m1aX4y8p0q9.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/5q36YhU8r0wX4n2bY5z9q1r0.jpg',
        durationMinutes: 147,
        genre: ['Comedy', 'Horror'],
        languages: ['Hindi'],
        formats: ['2D'],
        rating: 8.4,
        voteCount: 19800,
        certification: 'UA',
        synopsis: 'The town of Chanderi is haunted once again, this time by a headless entity abducting women. Vicky and his friends band together with Stree to save the town.',
        releaseDate: '2026-08-15',
        cast: ['Rajkummar Rao', 'Shraddha Kapoor', 'Pankaj Tripathi', 'Abhishek Banerjee'],
        director: 'Amar Kaushik'
      },
      {
        id: 'movie-devara',
        title: 'Devara: Part 1',
        posterUrl: 'https://image.tmdb.org/t/p/w500/6r12YgT8r0wX4n2bY5z9q1r0.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/7s23ZhU9s1xY5o3cZ6a0r2s1.jpg',
        durationMinutes: 178,
        genre: ['Action', 'Drama', 'Thriller'],
        languages: ['Telugu', 'Hindi', 'Tamil'],
        formats: ['2D', 'IMAX'],
        rating: 8.2,
        voteCount: 12500,
        certification: 'A',
        synopsis: 'An epic action saga set against coastal lands, chronicling fear and retribution across generations.',
        releaseDate: '2026-09-27',
        cast: ['NTR Jr', 'Janhvi Kapoor', 'Saif Ali Khan', 'Prakash Raj'],
        director: 'Koratala Siva'
      },
      {
        id: 'movie-oppenheimer',
        title: 'Oppenheimer (Re-release)',
        posterUrl: 'https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/rLb2cwF3Pazuxaj0sRXQ037tGI1.jpg',
        durationMinutes: 180,
        genre: ['Biography', 'Drama', 'History'],
        languages: ['English', 'Hindi'],
        formats: ['IMAX 70mm', 'IMAX', '2D'],
        rating: 9.1,
        voteCount: 35000,
        certification: 'A',
        synopsis: 'The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb.',
        releaseDate: '2026-10-01',
        cast: ['Cillian Murphy', 'Emily Blunt', 'Matt Damon', 'Robert Downey Jr.'],
        director: 'Christopher Nolan'
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
    const cinemas: Cinema[] = [
      {
        id: 'cinema-pvr-moi',
        name: 'PVR INOX Mall of India',
        chain: 'PVR INOX',
        address: 'Sector 18, Noida, Uttar Pradesh 201301',
        city: 'Noida',
        distanceKm: 2.1,
        supportedPlatforms: ['pvr', 'bms', 'district'],
        facilities: ['IMAX Laser', '4DX', 'Recliner', 'F&B In-Seat']
      },
      {
        id: 'cinema-pvr-logix',
        name: 'PVR Superplex Logix City Centre',
        chain: 'PVR INOX',
        address: 'Sector 32, Noida, Uttar Pradesh 201301',
        city: 'Noida',
        distanceKm: 3.4,
        supportedPlatforms: ['pvr', 'bms', 'district'],
        facilities: ['Gold Class', 'IMAX', 'Dolby Atmos', 'Parking']
      },
      {
        id: 'cinema-wave-noida',
        name: 'Wave Cinemas Noida',
        chain: 'Wave Cinemas',
        address: 'The Great India Place, Sector 38A, Noida',
        city: 'Noida',
        distanceKm: 2.5,
        supportedPlatforms: ['bms', 'district'],
        facilities: ['Platinum Lounge', 'Dolby 7.1', 'Parking']
      },
      {
        id: 'cinema-cinepolis-venice',
        name: 'Cinepolis Grand Venice Mall',
        chain: 'Cinepolis',
        address: 'Greater Noida, Uttar Pradesh 201308',
        city: 'Noida',
        distanceKm: 8.6,
        supportedPlatforms: ['cinepolis', 'bms', 'district'],
        facilities: ['VIP Lounge', 'Macro XE', 'Junior']
      }
    ];

    return cinemas;
  }

  public getShowsForMovie(city: string, movieId: string, cinemaId?: string, dateStr: string = '2026-10-05'): Show[] {
    const cinemas = cinemaId ? this.getCinemas(city).filter(c => c.id === cinemaId) : this.getCinemas(city);
    const shows: Show[] = [];

    const timeSlots = ['10:30 AM', '01:45 PM', '05:15 PM', '08:30 PM'];

    for (const cinema of cinemas) {
      for (const time of timeSlots) {
        const showId = `show-${cinema.id}-${time.replace(/[: ]/g, '')}`;
        
        // Multi-platform price generation
        const pricing: ProviderShowPrice[] = [];

        // 1. District Pricing
        const distPricing = this.priceCalculator.calculate({
          platformId: 'district',
          platformName: 'District',
          ticketPricePerUnit: 250,
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
          deepLink: `district://movies/${movieId}/show/${showId}?ref=ticketcompare`,
          officialWebCheckout: `https://district.in/movies/${movieId}/${cinema.id}?show=${showId}`
        });

        // 2. BookMyShow Pricing
        const bmsPricing = this.priceCalculator.calculate({
          platformId: 'bms',
          platformName: 'BookMyShow',
          ticketPricePerUnit: 250,
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
          deepLink: `bms://movie/${movieId}/show/${showId}?source=TicketCompare`,
          officialWebCheckout: `https://in.bookmyshow.com/buytickets/${movieId}/${cinema.id}/${showId}`
        });

        // 3. PVR INOX Direct (if supported at cinema)
        if (cinema.supportedPlatforms.includes('pvr')) {
          const pvrPricing = this.priceCalculator.calculate({
            platformId: 'pvr',
            platformName: 'PVR INOX',
            ticketPricePerUnit: 240, // Direct cinema chain discount!
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
            deepLink: `pvr://book?showId=${showId}&cinemaId=${cinema.id}&affiliate=ticketcompare`,
            officialWebCheckout: `https://www.pvrcinemas.com/booking?show=${showId}`
          });
        }

        // 4. Cinepolis (if supported at cinema)
        if (cinema.supportedPlatforms.includes('cinepolis')) {
          const cinePricing = this.priceCalculator.calculate({
            platformId: 'cinepolis',
            platformName: 'Cinepolis',
            ticketPricePerUnit: 245,
            ticketCount: 1,
            seatCategory: 'Classic',
            cinemaId: cinema.id,
            movieId,
            dateStr
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
            deepLink: `cinepolis://show/${showId}?partner=ticketcompare`,
            officialWebCheckout: `https://www.cinepolisindia.com/showtimes/${showId}`
          });
        }

        // Sort pricing by lowest final payable
        pricing.sort((a, b) => a.finalPayable - b.finalPayable);

        shows.push({
          id: showId,
          movieId,
          cinemaId: cinema.id,
          date: dateStr,
          time,
          format: 'IMAX 3D',
          language: 'Hindi',
          screenName: 'Audi 03 (Laser)',
          status: time === '08:30 PM' ? 'FAST_FILLING' : 'AVAILABLE',
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
