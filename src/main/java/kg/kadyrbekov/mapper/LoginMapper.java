package kg.kadyrbekov.mapper;

import kg.kadyrbekov.model.User;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    public LoginResponse loginView(String token, String message, User user) {
        var loginResponse = new LoginResponse();
        if (user != null) {
            try {
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
            loginResponse.setToken(token);
            loginResponse.setId(user.getId());
            loginResponse.setRole(user.getRole());
        }
        return loginResponse;
    }


}
