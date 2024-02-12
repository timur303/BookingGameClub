package kg.kadyrbekov.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginWithPhoneNumber {

    private String password;

    private String phoneNumber;
}
