package kg.kadyrbekov.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseBilliard {

    private Long bookingId;

    private Long billiardId;

    private BigDecimal price;

    private LocalDateTime bookingTimeStart;

    private LocalDateTime bookingTimeEnd;
}

