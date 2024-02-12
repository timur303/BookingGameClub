package kg.kadyrbekov.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {


    private String username;

    private String email;

    private String phoneNumber;

    private String avatarUrl;

}
