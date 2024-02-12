package kg.kadyrbekov.dto;


import kg.kadyrbekov.model.enums.StarsRating;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class ReviewRequest {

    @Enumerated(EnumType.STRING)
    private StarsRating starsRating;

    private String comments;

}
