package kg.kadyrbekov.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseComputer {

    private Long bookingId;

    private Long computerId;

    private BigDecimal price;

    private LocalDateTime bookingTimeStart;

    private LocalDateTime bookingTimeEnd;
}
