package kg.kadyrbekov.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequestBilliard {

    private Long billiardId;

    private LocalDateTime bookingTimeStart;

    private LocalDateTime bookingTimeEnd;


}
