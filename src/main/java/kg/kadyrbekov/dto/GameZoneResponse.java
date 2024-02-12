package kg.kadyrbekov.dto;

import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.model.enums.GameType;
import kg.kadyrbekov.model.enums.SizeEnum;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameZoneResponse {

    private Long id;

    private String name;

    private String image;

    private String description;

    private BigDecimal pricePerHour;

    private LocalDateTime localDateTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    private SizeEnum sizeEnum;

    @Transient
    private Long clubId;

    @Enumerated(EnumType.STRING)
    private GameType gameType;

}
