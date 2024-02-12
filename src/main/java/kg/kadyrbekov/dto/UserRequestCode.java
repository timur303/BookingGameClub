package kg.kadyrbekov.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestCode {

    private String username;

    private String email;

    private String phoneNumber;

    private String password;

    private String urlAvatar;

    private String code;
}
