package kg.kadyrbekov.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequestComputer {

    private Long computerId;

    private LocalDateTime bookingTimeStart;

    private LocalDateTime bookingTimeEnd;

}
