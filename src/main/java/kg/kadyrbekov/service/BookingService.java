package kg.kadyrbekov.service;

import kg.kadyrbekov.dto.*;
import kg.kadyrbekov.exceptions.BookingConflictException;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.*;
import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private GameZoneRepository gameZoneRepository;


    private final UserRepository userRepository;

    private final TennisRepository tennisRepository;

    private final BilliardsRepository billiardsRepository;

    private final ComputerRepository computerRepository;

    private final PlaystationRepository playstationRepository;


    private final OrderHistoryRepository orderHistoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email not found"));
    }

    @Transactional
    public BookingResponse bookGameZone(BookingRequestFootball bookingRequestFootball) {
        GameZone gameZone = gameZoneRepository.findById(bookingRequestFootball.getFootballId())
                .orElseThrow(() -> new NotFoundException("GameZone not found with id " + bookingRequestFootball.getFootballId()));
        LocalDateTime from = bookingRequestFootball.getBookingTimeStart();
        LocalDateTime to = bookingRequestFootball.getBookingTimeEnd();
        if (from.isAfter(to)) {
            throw new NotFoundException("From time can't be after to time");
        }

        User user = getAuthentication();

        Booking booking = new Booking();
        booking.setBookingTimeStart(from);
        booking.setBookingTimeEnd(to);
        booking.setUser(user);
        booking.setGameZone(gameZone);

        gameZone.setBookingStatus(BookingStatus.BUSY);

        BigDecimal pricePerHour = gameZone.getPricePerHour();
        Duration duration = Duration.between(from, to);
        BigDecimal price = calculatePrice(duration, pricePerHour);

        booking.setPrice(price);

        gameZoneRepository.save(gameZone);

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBookingId(booking.getId());
        bookingResponse.setFootballId(gameZone.getId());
        bookingResponse.setBookingTimeStart(booking.getBookingTimeStart());
        bookingResponse.setBookingTimeEnd(booking.getBookingTimeEnd());
        bookingResponse.setPrice(price);

        completeBookingGameZone(booking);

        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setName(gameZone.getName());
        orderHistory.setO_J_Id(gameZone.getId());
        orderHistory.setBooking(booking);
        orderHistory.setDate_Of_Operation(LocalDateTime.now());
        orderHistory.setPrice(booking.getPrice());


        orderHistory.setUser(user);

        orderHistoryRepository.save(orderHistory);
        booking.addOrderHistory(orderHistory);
        bookingRepository.save(booking);

        return bookingResponse;
    }


    @Transactional
    public BookingResponseTennis bookTennis(BookingRequestTennis bookingRequestTennis) {
        Tennis tennis = tennisRepository.findById(bookingRequestTennis.getTennisId())
                .orElseThrow(() -> new NotFoundException("Tennis not found with id " + bookingRequestTennis.getTennisId()));

        if (tennis.getBookingStatus() == BookingStatus.BUSY) {
            throw new RuntimeException("Tennis field is already booked");
        }

        LocalDateTime from = bookingRequestTennis.getBookingTimeStart();
        LocalDateTime to = bookingRequestTennis.getBookingTimeEnd();
        if (from.isAfter(to)) {
            throw new NotFoundException("From time can't be after to time");
        }

        User user = getAuthentication();
        Booking booking = new Booking();
        booking.setBookingTimeStart(from);
        booking.setBookingTimeEnd(to);
        booking.setUser(user);
        booking.setTennis(tennis);

        tennis.setBookingStatus(BookingStatus.BUSY);

        BigDecimal pricePerHour = tennis.getPricePerHour();
        Duration duration = Duration.between(from, to);
        BigDecimal price = calculatePrice(duration, pricePerHour);

        booking.setPrice(price);

        BigDecimal vipPricePerHour = tennis.getVipPriceCabin();
        Duration durations = Duration.between(from, to);
        BigDecimal vipPrice = calculatePrice(durations, vipPricePerHour);

        booking.setPriceVip(vipPrice);


        tennisRepository.save(tennis);

        BookingResponseTennis bookingResponse = new BookingResponseTennis();
        bookingResponse.setBookingId(booking.getId());
        bookingResponse.setTennisId(tennis.getId());
        bookingResponse.setBookingTimeStart(booking.getBookingTimeStart());
        bookingResponse.setBookingTimeEnd(booking.getBookingTimeEnd());
        bookingResponse.setPrice(price);

        completeBookingTennis(booking);

        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setName(tennis.getName());
        orderHistory.setDate_Of_Operation(LocalDateTime.now());
        orderHistory.setVipPrice(orderHistory.getVipPrice());
        orderHistory.setPrice(orderHistory.getPrice());
        orderHistory.setGameTypeForHistory(orderHistory.getGameTypeForHistory());

        orderHistoryRepository.save(orderHistory);
        booking.addOrderHistory(orderHistory);
        bookingRepository.save(booking);

        return bookingResponse;
    }


    @Transactional
    public BookingResponseComputer bookComputer(BookingRequestComputer bookingRequestComputer) {
        Computer computer = computerRepository.findById(bookingRequestComputer.getComputerId())
                .orElseThrow(() -> new NotFoundException("Computer not found with id " + bookingRequestComputer.getComputerId()));

        if (computer.getBookingStatus() == BookingStatus.BUSY) {
            throw new RuntimeException("Computer field is already booked");
        }

        LocalDateTime from = bookingRequestComputer.getBookingTimeStart();
        LocalDateTime to = bookingRequestComputer.getBookingTimeEnd();
        if (from.isAfter(to)) {
            throw new NotFoundException("From time can't be after to time");
        }

        User user = getAuthentication();
        Booking booking = new Booking();
        booking.setBookingTimeStart(from);
        booking.setBookingTimeEnd(to);
        booking.setUser(user);
        booking.setComputer(computer);

        computer.setBookingStatus(BookingStatus.BUSY);

        BigDecimal pricePerHour = computer.getPricePerHour();
        Duration duration = Duration.between(from, to);
        BigDecimal price = calculatePrice(duration, pricePerHour);

        booking.setPrice(price);


        BigDecimal vipPricePerHour = computer.getVipCabinPrice();
        Duration durations = Duration.between(from, to);
        BigDecimal vipPrice = calculatePrice(durations, vipPricePerHour);

        booking.setPriceVip(vipPrice);


        computerRepository.save(computer);

        BookingResponseComputer bookingResponse = new BookingResponseComputer();
        bookingResponse.setBookingId(booking.getId());
        bookingResponse.setComputerId(computer.getId());
        bookingResponse.setBookingTimeStart(booking.getBookingTimeStart());
        bookingResponse.setBookingTimeEnd(booking.getBookingTimeEnd());
        bookingResponse.setPrice(price);

        completeBookingComputer(booking);

        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setName(computer.getName());
        orderHistory.setGameTypeForHistory(computer.getGameTypeForHistory());
        orderHistory.setDate_Of_Operation(LocalDateTime.now());
        orderHistory.setPrice(booking.getPrice());
        orderHistory.setO_J_Id(computer.getId());
        orderHistory.setUser(user.getAvatar().getUser());
        orderHistory.setVipPrice(booking.getPriceVip());

        orderHistoryRepository.save(orderHistory);
        booking.addOrderHistory(orderHistory);
        bookingRepository.save(booking);

        return bookingResponse;
    }

    @Transactional
    public BookingResponseBilliard bookBilliard(BookingRequestBilliard bookingRequestBilliard) {
        Billiards billiards = billiardsRepository.findById(bookingRequestBilliard.getBilliardId())
                .orElseThrow(() -> new NotFoundException("Billiard not found with id " + bookingRequestBilliard.getBilliardId()));

        if (billiards.getBookingStatus() == BookingStatus.BUSY) {
            throw new BookingConflictException("Billiard field is already booked");
        }

        LocalDateTime from = bookingRequestBilliard.getBookingTimeStart();
        LocalDateTime to = bookingRequestBilliard.getBookingTimeEnd();
        if (from.isAfter(to)) {
            throw new NotFoundException("From time can't be after to time");
        }

        User user = getAuthentication();
        Booking booking = new Booking();
        booking.setBookingTimeStart(from);
        booking.setBookingTimeEnd(to);
        booking.setUser(user);
        booking.setBilliard(billiards);

        billiards.getBookings().add(booking);

        billiards.setBookingStatus(BookingStatus.BUSY);

        BigDecimal pricePerHour = billiards.getPricePerHour();
        Duration duration = Duration.between(from, to);
        BigDecimal price = calculatePrice(duration, pricePerHour);

        booking.setPrice(price);

        BigDecimal vipPricePerHour = billiards.getVipCabinPrice();
        Duration durations = Duration.between(from, to);
        BigDecimal vipPrice = calculatePrice(durations, vipPricePerHour);

        booking.setPriceVip(vipPrice);


        billiardsRepository.save(billiards);

        BookingResponseBilliard bookingResponse = new BookingResponseBilliard();
        bookingResponse.setBookingId(booking.getId());
        bookingResponse.setBilliardId(billiards.getId());
        bookingResponse.setBookingTimeStart(booking.getBookingTimeStart());
        bookingResponse.setBookingTimeEnd(booking.getBookingTimeEnd());
        bookingResponse.setPrice(price);

        completeBookingBilliard(booking);

        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setO_J_Id(orderHistory.getO_J_Id());
        orderHistory.setName(billiards.getName());
        orderHistory.setPrice(billiards.getPricePerHour());
        orderHistory.setDate_Of_Operation(LocalDateTime.now());
        orderHistory.setGameTypeForHistory(billiards.getGameTypeForHistory());
        orderHistory.setVipPrice(billiards.getVipCabinPrice());

        orderHistoryRepository.save(orderHistory);
        booking.addOrderHistory(orderHistory);
        bookingRepository.save(booking);

        return bookingResponse;
    }


    @Transactional
    public BookingResponsePlaystation bookPlaystation(BookingRequestPlaystation bookingRequestPlaystation) {
        Playstation playstation = playstationRepository.findById(bookingRequestPlaystation.getPlaystationId())
                .orElseThrow(() -> new NotFoundException("Playstation not found with id " + bookingRequestPlaystation.getPlaystationId()));

        if (playstation.getBookingStatus() == BookingStatus.BUSY) {
            throw new RuntimeException("Playstation field is already booked");
        }

        LocalDateTime from = bookingRequestPlaystation.getBookingTimeStart();
        LocalDateTime to = bookingRequestPlaystation.getBookingTimeEnd();
        if (from.isAfter(to)) {
            throw new NotFoundException("From time can't be after to time");
        }

        User user = getAuthentication();
        Booking booking = new Booking();
        booking.setBookingTimeStart(from);
        booking.setBookingTimeEnd(to);
        booking.setUser(user);
        booking.setPlaystation(playstation);

        playstation.setBookingStatus(BookingStatus.BUSY);

        BigDecimal pricePerHour = playstation.getPricePerHour();
        Duration duration = Duration.between(from, to);
        BigDecimal price = calculatePrice(duration, pricePerHour);

        booking.setPrice(price);

        BigDecimal vipPricePerHour = playstation.getPricePerHourVIPCabin();
        Duration durations = Duration.between(from, to);
        BigDecimal vipPrice = calculatePrice(durations, vipPricePerHour);

        booking.setPriceVip(vipPrice);

        playstationRepository.save(playstation);

        BookingResponsePlaystation bookingResponse = new BookingResponsePlaystation();
        bookingResponse.setBookingId(booking.getId());
        bookingResponse.setPlaystationId(playstation.getId());
        bookingResponse.setBookingTimeStart(booking.getBookingTimeStart());
        bookingResponse.setBookingTimeEnd(booking.getBookingTimeEnd());
        bookingResponse.setPrice(price);

        completeBookingPlaystation(booking);

        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setName(playstation.getName());
        orderHistory.setVipPrice(playstation.getPricePerHourVIPCabin());
        orderHistory.setDate_Of_Operation(LocalDateTime.now());
        orderHistory.setGameTypeForHistory(playstation.getGameTypeForHistory());

        orderHistoryRepository.save(orderHistory);
        booking.addOrderHistory(orderHistory);
        bookingRepository.save(booking);

        return bookingResponse;
    }


    private void completeBookingGameZone(Booking booking) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));

        ZonedDateTime bookingEndTime = booking.getBookingTimeEnd().atZone(ZoneId.of("UTC"));

        if (currentTime.isAfter(bookingEndTime)) {
            booking.setCompleted(true);
            bookingRepository.save(booking);

            booking.getGameZone().setBookingStatus(BookingStatus.FREE);
            gameZoneRepository.save(booking.getGameZone());

            logger.info("Booking completed. BookingId: {}, GameZoneId: {}", booking.getId(), booking.getGameZone().getId());
        }
    }

    @Async
    public void scheduleCompleteBooking(Booking booking) {
        try {
            completeBookingGameZone(booking);
        } catch (Exception e) {
            logger.error("Error completing booking", e);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void checkAndCompleteBookings() {
        logger.info("Executing checkAndCompleteBookings...");

        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));
        logger.info("Current time: {}", currentTime);

        List<Booking> activeBookings = bookingRepository.findActiveGameZone(currentTime.toLocalDateTime());

        for (Booking booking : activeBookings) {
            scheduleCompleteBooking(booking);
        }
    }

    private void completeBookingTennis(Booking booking) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));

        ZonedDateTime bookingEndTime = booking.getBookingTimeEnd().atZone(ZoneId.of("UTC"));

        if (currentTime.isAfter(bookingEndTime)) {
            booking.setCompleted(true);
            bookingRepository.save(booking);

            booking.getPlaystation().setBookingStatus(BookingStatus.FREE);
            tennisRepository.save(booking.getTennis());

            logger.info("Booking completed. BookingId: {}, FootballId: {}", booking.getId(), booking.getTennis().getId());
        }
    }


    @Scheduled(fixedRate = 60000)
    public void checkAndCompleteBookingTennis() {
        logger.info("Executing checkAndCompleteBookings...");

        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));
        logger.info("Current time: {}", currentTime);

        List<Booking> activeBookings = bookingRepository.findActiveTennis(currentTime.toLocalDateTime());

        for (Booking booking : activeBookings) {
            scheduleCompleteBooking(booking);
        }
    }


    private void completeBookingBilliard(Booking booking) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));

        ZonedDateTime bookingEndTime = booking.getBookingTimeEnd().atZone(ZoneId.of("UTC"));

        if (currentTime.isAfter(bookingEndTime)) {
            booking.setCompleted(true);
            bookingRepository.save(booking);

            booking.getBilliard().setBookingStatus(BookingStatus.FREE);
            billiardsRepository.save(booking.getBilliard());

            logger.info("Booking completed. BookingId: {}, BilliardId: {}", booking.getId(), booking.getBilliard().getId());
        }
    }


    @Scheduled(fixedRate = 60000)
    public void checkAndCompleteBookingBilliard() {
        logger.info("Executing checkAndCompleteBookings...");

        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));
        logger.info("Current time: {}", currentTime);

        List<Booking> activeBookings = bookingRepository.findActiveBilliard(currentTime.toLocalDateTime());

        for (Booking booking : activeBookings) {
            scheduleCompleteBooking(booking);
        }
    }

    private void completeBookingPlaystation(Booking booking) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));

        ZonedDateTime bookingEndTime = booking.getBookingTimeEnd().atZone(ZoneId.of("UTC"));

        if (currentTime.isAfter(bookingEndTime)) {
            booking.setCompleted(true);
            bookingRepository.save(booking);

            booking.getPlaystation().setBookingStatus(BookingStatus.FREE);
            playstationRepository.save(booking.getPlaystation());

            logger.info("Booking completed. BookingId: {}, PlaystationId: {}", booking.getId(), booking.getPlaystation().getId());
        }
    }


    @Scheduled(fixedRate = 60000)
    public void checkAndCompleteBookingPlaystation() {
        logger.info("Executing checkAndCompleteBookings...");

        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));
        logger.info("Current time: {}", currentTime);

        List<Booking> activeBookings = bookingRepository.findActivePlaystation(currentTime.toLocalDateTime());

        for (Booking booking : activeBookings) {
            scheduleCompleteBooking(booking);
        }
    }

    private void completeBookingComputer(Booking booking) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));

        ZonedDateTime bookingEndTime = booking.getBookingTimeEnd().atZone(ZoneId.of("UTC"));

        if (currentTime.isAfter(bookingEndTime)) {
            booking.setCompleted(true);
            bookingRepository.save(booking);

            booking.getComputer().setBookingStatus(BookingStatus.FREE);
            computerRepository.save(booking.getComputer());

            logger.info("Booking completed. BookingId: {}, FootballId: {}", booking.getId(), booking.getComputer().getId());
        }
    }


    @Scheduled(fixedRate = 60000)
    public void checkAndCompleteBookingComputer() {
        logger.info("Executing checkAndCompleteBookings...");

        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));
        logger.info("Current time: {}", currentTime);

        List<Booking> activeBookings = bookingRepository.findActiveComputer(currentTime.toLocalDateTime());

        for (Booking booking : activeBookings) {
            scheduleCompleteBooking(booking);
        }
    }


    private BigDecimal calculatePrice(Duration duration, BigDecimal pricePerHour) {
        long minutes = duration.toMinutes();
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        return pricePerHour.multiply(hours);
    }
}
