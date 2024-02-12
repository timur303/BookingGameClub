package kg.kadyrbekov.dto;

import kg.kadyrbekov.model.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Getter
@Setter
public class ComputerRequest {

    private String name;

    private String image;

    private String description;

    private BigDecimal pricePerHour;

    private BigDecimal vipCabinPrice;

    private int numberOfTennisTables;

    private String logo;

    private Long clubId;

}

