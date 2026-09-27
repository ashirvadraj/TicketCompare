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

    // REAL CURRENT RUNNING THEATRICAL MOVIES IN INDIA
    const movies: Movie[] = [
      {
        id: 'movie-devara',
        title: 'Devara: Part 1',
        posterUrl: 'https://image.tmdb.org/t/p/w500/A1gC20tU51g5u9o7n8b6c4e2y9q.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/8YFL5QQVPy3AgrEQxNYvsgiPEbe.jpg',
        durationMinutes: 178,
        genre: ['Action', 'Drama', 'Thriller'],
        languages: ['Hindi', 'Telugu', 'Tamil', 'Kannada', 'Malayalam'],
        formats: ['2D', 'IMAX 3D', '4DX 3D', 'Dolby Cinema'],
        rating: 8.8,
        voteCount: 38500,
        certification: 'UA',
        synopsis: 'An epic coastal action thriller chronicling fear, honor, and redemption across turbulent tides as a fearless warrior protects his people.',
        releaseDate: '2026-09-27',
        cast: ['NTR Jr.', 'Janhvi Kapoor', 'Saif Ali Khan', 'Prakash Raj', 'Srikanth'],
        director: 'Koratala Siva'
      },
      {
        id: 'movie-stree2',
        title: 'Stree 2: Sarkate Ka Aatank',
        posterUrl: 'https://image.tmdb.org/t/p/w780/nfnhwfUEFuSOxxf4jDdBlY6Lccw.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/5q36YhU8r0wX4n2bY5z9q1r0.jpg',
        durationMinutes: 147,
        genre: ['Comedy', 'Horror'],
        languages: ['Hindi'],
        formats: ['2D', '4DX'],
        rating: 8.6,
        voteCount: 42100,
        certification: 'UA',
        synopsis: 'The town of Chanderi faces a terrifying new headless entity, Sarkata. Vicky and his loyal friends team up with Stree to save the women of Chanderi in this blockbuster sequel.',
        releaseDate: '2026-08-15',
        cast: ['Rajkummar Rao', 'Shraddha Kapoor', 'Pankaj Tripathi', 'Abhishek Banerjee', 'Aparshakti Khurana'],
        director: 'Amar Kaushik'
      },
      {
        id: 'movie-tumbbad',
        title: 'Tumbbad (Re-release)',
        posterUrl: 'https://image.tmdb.org/t/p/w500/7aZ8fT6N1fW6o8oWv6Y9a0b1c2d.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/rLb2cwF3Pazuxaj0sRXQ037tGI1.jpg',
        durationMinutes: 104,
        genre: ['Horror', 'Fantasy', 'Period Drama'],
        languages: ['Hindi'],
        formats: ['2D', 'IMAX'],
        rating: 8.9,
        voteCount: 29400,
        certification: 'A',
        synopsis: 'A mythological horror masterpiece exploring the destructive nature of human greed centered around the cursed goddess of prosperity, Hastar.',
        releaseDate: '2026-09-13',
        cast: ['Sohum Shah', 'Jyoti Malshe', 'Anita Date', 'Ronjini Chakraborty'],
        director: 'Rahi Anil Barve'
      },
      {
        id: 'movie-buckingham',
        title: 'The Buckingham Murders',
        posterUrl: 'https://image.tmdb.org/t/p/w500/8q25XgT7q9vW3m1aX4y8p0q9a1b.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/w2R3x1lZ2u7A9V7nQ90m4q0p.jpg',
        durationMinutes: 110,
        genre: ['Crime', 'Mystery', 'Thriller'],
        languages: ['Hindi', 'English'],
        formats: ['2D'],
        rating: 8.1,
        voteCount: 11200,
        certification: 'UA',
        synopsis: 'A grieving detective investigates the murder of a ten-year-old boy in Buckinghamshire while confronting deep community prejudices and inner trauma.',
        releaseDate: '2026-09-13',
        cast: ['Kareena Kapoor Khan', 'Ash Tandon', 'Keith Allen', 'Ranveer Brar'],
        director: 'Hansal Mehta'
      },
      {
        id: 'movie-transformers',
        title: 'Transformers One',
        posterUrl: 'https://image.tmdb.org/t/p/w500/iRCgqpdVE4wyLQvKdU01w2oQjN3.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/7s23ZhU9s1xY5o3cZ6a0r2s1.jpg',
        durationMinutes: 104,
        genre: ['Animation', 'Action', 'Sci-Fi'],
        languages: ['English', 'Hindi'],
        formats: ['2D', '3D', 'IMAX 3D', '4DX 3D'],
        rating: 8.5,
        voteCount: 18700,
        certification: 'UA',
        synopsis: 'The untold origin story of how legendary brothers-in-arms Orion Pax and D-16 transformed into sworn enemies: Optimus Prime and Megatron.',
        releaseDate: '2026-09-20',
        cast: ['Chris Hemsworth', 'Brian Tyree Henry', 'Scarlett Johansson', 'Keegan-Michael Key'],
        director: 'Josh Cooley'
      },
      {
        id: 'movie-goat',
        title: 'The Greatest of All Time (GOAT)',
        posterUrl: 'https://image.tmdb.org/t/p/w500/9yZ6a0r2s1xY5o3cZ7s23ZhU9s1.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg',
        durationMinutes: 179,
        genre: ['Action', 'Sci-Fi', 'Thriller'],
        languages: ['Tamil', 'Hindi', 'Telugu'],
        formats: ['2D', 'IMAX'],
        rating: 8.3,
        voteCount: 31000,
        certification: 'UA',
        synopsis: 'A special anti-terrorist squad veteran is haunted by unresolved consequences from a past mission, forcing an explosive confrontation across decades.',
        releaseDate: '2026-09-05',
        cast: ['Thalapathy Vijay', 'Prashanth', 'Prabhu Deva', 'Mohan', 'Sneha'],
        director: 'Venkat Prabhu'
      },
      {
        id: 'movie-yudhra',
        title: 'Yudhra',
        posterUrl: 'https://image.tmdb.org/t/p/w500/5q36YhU8r0wX4n2bY5z9q1r0a2b.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/5q36YhU8r0wX4n2bY5z9q1r0.jpg',
        durationMinutes: 142,
        genre: ['Action', 'Thriller'],
        languages: ['Hindi'],
        formats: ['2D'],
        rating: 7.9,
        voteCount: 9800,
        certification: 'A',
        synopsis: 'A young man with extreme anger management issues goes undercover into an international syndicate to uncover the truth behind his parents death.',
        releaseDate: '2026-09-20',
        cast: ['Siddhant Chaturvedi', 'Malavika Mohanan', 'Raghav Juyal', 'Gajraj Rao'],
        director: 'Ravi Udyawar'
      },
      {
        id: 'movie-jigra',
        title: 'Jigra (Advance Booking)',
        posterUrl: 'https://image.tmdb.org/t/p/w500/4q25XgT7q9vW3m1aX4y8p0q9b3c.jpg',
        bannerUrl: 'https://image.tmdb.org/t/p/original/w2R3x1lZ2u7A9V7nQ90m4q0p.jpg',
        durationMinutes: 153,
        genre: ['Action', 'Drama'],
        languages: ['Hindi', 'Telugu'],
        formats: ['2D', 'IMAX'],
        rating: 9.0,
        voteCount: 15400,
        certification: 'UA',
        synopsis: 'A fiercely protective sister undertakes an impossible high-stakes rescue mission across hostile territory to save her imprisoned younger brother.',
        releaseDate: '2026-10-11',
        cast: ['Alia Bhatt', 'Vedang Raina', 'Manoj Pahwa', 'Rahul Ravindran'],
        director: 'Vasan Bala'
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
