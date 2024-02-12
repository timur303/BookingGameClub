package kg.kadyrbekov.service;

import kg.kadyrbekov.model.entity.*;
import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class AutomaticBookingStatusUpdateService {

    private final BookingRepository bookingRepository;
    private final GameZoneRepository gameZoneRepository;
    private final BilliardsRepository billiardsRepository;
    private final TennisRepository tennisRepository;
    private final PlaystationRepository playstationRepository;
    private final ComputerRepository computerRepository;

    @Autowired
    public AutomaticBookingStatusUpdateService(BookingRepository bookingRepository, GameZoneRepository gameZoneRepository, BilliardsRepository billiardsRepository, TennisRepository tennisRepository, PlaystationRepository playstationRepository, ComputerRepository computerRepository) {
        this.bookingRepository = bookingRepository;
        this.gameZoneRepository = gameZoneRepository;
        this.billiardsRepository = billiardsRepository;
        this.tennisRepository = tennisRepository;
        this.playstationRepository = playstationRepository;
        this.computerRepository = computerRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void updateBookingStatusGameZone() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings = bookingRepository.findByBookingTimeEndBeforeAndGameZone_BookingStatus(currentTime, BookingStatus.BUSY);

        for (Booking booking : bookings) {
            GameZone gameZone = booking.getGameZone();
            gameZone.setBookingStatus(BookingStatus.FREE);
            gameZoneRepository.save(gameZone);
            bookingRepository.save(booking);
        }
    }


    @Scheduled(fixedRate = 60000)
    public void updateBookingStatusTennis() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings = bookingRepository.findByBookingTimeEndBeforeAndTennis_BookingStatus(currentTime, BookingStatus.BUSY);

        for (Booking booking : bookings) {
            Tennis tennis = booking.getTennis();
            tennis.setBookingStatus(BookingStatus.FREE);
            tennisRepository.save(tennis);
            bookingRepository.save(booking);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void updateBookingStatusBilliard() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings = bookingRepository.findByBookingTimeEndBeforeAndBilliard_BookingStatus(currentTime, BookingStatus.BUSY);

        for (Booking booking : bookings) {
            Billiards billiards = booking.getBilliard();
            billiards.setBookingStatus(BookingStatus.FREE);
            billiardsRepository.save(billiards);
            bookingRepository.save(booking);
        }
    }


    @Scheduled(fixedRate = 60000)
    public void updateBookingStatusComputer() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings = bookingRepository.findByBookingTimeEndBeforeAndComputer_BookingStatus(currentTime, BookingStatus.BUSY);

        for (Booking booking : bookings) {
            Computer computer = booking.getComputer();
            computer.setBookingStatus(BookingStatus.FREE);
            computerRepository.save(computer);
            bookingRepository.save(booking);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void updateBookingStatusPlaystation() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings = bookingRepository.findByBookingTimeEndBeforeAndPlaystation_BookingStatus(currentTime, BookingStatus.BUSY);

        for (Booking booking : bookings) {
            Playstation playstation = booking.getPlaystation();
            playstation.setBookingStatus(BookingStatus.FREE);
            playstationRepository.save(playstation);
            bookingRepository.save(booking);
        }
    }
}
