package kg.kadyrbekov.dto;

import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Club;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class PostRequest {

    private String description;

    private LocalDateTime localDateTime;

    private String imageUrl;

//    private ClubResponse clubResponse;

    private Long clubId;

    private Long UserId;

//    private User user;

//    private Club club;

}
