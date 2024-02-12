package kg.kadyrbekov.dto;

import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.model.enums.SizeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PlayStationRequest {

    private String name;

    private String image;

    private String description;

    private BigDecimal pricePerHour;

    private BigDecimal vipCabinPrice;

    private int numberOfPlaystation;

    private int numberOfPlaystationCabins;

    private int numberOfVIPPlaystationCabins;

    @Enumerated(EnumType.STRING)
    private SizeEnum sizeEnum;

    private Long clubId;
}
