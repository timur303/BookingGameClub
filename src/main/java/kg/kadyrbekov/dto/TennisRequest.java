package kg.kadyrbekov.dto;

import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.model.enums.SizeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Getter
@Setter
public class TennisRequest {

    private String name;

    private String image;

    private String description;

    private BigDecimal pricePerHour;

    private BigDecimal vipCabinPrice;

    private int numberOfTennisTables;

    private Long clubId;
}
