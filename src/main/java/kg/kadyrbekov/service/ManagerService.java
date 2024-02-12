package kg.kadyrbekov.service;

import kg.kadyrbekov.config.jwt.JwtTokenUtil;
import kg.kadyrbekov.dto.ManagerRequest;
import kg.kadyrbekov.dto.UserDTO;
import kg.kadyrbekov.dto.UserResponse;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.exceptions.UserRegistrationException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.enums.Role;
import kg.kadyrbekov.repository.ClubRepository;
import kg.kadyrbekov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository userRepository;

    private final ClubRepository clubRepository;

    private final JwtTokenUtil jwtTokenUtil;


    private final PasswordEncoder encoder;

    public UserResponse createManager(ManagerRequest managerRequest) {
        Club club = clubRepository.findById(managerRequest.getClubId())
                .orElseThrow(() -> new NotFoundException("Club not found"));

        String email = managerRequest.getEmail();

        Optional<User> existingUserByEmail = userRepository.findByEmail(email);
        if (existingUserByEmail.isPresent()) {
            throw new UserRegistrationException("User with this email already exists.");
        }

        Optional<User> existingUserByPhoneNumber = userRepository.findByPhoneNumber(managerRequest.getPhoneNumber());
        if (existingUserByPhoneNumber.isPresent()) {
            throw new UserRegistrationException("User with this phone number already exists.");
        }

        User user = new User();
        user.setUsername(managerRequest.getUsername());
        user.setEmail(managerRequest.getEmail());
        user.setPassword(encoder.encode(managerRequest.getPassword()));
        user.setPhoneNumber(managerRequest.getPhoneNumber());
        user.setClub(club);
        user.setRole(Role.MANAGER);
        userRepository.save(user);
        return mapToResponse(user);
    }


    private UserResponse mapToResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setRole(user.getRole());
        userResponse.setToken(jwtTokenUtil.generateToken(user));

        return userResponse;
    }

    public UserResponse updateManager(Long userId, ManagerRequest managerRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Manager not found"));
        Club club = clubRepository.findById(managerRequest.getClubId())
                .orElseThrow(() -> new NotFoundException("Club not found"));
        user.setUsername(managerRequest.getUsername());
        user.setEmail(managerRequest.getEmail());
        user.setPassword(managerRequest.getPassword());
        user.setClub(club);
        userRepository.save(user);
        return mapToResponse(user);
    }

    public void deleteManager(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Manager not found"));

        userRepository.delete(user);
    }
    public UserDTO getUserByID(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id not found"));
        UserDTO response = new UserDTO();
        response.setId(user.getId());
        response.setEmail(user.getUsername());
        if (user.getAvatar() != null) {
            response.setAvatarUrl(user.getAvatar().getUrl());
        } else {
            response.setAvatarUrl(null);
        }
        response.setUsername(user.getUserName1());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole());
        return response;
    }

}

