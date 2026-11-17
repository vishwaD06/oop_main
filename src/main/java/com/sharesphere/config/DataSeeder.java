package com.sharesphere.config;

import com.sharesphere.entity.*;
import com.sharesphere.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestOfferRepository itemRequestOfferRepository;
    private final RideShareRepository rideShareRepository;
    private final RideBookingRepository rideBookingRepository;
    private final RentalRepository rentalRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) { log.info("Data already seeded, skipping."); return; }
        log.info("Seeding initial data...");

        // Categories
        List<Category> categories = List.of(
            cat("Electronics","💻","Phones, laptops, cameras, gadgets"),
            cat("Books","📚","Textbooks, novels, reference books"),
            cat("Calculators","🧮","Scientific and graphing calculators"),
            cat("Engineering Tools","📐","Drawing sets, instruments, tools"),
            cat("Lab Equipment","🔬","Microscopes, beakers, lab instruments"),
            cat("Sports Equipment","⚽","Balls, rackets, gym equipment"),
            cat("Furniture","🪑","Tables, chairs, shelves, lamps"),
            cat("Cycles","🚲","Bicycles and accessories"),
            cat("Bags","🎒","Backpacks, laptop bags, luggage"),
            cat("Musical Instruments","🎸","Guitars, keyboards, drums"),
            cat("Event Equipment","🎤","Projectors, mics, displays"),
            cat("Hostel Items","🏠","Kettles, buckets, fans, utensils"),
            cat("Study Materials","📝","Notes, question papers, assignments"),
            cat("Other","📦","Everything else")
        );
        categoryRepository.saveAll(categories);

        // Users
        User admin = userRepository.save(User.builder()
            .name("Admin").email("admin@sharesphere.com")
            .password(passwordEncoder.encode("Admin@123"))
            .role(Role.ADMIN).college("ShareSphere HQ").location("Campus").build());

        User rahul = userRepository.save(User.builder()
            .name("Rahul Sharma").email("rahul@college.edu")
            .password(passwordEncoder.encode("Student@123"))
            .phone("9876543210").college("IIT Campus").location("Hostel A").build());

        User priya = userRepository.save(User.builder()
            .name("Priya Verma").email("priya@college.edu")
            .password(passwordEncoder.encode("Student@123"))
            .phone("9123456789").college("IIT Campus").location("Hostel B").build());

        User arjun = userRepository.save(User.builder()
            .name("Arjun Mehta").email("arjun@college.edu")
            .password(passwordEncoder.encode("Student@123"))
            .college("IIT Campus").location("Block C").build());

        User sneha = userRepository.save(User.builder()
            .name("Sneha Patel").email("sneha@college.edu")
            .password(passwordEncoder.encode("Student@123"))
            .college("IIT Campus").location("Library Block").build());

        User dev = userRepository.save(User.builder()
            .name("Dev Kumar").email("dev@college.edu")
            .password(passwordEncoder.encode("Student@123"))
            .college("IIT Campus").location("Hostel C").build());

        // Re-fetch saved categories from DB to get real IDs
        List<Category> savedCats = categoryRepository.findAll();
        Category electronics = savedCats.stream().filter(c->c.getName().equals("Electronics")).findFirst().orElse(savedCats.get(0));
        Category books = savedCats.stream().filter(c->c.getName().equals("Books")).findFirst().orElse(savedCats.get(1));
        Category calculators = savedCats.stream().filter(c->c.getName().equals("Calculators")).findFirst().orElse(savedCats.get(2));
        Category engTools = savedCats.stream().filter(c->c.getName().equals("Engineering Tools")).findFirst().orElse(savedCats.get(3));
        Category sports = savedCats.stream().filter(c->c.getName().equals("Sports Equipment")).findFirst().orElse(savedCats.get(5));
        Category cycles = savedCats.stream().filter(c->c.getName().equals("Cycles")).findFirst().orElse(savedCats.get(7));
        Category hostel = savedCats.stream().filter(c->c.getName().equals("Hostel Items")).findFirst().orElse(savedCats.get(11));
        Category other = savedCats.stream().filter(c->c.getName().equals("Other")).findFirst().orElse(savedCats.get(13));
        Category furniture = savedCats.stream().filter(c->c.getName().equals("Furniture")).findFirst().orElse(savedCats.get(6));
        Category eventEquipment = savedCats.stream().filter(c->c.getName().equals("Event Equipment")).findFirst().orElse(savedCats.get(10));

        // Items
        List<Item> items = List.of(
            Item.builder().name("Casio FX-991CW Scientific Calculator")
                .description("Latest Casio FX-991CW, 552 functions, natural display. Perfect for semester exams.")
                .category(calculators).condition("Like New").price(new BigDecimal("900"))
                .rentalPricePerDay(new BigDecimal("30")).securityDeposit(new BigDecimal("300"))
                .listingType(ListingType.SELL_AND_RENT).owner(rahul).location("Hostel A").build(),

            Item.builder().name("DSLR Camera Canon 200D")
                .description("Canon 200D with 18-55mm kit lens, 24.1MP. Great for photography projects and events.")
                .category(electronics).condition("Good").price(new BigDecimal("28000"))
                .rentalPricePerDay(new BigDecimal("300")).securityDeposit(new BigDecimal("2000"))
                .listingType(ListingType.SELL_AND_RENT).owner(rahul).location("Hostel A").build(),

            Item.builder().name("Arduino Uno Starter Kit")
                .description("Arduino Uno R3 with 37-sensor kit, breadboard, jumper wires. Ideal for IoT projects.")
                .category(electronics).condition("New").price(new BigDecimal("1500"))
                .rentalPricePerDay(new BigDecimal("50")).securityDeposit(new BigDecimal("500"))
                .listingType(ListingType.SELL_AND_RENT).owner(priya).location("Lab Block").build(),

            Item.builder().name("Engineering Drawing Kit")
                .description("Staedtler 551 drawing set with compass, mini-drafter, set squares. First year essential.")
                .category(engTools).condition("Good").price(new BigDecimal("800"))
                .rentalPricePerDay(new BigDecimal("20")).securityDeposit(new BigDecimal("200"))
                .listingType(ListingType.SELL_AND_RENT).owner(arjun).location("Block C").build(),

            Item.builder().name("Mountain Bicycle")
                .description("Hero Sprint 21-speed MTB, well maintained. Perfect for campus commute.")
                .category(cycles).condition("Good").price(new BigDecimal("6000"))
                .rentalPricePerDay(new BigDecimal("100")).securityDeposit(new BigDecimal("1000"))
                .listingType(ListingType.SELL_AND_RENT).owner(arjun).location("Parking Lot B").build(),

            Item.builder().name("Data Structures & Algorithms - CLRS 4th Ed")
                .description("Introduction to Algorithms by CLRS. Excellent condition, no markings.")
                .category(books).condition("Like New").price(new BigDecimal("1200"))
                .listingType(ListingType.SELL).owner(sneha).location("Library Block").build(),

            Item.builder().name("Laptop Stand Adjustable")
                .description("Aluminum adjustable laptop stand, foldable. Reduces neck strain during long study sessions.")
                .category(other).condition("New")
                .price(new BigDecimal("1400")).listingType(ListingType.SELL)
                .owner(sneha).location("Library Block").build(),

            Item.builder().name("Football (Nivia)")
                .description("Nivia Storm Football, Size 5. Lightly used, great for hostel grounds.")
                .category(sports).condition("Good").price(new BigDecimal("600"))
                .rentalPricePerDay(new BigDecimal("20")).securityDeposit(new BigDecimal("100"))
                .listingType(ListingType.SELL_AND_RENT).owner(dev).location("Hostel C").build(),

            Item.builder().name("Hostel Electric Kettle 1L")
                .description("Prestige 1-litre stainless steel kettle. Perfect for hostel rooms.")
                .category(hostel).condition("Good").price(new BigDecimal("500"))
                .listingType(ListingType.SELL).owner(dev).location("Hostel C").build(),

            Item.builder().name("Raspberry Pi 4 Model B (4GB)")
                .description("Raspberry Pi 4B 4GB RAM, unused. Comes with SD card and case.")
                .category(electronics).condition("New").price(new BigDecimal("5500"))
                .rentalPricePerDay(new BigDecimal("150")).securityDeposit(new BigDecimal("1000"))
                .listingType(ListingType.SELL_AND_RENT).owner(priya).location("Lab Block").build(),

            Item.builder().name("Badminton Racket Set (2 pcs)")
                .description("Yonex Astrox pair with 3 shuttlecocks. Good for hostel court matches.")
                .category(sports).condition("Good").price(new BigDecimal("1400"))
                .rentalPricePerDay(new BigDecimal("40")).securityDeposit(new BigDecimal("200"))
                .listingType(ListingType.SELL_AND_RENT).owner(rahul).location("Sports Block").build(),

            Item.builder().name("Operating Systems - Galvin 10th Ed")
                .description("OS concepts by Silberschatz. Must-have for CS students. Minor pencil markings.")
                .category(books).condition("Fair").price(new BigDecimal("750"))
                .listingType(ListingType.SELL).owner(arjun).location("Block C").build(),

            Item.builder().name("Wireless Bluetooth Speaker")
                .description("JBL Go 3, waterproof, 5hr battery. For hostel room or outdoor trips.")
                .category(electronics).condition("Like New").price(new BigDecimal("2500"))
                .rentalPricePerDay(new BigDecimal("70")).securityDeposit(new BigDecimal("500"))
                .listingType(ListingType.SELL_AND_RENT).owner(sneha).location("Hostel B").build(),

            Item.builder().name("Study Table with Drawer")
                .description("Wooden study table, 120x60cm with 2 drawers. Pickup from Hostel B room 204.")
                .category(furniture)
                .condition("Good").price(new BigDecimal("2500"))
                .listingType(ListingType.SELL).owner(priya).location("Hostel B").build(),

            Item.builder().name("Crimping Tool + RJ45 Connectors Kit")
                .description("Network crimping tool with 100 RJ45 connectors. For networking lab projects.")
                .category(engTools).condition("New").price(new BigDecimal("450"))
                .rentalPricePerDay(new BigDecimal("15")).securityDeposit(new BigDecimal("100"))
                .listingType(ListingType.SELL_AND_RENT).owner(dev).location("Hostel C").build()
        );
        List<Item> savedItems = itemRepository.saveAll(items);
        Item calculator = savedItems.get(0);
        Item dslr = savedItems.get(1);
        Item bicycle = savedItems.get(4);
        Item clrsBook = savedItems.get(5);

        ItemRequest hdmiRequest = itemRequestRepository.save(ItemRequest.builder()
                .title("Need HDMI adapter for 2 hours today")
                .description("Need a HDMI to VGA/Type-C adapter for a seminar hall presentation today between 4-6 PM.")
                .category(eventEquipment)
                .requester(sneha)
                .location("Seminar Hall")
                .landmark("Student Activity Center (SAC) Plaza")
                .budget(new BigDecimal("100"))
                .neededTimeframe("Today 4-6 PM")
                .urgency(RequestUrgency.URGENT)
                .status(RequestStatus.OPEN)
                .build());
        itemRequestOfferRepository.save(ItemRequestOffer.builder()
                .request(hdmiRequest).offerer(priya)
                .message("I have an HDMI to Type-C adapter and can meet at SAC plaza.")
                .priceOffered(new BigDecimal("50")).build());

        ItemRequest racketRequest = itemRequestRepository.save(ItemRequest.builder()
                .title("Looking for badminton racket for weekend")
                .description("Need a racket for hostel court this Saturday morning. Can return Sunday evening.")
                .category(sports)
                .requester(arjun)
                .location("Sports Complex")
                .landmark("Sports Complex & Badminton Court")
                .budget(new BigDecimal("150"))
                .neededTimeframe("Saturday 8-11 AM")
                .urgency(RequestUrgency.MEDIUM)
                .status(RequestStatus.OPEN)
                .build());
        itemRequestOfferRepository.save(ItemRequestOffer.builder()
                .request(racketRequest).offerer(dev)
                .message("I have a spare racket and shuttlecocks; can meet at the sports complex.")
                .priceOffered(new BigDecimal("75")).build());

        ItemRequest calculatorRequest = itemRequestRepository.save(ItemRequest.builder()
                .title("Need scientific calculator for exam week")
                .description("Need a Casio FX-991 or equivalent for 3 days during exam week. Prefer hostel handover.")
                .category(calculators)
                .requester(rahul)
                .location("Hostel A")
                .landmark("Hostel A - Girls Wing Entrance")
                .budget(new BigDecimal("120"))
                .neededTimeframe("3 days next week")
                .urgency(RequestUrgency.MEDIUM)
                .status(RequestStatus.FULFILLED)
                .build());
        itemRequestOfferRepository.save(ItemRequestOffer.builder()
                .request(calculatorRequest).offerer(priya)
                .message("I can get you a Casio FX-991 from my batchmate for exam week.")
                .priceOffered(new BigDecimal("100")).build());
        itemRequestOfferRepository.save(ItemRequestOffer.builder()
                .request(calculatorRequest).offerer(arjun)
                .message("I have a scientific calculator available at Block C.")
                .priceOffered(new BigDecimal("120")).build());

        LocalDateTime stationDeparture = LocalDateTime.now().plusDays(3).withHour(18).withMinute(30).withSecond(0).withNano(0);
        RideShare stationRide = rideShareRepository.save(RideShare.builder()
                .organizer(rahul)
                .origin("Campus Main Gate (Gate 1)")
                .destination("Hyderabad Railway Station")
                .departureTime(stationDeparture)
                .totalSeats(4).availableSeats(2)
                .pricePerSeat(new BigDecimal("150"))
                .vehicleType("Cab (Uber/Ola)")
                .notes("Leave at sharp time. Luggage allowed up to 2 bags.")
                .status(RideStatus.OPEN)
                .build());
        rideBookingRepository.save(RideBooking.builder().ride(stationRide).passenger(priya).seatsBooked(1).build());
        rideBookingRepository.save(RideBooking.builder().ride(stationRide).passenger(arjun).seatsBooked(1).build());

        LocalDateTime airportDeparture = LocalDateTime.now().plusDays(4).withHour(5).withMinute(0).withSecond(0).withNano(0);
        RideShare airportRide = rideShareRepository.save(RideShare.builder()
                .organizer(priya)
                .origin("Hostel B - Boys Wing Entrance")
                .destination("RGIA Airport Terminal 1")
                .departureTime(airportDeparture)
                .totalSeats(2).availableSeats(0)
                .pricePerSeat(new BigDecimal("450"))
                .vehicleType("Personal Car")
                .notes("Early morning airport drop. Meet at hostel gate 10 mins early.")
                .status(RideStatus.FULL)
                .build());
        rideBookingRepository.save(RideBooking.builder().ride(airportRide).passenger(sneha).seatsBooked(1).build());
        rideBookingRepository.save(RideBooking.builder().ride(airportRide).passenger(dev).seatsBooked(1).build());

        LocalDateTime metroDeparture = LocalDateTime.now().plusDays(5).withHour(9).withMinute(15).withSecond(0).withNano(0);
        RideShare metroRide = rideShareRepository.save(RideShare.builder()
                .organizer(arjun)
                .origin("Campus Main Gate (Gate 1)")
                .destination("Secunderabad Metro Station")
                .departureTime(metroDeparture)
                .totalSeats(3).availableSeats(2)
                .pricePerSeat(new BigDecimal("80"))
                .vehicleType("Auto Rickshaw")
                .notes("Shared auto to metro. Ideal for weekend plans.")
                .status(RideStatus.OPEN)
                .build());
        rideBookingRepository.save(RideBooking.builder().ride(metroRide).passenger(rahul).seatsBooked(1).build());

        bicycle.setStatus(ItemStatus.RENTED);
        itemRepository.save(bicycle);
        Rental activeRental = Rental.builder()
                .item(bicycle).renter(priya).owner(arjun)
                .startDate(LocalDate.now().minusDays(1)).endDate(LocalDate.now().plusDays(3))
                .pricePerDay(bicycle.getRentalPricePerDay()).securityDeposit(bicycle.getSecurityDeposit())
                .status(RentalStatus.ACTIVE).escrowStatus(EscrowStatus.HELD)
                .renterNote("Need for weekend project")
                .build();
        activeRental.calculateTotals();
        rentalRepository.save(activeRental);

        Rental completedRental = Rental.builder()
                .item(dslr).renter(arjun).owner(rahul)
                .startDate(LocalDate.now().minusDays(10)).endDate(LocalDate.now().minusDays(7))
                .pricePerDay(dslr.getRentalPricePerDay()).securityDeposit(dslr.getSecurityDeposit())
                .status(RentalStatus.COMPLETED).escrowStatus(EscrowStatus.REFUNDED)
                .ownerNote("Returned in good condition")
                .build();
        completedRental.calculateTotals();
        rentalRepository.save(completedRental);

        Order pendingOrder = orderRepository.save(Order.builder()
                .item(clrsBook).buyer(dev).seller(sneha)
                .amount(clrsBook.getPrice()).paymentStatus(PaymentStatus.PENDING)
                .transactionRef("SS-SEED01").buyerNote("Can pickup from Library Block")
                .build());

        Review itemReview = reviewRepository.save(Review.builder()
                .reviewer(priya).reviewee(rahul).item(calculator)
                .rating(5).comment("Calculator was exactly as described.")
                .reviewType(ReviewType.ITEM).build());
        Review userReview = reviewRepository.save(Review.builder()
                .reviewer(rahul).reviewee(priya)
                .rating(4).comment("Smooth handover at SAC plaza.")
                .reviewType(ReviewType.USER).build());

        calculator.setAverageRating(4.8);
        calculator.setTotalRatings(2);
        itemRepository.save(calculator);
        rahul.setAverageRating(4.9); rahul.setTotalRatings(12);
        priya.setAverageRating(4.8); priya.setTotalRatings(9);
        arjun.setAverageRating(4.7); arjun.setTotalRatings(7);
        sneha.setAverageRating(5.0); sneha.setTotalRatings(8);
        dev.setAverageRating(4.6); dev.setTotalRatings(5);
        userRepository.saveAll(List.of(rahul, priya, arjun, sneha, dev));

        notificationRepository.saveAll(List.of(
                Notification.builder().user(arjun).title("New Rental Request")
                        .message("Priya Verma requested your Mountain Bicycle.")
                        .type(NotificationType.RENTAL_REQUEST).referenceId(activeRental.getId()).referenceType("RENTAL").build(),
                Notification.builder().user(sneha).title("Item Order Received")
                        .message("Dev Kumar wants to buy your Data Structures & Algorithms - CLRS 4th Ed.")
                        .type(NotificationType.ORDER_PLACED).referenceId(pendingOrder.getId()).referenceType("ORDER").build(),
                Notification.builder().user(priya).title("New Review")
                        .message("Rahul Sharma rated you 4/5.")
                        .type(NotificationType.NEW_REVIEW).referenceId(userReview.getId()).referenceType("REVIEW").build(),
                Notification.builder().user(rahul).title("Ride Booking Confirmed")
                        .message("Your seat on Arjun Mehta's ride to Secunderabad Metro is booked.")
                        .type(NotificationType.SYSTEM).referenceId(metroRide.getId()).referenceType("RIDE").build(),
                Notification.builder().user(dev).title("Welcome to ShareSphere")
                        .message("Complete your profile and list your first item.")
                        .type(NotificationType.SYSTEM).read(true).build()
        ));
        log.info("Seeded {} categories, {} users, {} items, plus requests/offers, rides/bookings, rentals, orders, reviews and notifications",
                categories.size(), 6, savedItems.size());
    }

    private Category cat(String name, String icon, String desc) {
        return Category.builder().name(name).icon(icon).description(desc).build();
    }
}
