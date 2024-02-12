package kg.kadyrbekov.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.*;
import kg.kadyrbekov.model.enums.City;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.CascadeType.REFRESH;


@ApiModel
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubResponse {


    private Long id;

    private String clubName;

    private String description;

    private String address;

    private String linkInstagram;

    private String logoUrl;

    private LocalDateTime localDateTime;

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
    private Long gameZone;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Transient
    private Long userId;

    private List<ReviewResponse> reviewResponse;

    private Long logoId;


}
