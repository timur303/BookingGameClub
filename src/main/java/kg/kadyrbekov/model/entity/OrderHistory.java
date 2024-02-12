package kg.kadyrbekov.model.entity;

import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.enums.GameType;
import kg.kadyrbekov.model.enums.GameTypeForHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_history")
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long o_J_Id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private LocalDateTime date_Of_Operation;

    private BigDecimal price;

    private BigDecimal vipPrice;

    @Enumerated(EnumType.STRING)
    private GameTypeForHistory gameTypeForHistory;

}
