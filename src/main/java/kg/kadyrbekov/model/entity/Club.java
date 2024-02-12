package kg.kadyrbekov.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.enums.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clubs")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clubName;

    private String description;

    private String address;

    private String linkInstagram;

    private String logoUrl;

    private LocalDateTime localDateTime;

    @OneToMany(cascade = {DETACH, PERSIST, REFRESH}, mappedBy = "club")
    private List<Post> posts;

    @OneToOne(cascade = {DETACH, REFRESH, REMOVE, MERGE}, mappedBy = "club")
    private Image logo;

    @Enumerated(EnumType.STRING)
    private City city;

    @OneToMany(fetch = FetchType.LAZY, cascade = {DETACH, PERSIST, MERGE, REFRESH})
    @JsonIgnore
    private List<Playstation> playStations;
    @Transient
    private Long cabinId;

    @OneToMany(fetch = FetchType.LAZY, cascade = {DETACH, PERSIST, MERGE, REFRESH})
    @JsonIgnore
    private List<GameZone> gameZones;
    @Transient
    private Long GameZoneId;

    @OneToMany(fetch = FetchType.LAZY, cascade = {DETACH, PERSIST, MERGE, REFRESH})
    @JsonIgnore
    private List<Tennis> tennis;
    @Transient
    private Long tennisId;

    @OneToMany(fetch = FetchType.LAZY, cascade = {DETACH, PERSIST, MERGE, REFRESH})
    @JsonIgnore
    private List<Computer> computers;
    @Transient
    private Long computerId;

    @OneToMany(fetch = FetchType.LAZY, cascade = {DETACH, PERSIST, MERGE, REFRESH})
    @JsonIgnore
    private List<Billiards> billiards;
    @Transient
    private Long billiardId;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private User manager;
    @Transient
    private Long userId;

    @OneToMany(mappedBy = "club")
    private List<Review> reviews;

}
