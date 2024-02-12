package kg.kadyrbekov.dto;

import kg.kadyrbekov.model.enums.GameType;
import kg.kadyrbekov.model.enums.SizeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Getter
@Setter
public class GameZoneRequest {

    private String name;

    private String image;

    private String description;

    private BigDecimal pricePerHour;

    @Enumerated(EnumType.STRING)
    private SizeEnum sizeEnum;

    @Enumerated(EnumType.STRING)
    private GameType gameType;

    private Long clubId;
}
