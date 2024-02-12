package kg.kadyrbekov.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Booking;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.model.enums.SizeEnum;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.*;
import static javax.persistence.CascadeType.REFRESH;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BilliardsResponse{

    private Long id;

    private String name;

    private String image;

    private String description;

    private BigDecimal pricePerHour;

    private BigDecimal vipCabinPrice;

    private int numberOfBilliardsTables;

    private LocalDateTime localDateTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Transient
    private Long clubId;


}
