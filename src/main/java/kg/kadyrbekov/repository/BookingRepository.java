package kg.kadyrbekov.repository;

import kg.kadyrbekov.model.entity.Booking;
import kg.kadyrbekov.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookingTimeEndBeforeAndTennis_BookingStatus(LocalDateTime endTime, BookingStatus status);

    List<Booking> findByBookingTimeEndBeforeAndBilliard_BookingStatus(LocalDateTime endTime, BookingStatus status);

    List<Booking> findByBookingTimeEndBeforeAndPlaystation_BookingStatus(LocalDateTime endTime, BookingStatus status);

    List<Booking> findByBookingTimeEndBeforeAndComputer_BookingStatus(LocalDateTime endTime, BookingStatus status);
    List<Booking> findByBookingTimeEndBeforeAndGameZone_BookingStatus(LocalDateTime endTime, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.tennis.bookingStatus = 'BUSY' AND b.bookingTimeEnd <= :currentTime")
    List<Booking> findActiveTennis(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b WHERE b.billiard.bookingStatus = 'BUSY' AND b.bookingTimeEnd <= :currentTime")
    List<Booking> findActiveBilliard(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b WHERE b.playstation.bookingStatus = 'BUSY' AND b.bookingTimeEnd <= :currentTime")
    List<Booking> findActivePlaystation(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b WHERE b.computer.bookingStatus = 'BUSY' AND b.bookingTimeEnd <= :currentTime")
    List<Booking> findActiveComputer(@Param("currentTime") LocalDateTime currentTime);
    @Query("SELECT b FROM Booking b WHERE b.gameZone.bookingStatus = 'BUSY' AND b.bookingTimeEnd <= :currentTime")
    List<Booking> findActiveGameZone(@Param("currentTime") LocalDateTime currentTime);

}

