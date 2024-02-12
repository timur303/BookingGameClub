package kg.kadyrbekov.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kg.kadyrbekov.config.jwt.JwtTokenUtil;
import kg.kadyrbekov.dto.*;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.exceptions.UserRegistrationException;
import kg.kadyrbekov.mapper.LoginMapper;
import kg.kadyrbekov.mapper.LoginResponse;
import kg.kadyrbekov.mapper.ValidationType;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.repository.UserRepository;
import kg.kadyrbekov.service.EmailService;
import kg.kadyrbekov.service.UserService;
import kg.kadyrbekov.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt")
public class AuthController {
    private final LoginMapper loginMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final EmailService emailService;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final UserServiceImpl userServiceImpl;

    private final UserService userService;

    private final MessageSource messageSource;

    private String OTP_CODE;

    @GetMapping("/loginGoogle")
    @ApiOperation(value = "Get login page", notes = "This endpoint returns the login page.")
    public String loginPage() {
        return "login";
    }


    @ApiOperation(value = "User login")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = LoginResponse.class), @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class)})
    @RequestMapping(value = "/loginWithEmail", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> getLoginWithEmail(HttpServletRequest request, @RequestBody LoginWithEmailRequest loginWithEmailRequest) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }
        try {
            UserDetails userDetails = null;
            Authentication authentication = null;

            if (StringUtils.isNotBlank(loginWithEmailRequest.getEmail()) && StringUtils.isNotBlank(loginWithEmailRequest.getPassword())) {
                // using email
                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginWithEmailRequest.getEmail(), loginWithEmailRequest.getPassword()));
                userDetails = (UserDetails) authentication.getPrincipal();
                if (userDetails != null) {
                    if (!passwordEncoder.matches(loginWithEmailRequest.getPassword(), userDetails.getPassword())) {
                        throw new BadCredentialsException("Invalid password");
                    }
                } else {
                    throw new UsernameNotFoundException("User with email " + loginWithEmailRequest.getEmail() + " not found");
                }
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }

            if (userDetails != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtTokenUtil.generateToken(userDetails);
                User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

                LoginResponse loginResponse = loginMapper.loginView(token, ValidationType.SUCCESSFUL, user);
                loginResponse.setToken(token);
                loginResponse.setId(user != null ? user.getId() : null);

                return ResponseEntity.ok().body(loginResponse);
            } else {
                // Authentication failed
                ApiError apiError = new ApiError();
                String messages = messageSource.getMessage("login.invalid", null, locale);
                apiError.setReason(messages);
                return ResponseEntity.badRequest().body(apiError);
            }
        } catch (BadCredentialsException ex) {
            // Authentication failed
            ApiError apiError = new ApiError();
            String messages = messageSource.getMessage("login.invalid", null, locale);
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (UsernameNotFoundException ex) {
            // User not found
            ApiError apiError = new ApiError();
            String messages = messageSource.getMessage("login.invalid", null, locale);
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        }
    }

    @ApiOperation(value = "User login")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = LoginResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class)})
    @RequestMapping(value = "/loginWithPhoneNumber", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> getLoginWithPhoneNumber(HttpServletRequest request, @RequestBody LoginWithPhoneNumber loginWithEmailRequest) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }
        try {
            UserDetails userDetails = null;
            Authentication authentication = null;

            if (StringUtils.isNotBlank(loginWithEmailRequest.getPhoneNumber()) && StringUtils.isNotBlank(loginWithEmailRequest.getPassword())) {
                // using phoneNumber
                userDetails = userServiceImpl.loadUserByUsername(loginWithEmailRequest.getPhoneNumber());
                if (userDetails != null) {
                    if (!passwordEncoder.matches(loginWithEmailRequest.getPassword(), userDetails.getPassword())) {
                        throw new BadCredentialsException("Invalid password");
                    }
                } else {
                    throw new UsernameNotFoundException("User with phone number - " + loginWithEmailRequest.getPhoneNumber() + " not found");
                }
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }

            if (userDetails != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtTokenUtil.generateToken(userDetails);
                User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

                LoginResponse loginResponse = loginMapper.loginView(token, ValidationType.SUCCESSFUL, user);
                loginResponse.setToken(token);
                loginResponse.setId(user != null ? user.getId() : null);

                return ResponseEntity.ok().body(loginResponse);
            } else {
                // Authentication failed
                ApiError apiError = new ApiError();
                String messages = messageSource.getMessage("login.invalid", null, locale);
                apiError.setReason(messages);
                return ResponseEntity.badRequest().body(apiError);
            }
        } catch (BadCredentialsException ex) {
            // Authentication failed
            ApiError apiError = new ApiError();
            String messages = messageSource.getMessage("login.invalid", null, locale);
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (UsernameNotFoundException ex) {
            // User not found
            ApiError apiError = new ApiError();
            String messages = messageSource.getMessage("login.invalid", null, locale);
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        }
    }

    @ApiOperation(value = "Registration user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = UserResponse.class), @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ApiError.class)})
    @RequestMapping(value = "/checkUserAndSendEmailOTP", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> checkUserAndSendEmailOTP(HttpServletRequest request, @RequestBody UserRequest userRequest) throws MessagingException {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }

        try {
            if (StringUtils.isBlank(userRequest.getEmail()) || StringUtils.isBlank(userRequest.getPassword()) || StringUtils.isBlank(userRequest.getPhoneNumber())) {
                String errorMessage = messageSource.getMessage("registration.invalid", null, locale);
                ApiError response = new ApiError();
                response.setReason(errorMessage);
                return ResponseEntity.badRequest().body(response);
            }

            if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
                String errorMessage = messageSource.getMessage("email.exists", null, locale);
                ApiError response = new ApiError();
                response.setReason(errorMessage);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            if (userRepository.findByPhoneNumber(userRequest.getPhoneNumber()).isPresent()) {
                String errorMessage = messageSource.getMessage("phone.exists", null, locale);
                ApiError response = new ApiError();
                response.setReason(errorMessage);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            UserResponse response = userService.register(userRequest);
            String confirmationCode = getRandomNumberString();
            emailService.sendLoginConfirmationCode(userRequest.getEmail(), confirmationCode);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserRegistrationException e) {
            String errorMessage = messageSource.getMessage("registration.failed", null, locale);
            ApiError response = new ApiError();
            response.setReason(errorMessage);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @ApiOperation(value = "Registration user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = UserResponse.class), @ApiResponse(code = 401, message = "UNAUTHORIZED", response = ApiError.class)})
    @RequestMapping(value = "/registerUserWithCode", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> registerUserWithCode(HttpServletRequest request, @RequestBody UserRequestCode userRequest) throws MessagingException {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale = selectedLanguage != null ? new Locale(selectedLanguage) : new Locale("ru");
        try {
            if (StringUtils.isAnyBlank(userRequest.getEmail(), userRequest.getPassword(), userRequest.getPhoneNumber())) {
                return createBadRequestResponse("registration.invalid", locale);
            }
            if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
                return createConflictResponse("email.exists", locale);
            }
            if (userRepository.findByPhoneNumber(userRequest.getPhoneNumber()).isPresent()) {
                return createConflictResponse("phone.exists", locale);
            }
            if (!userRequest.getCode().equals(OTP_CODE)) {
                return createUnauthorizedResponse("confirmation.code.invalid", null);

            }
            UserResponse response = userService.registerWithCode(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserRegistrationException e) {
            return createUnauthorizedResponse("registration.failed", locale);
        }
    }

    private ResponseEntity<ApiError> createBadRequestResponse(String messageCode, Locale locale) {
        return createErrorResponse(messageCode, locale, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiError> createConflictResponse(String messageCode, Locale locale) {
        return createErrorResponse(messageCode, locale, HttpStatus.CONFLICT);
    }

    private ResponseEntity<ApiError> createUnauthorizedResponse(String messageCode, Locale locale) {
        return createErrorResponse(messageCode, locale, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ApiError> createErrorResponse(String messageCode, Locale locale, HttpStatus status) {
        String errorMessage = messageSource.getMessage(messageCode, null, locale);
        ApiError response = new ApiError();
        response.setReason(errorMessage);
        return ResponseEntity.status(status).body(response);
    }


    @ApiOperation(value = "Code send code for login")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = CodeResponse.class),
            @ApiResponse(code = 404, message = "Unauthorized", response = ApiError.class)})
    @PostMapping(value = "/send-code-login", produces = "application/json")
    public ResponseEntity<?> sendLoginConfirmationCode(@RequestParam String email) {
        String confirmationCode = getRandomNumberString();
        emailService.sendLoginConfirmationCode(email, confirmationCode);
        CodeResponse response = new CodeResponse();
        response.setCode(confirmationCode);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public String getRandomNumberString() {
        Random rnd = new Random();
        int number = 100000 + rnd.nextInt(900000);
        OTP_CODE = String.format("%06d", number);
        return OTP_CODE;
    }

}
