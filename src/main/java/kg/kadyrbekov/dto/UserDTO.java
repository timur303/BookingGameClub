package kg.kadyrbekov.dto;

import kg.kadyrbekov.model.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private String phoneNumber;

    private String avatarUrl;

    private Role role;
}
