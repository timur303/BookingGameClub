package kg.kadyrbekov.dto;


import kg.kadyrbekov.model.entity.Club;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {

    private Long id;

    private String description;

    private LocalDateTime localDateTime;

    private String imageUrl;

    private Club club;

    private Long clubId;

}
