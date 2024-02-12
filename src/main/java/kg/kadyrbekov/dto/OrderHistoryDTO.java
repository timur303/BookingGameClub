package kg.kadyrbekov.dto;

import kg.kadyrbekov.model.enums.GameTypeForHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryDTO {

    private Long id;

    private String name;

    private BigDecimal price;

    private BigDecimal vipPrice;

    private LocalDateTime localDateTime;

    private Long o_j_id;

    @Enumerated(EnumType.STRING)
    private GameTypeForHistory gameTypeForHistory;

}