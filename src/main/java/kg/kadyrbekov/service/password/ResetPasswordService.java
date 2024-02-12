package kg.kadyrbekov.service.password;

import kg.kadyrbekov.config.jwt.JwtTokenUtil;
import kg.kadyrbekov.dto.UserDTO;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.mapper.Mail;
import kg.kadyrbekov.mapper.ValidationType;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.ResetPasswordToken;
import kg.kadyrbekov.repository.ResetPasswordRepository;
import kg.kadyrbekov.repository.UserRepository;
import kg.kadyrbekov.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final BCryptPasswordEncoder encoder;

    private final ResetPasswordRepository passwordRepository;

    private final EmailService emailService;

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    public String processForgotPassword(String email, HttpServletRequest request, String confirmationCode) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User with email not found"));
        if (user == null) {
            throw new UsernameNotFoundException("User with email" + email + "not found");
        }
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setUser(user);
        resetPasswordToken.setToken(jwtTokenUtil.generateToken(user));
        resetPasswordToken.setExpirationTime(LocalDateTime.now().plusMinutes(30));

        Mail mail = new Mail();
        mail.setFrom("pro.fri.suppor5@gmail.com");
        mail.setTo(user.getEmail());
        mail.setSubject("Password reset request");
        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("token", resetPasswordToken);
        mailModel.put("user", user);
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/jwt";
        mailModel.put("resetUrl", url + "/reset_password?token=" + resetPasswordToken.getToken());
//        System.out.println(url + " " + resetPasswordToken.getToken());
        String URL = url + "/reset_password?token=" + resetPasswordToken.getToken();
        mail.setModel(mailModel);
        passwordRepository.save(resetPasswordToken);
        emailService.sendEmail(mail, URL,confirmationCode);

        return ValidationType.SUCCESSFUL;
    }



    public UserDTO save(String token, String password) {
        ResetPasswordToken resetToken = passwordRepository.findByToken(token);
        User user = resetToken.getUser();
        user.setPassword(password);
        updatePassword(user);
        UserDTO authResponse = new UserDTO();
        authResponse.setId(user.getId());
        authResponse.setUsername(user.getUsername());
        authResponse.setEmail(user.getEmail());
        authResponse.setRole(user.getRole());
        return authResponse;
    }


    private void updatePassword(User user) {
        User user1 = userRepository.findById(user.getId()).get();
        user1.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user1);
    }
}
