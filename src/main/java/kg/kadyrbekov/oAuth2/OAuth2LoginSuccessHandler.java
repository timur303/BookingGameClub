package kg.kadyrbekov.oAuth2;

import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.enums.AuthenticationProvider;
import kg.kadyrbekov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    @Autowired
    public OAuth2LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("firstName");


        Optional<User> userOptional = userService.findByEmail(email);

        if (!userOptional.isPresent()) {
            userService.createNewUserAfterLoginSuccess(email, firstName, AuthenticationProvider.GOOGLE);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
