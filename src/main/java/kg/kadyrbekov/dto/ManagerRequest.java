package kg.kadyrbekov.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerRequest {

    private String username;

    private String email;

    private String password;

    private String phoneNumber;

    private Long clubId;

}
