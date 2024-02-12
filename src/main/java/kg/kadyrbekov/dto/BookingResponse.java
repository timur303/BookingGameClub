package kg.kadyrbekov.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private Long bookingId;

    private Long footballId;

    private BigDecimal price;

    private LocalDateTime bookingTimeStart;

    private LocalDateTime bookingTimeEnd;
}
