package kg.kadyrbekov.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequestFootball {

    private Long footballId;

    private LocalDateTime bookingTimeStart;

    private LocalDateTime bookingTimeEnd;


}
