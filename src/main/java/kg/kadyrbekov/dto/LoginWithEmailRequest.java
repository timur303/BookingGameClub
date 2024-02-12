package kg.kadyrbekov.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginWithEmailRequest {

    private String email;

    private String password;

}
