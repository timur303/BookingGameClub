package kg.kadyrbekov.model.entity;

import kg.kadyrbekov.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime bookingTimeStart;

    private LocalDateTime bookingTimeEnd;

    private BigDecimal price;

    private BigDecimal priceVip;

    @Column(name = "completed")
    private boolean completed;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "gameZone_id")
    private GameZone gameZone;

    @ManyToOne
    @JoinColumn(name = "playstation_id")
    private Playstation playstation;

    @ManyToOne
    @JoinColumn(name = "billiard_id")
    private Billiards billiard;

    @ManyToOne
    @JoinColumn(name = "computer_id")
    private Computer computer;

    @ManyToOne
    @JoinColumn(name = "tennis_id")
    private Tennis tennis;

    @OneToMany(mappedBy = "booking")
    private List<OrderHistory> orderHistoryList;

    public void addOrderHistory(OrderHistory orderHistory) {
        if (orderHistoryList == null) {
            orderHistoryList = new ArrayList<>();
        }
        orderHistoryList.add(orderHistory);
    }

}