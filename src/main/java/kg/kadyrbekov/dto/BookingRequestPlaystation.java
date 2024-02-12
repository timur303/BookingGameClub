package kg.kadyrbekov.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequestPlaystation {

    private Long playstationId;

    private LocalDateTime bookingTimeStart;

    private LocalDateTime bookingTimeEnd;


}
