package kg.kadyrbekov.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kg.kadyrbekov.config.jwt.JwtTokenUtil;
import kg.kadyrbekov.dto.ResetPasswordResponse;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.exceptions.IncorrectLoginException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.ResetPasswordToken;
import kg.kadyrbekov.repository.ResetPasswordRepository;
import kg.kadyrbekov.repository.UserRepository;
import kg.kadyrbekov.service.EmailService;
import kg.kadyrbekov.service.password.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/password")
public class PasswordController {


    private final EmailService emailService;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    private final ResetPasswordService resetPasswordService;

    private final MessageSource messageSource;

    private final ResetPasswordRepository resetPasswordRepository;



    @ApiOperation(value = "Code send code for forgot password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 404, message = "User not found", response = ApiError.class)
    })
    @PostMapping("/forgot_password")
    public ResponseEntity<?> processForgotPassword(@RequestParam("email") String email, HttpServletRequest request) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            ApiError response = new ApiError();
            response.setReason("User with email " + email + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String confirmationCode = generatePasswordConfirmationCode();
        emailService.sendForgotPasswordCode(email, confirmationCode);
        ResetPasswordResponse response = new ResetPasswordResponse();
        response.setConfirmationCode(confirmationCode);
        response.setMessages("Successfully");
        response.setToken(jwtTokenUtil.generateToken(user));
        resetPasswordService.processForgotPassword(email, request, confirmationCode);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @ApiOperation(value = "Code send code for forgot password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ApiError.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    @RequestMapping(value = "/reset_password", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> resetPassword(HttpServletRequest request, @RequestParam String token, @RequestParam String password) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }

        ResetPasswordToken resetToken = resetPasswordRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            String errorMessage = messageSource.getMessage("token.invalid", null, locale);
            ApiError response = new ApiError();
            response.setReason(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            resetPasswordService.save(token, password);
            String errorMessage = messageSource.getMessage("successfully.changed", null, locale);
            ApiError response = new ApiError();
            response.setReason(errorMessage);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IncorrectLoginException e) {
            String errorMessage = e.getMessage();
            ApiError response = new ApiError();
            response.setReason(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("reset.password.error", null, locale);
            ApiError response = new ApiError();
            response.setReason(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String generatePasswordConfirmationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
