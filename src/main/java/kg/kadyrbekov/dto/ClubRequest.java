package kg.kadyrbekov.dto;

import kg.kadyrbekov.model.enums.City;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class ClubRequest {

    private String clubName;

    private String description;

    private String address;

    private String linkInstagram;

    private String logoUrl;

    @Enumerated(EnumType.STRING)
    private City city;


}
