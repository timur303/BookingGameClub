package kg.kadyrbekov.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kg.kadyrbekov.config.jwt.JwtTokenUtil;
import kg.kadyrbekov.dto.UpdateUserRequest;
import kg.kadyrbekov.dto.UserRequest;
import kg.kadyrbekov.dto.UserRequestCode;
import kg.kadyrbekov.dto.UserResponse;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.exceptions.UserRegistrationException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Image;
import kg.kadyrbekov.model.enums.AuthenticationProvider;
import kg.kadyrbekov.model.enums.Role;
import kg.kadyrbekov.repository.ImageRepository;
import kg.kadyrbekov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final ImageRepository imageRepository;

    @Lazy
    public void createNewUserAfterLoginSuccess(String email, String firstName, AuthenticationProvider provider) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(firstName);
        user.setAuthenticationProvider(provider);

        userRepository.save(user);
    }

    public UserResponse register(UserRequest userRequest) throws MessagingException {
        String email = userRequest.getEmail();

        Optional<User> existingUserByEmail = userRepository.findByEmail(email);
        if (existingUserByEmail.isPresent()) {
            throw new UserRegistrationException("User with this email already exists.");
        }

        Optional<User> existingUserByPhoneNumber = userRepository.findByPhoneNumber(userRequest.getPhoneNumber());
        if (existingUserByPhoneNumber.isPresent()) {
            throw new UserRegistrationException("User with this phone number already exists.");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(email);
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setPhoneNumber(userRequest.getPhoneNumber());


        String logoUrl = userRequest.getUrlAvatar();
        Image image = new Image();
        image.setUrl(logoUrl);
        image.setUser(user);



        if (email.equals("admin@gmail.com")) {
            if (isAdminAlreadyLoggedIn()) {
                throw new RuntimeException("Admin is already logged in");
            } else {
                user.setRole(Role.ADMIN);
                setAdminLoggedIn(true);
            }
        } else {
            user.setRole(Role.USER);
        }

        return mapToResponse(user);
    }


    public UserResponse registerWithCode(UserRequestCode userRequest) throws MessagingException {
        String email = userRequest.getEmail();

        Optional<User> existingUserByEmail = userRepository.findByEmail(email);
        if (existingUserByEmail.isPresent()) {
            throw new UserRegistrationException("User with this email already exists.");
        }

        Optional<User> existingUserByPhoneNumber = userRepository.findByPhoneNumber(userRequest.getPhoneNumber());
        if (existingUserByPhoneNumber.isPresent()) {
            throw new UserRegistrationException("User with this phone number already exists.");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(email);
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setPhoneNumber(userRequest.getPhoneNumber());


        String logoUrl = userRequest.getUrlAvatar();
        Image image = new Image();
        image.setUrl(logoUrl);
        image.setUser(user);
        imageRepository.save(image);


        if (email.equals("admin@gmail.com")) {
            if (isAdminAlreadyLoggedIn()) {
                throw new RuntimeException("Admin is already logged in");
            } else {
                user.setRole(Role.ADMIN);
                setAdminLoggedIn(true);
            }
        } else {
            user.setRole(Role.USER);
        }

        userRepository.save(user);

        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .token(jwtTokenUtil.generateToken(user))
                .role(user.getRole())
                .build();
    }


    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found!"));
    }


    @Transactional
    @JsonIgnore
    public UserResponse updateProfile(UpdateUserRequest updatedUserRequest) {
        User existingUser = getAuthenticatedUser();

        if (existingUser == null) {
            throw new RuntimeException("User not found.");
        }


        existingUser.setUsername(updatedUserRequest.getUsername());
        existingUser.setEmail(updatedUserRequest.getEmail());
        existingUser.setPhoneNumber(updatedUserRequest.getPhoneNumber());
        User updatedUser = userRepository.save(existingUser);

        return mapToResponse(updatedUser);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private static boolean adminLoggedIn = false;

    private synchronized boolean isAdminAlreadyLoggedIn() {
        return adminLoggedIn;
    }

    private synchronized void setAdminLoggedIn(boolean loggedIn) {
        adminLoggedIn = loggedIn;
    }


}