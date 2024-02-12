package kg.kadyrbekov.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponsePlaystation {

    private Long bookingId;

    private Long playstationId;

    private BigDecimal price;

    private LocalDateTime bookingTimeStart;

    private LocalDateTime bookingTimeEnd;
}

