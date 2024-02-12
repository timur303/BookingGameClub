package kg.kadyrbekov.dto;

import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.model.enums.SizeEnum;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayStationResponse {

    private Long id;

    private String name;

    private String image;

    private String description;

    private BigDecimal pricePerHour;

    private BigDecimal vipCabinPrice;

    private LocalDateTime localDateTime;

    private int numberOfPlaystation;

    private int numberOfPlaystationCabins;

    private int numberOfVIPPlaystationCabins;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    private SizeEnum sizeEnum;

    @Transient
    private Long clubId;


}
