package kg.kadyrbekov.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestVolleyball {

    private Long volleyballId;

    private LocalDateTime bookingTimeStart;

    private LocalDateTime bookingTimeEnd;


}
