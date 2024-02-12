package kg.kadyrbekov.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequestTennis {

    private Long tennisId;

    private LocalDateTime bookingTimeStart;

    private LocalDateTime bookingTimeEnd;


}
