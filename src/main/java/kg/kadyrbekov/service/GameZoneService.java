package kg.kadyrbekov.service;

import kg.kadyrbekov.dto.GameZoneRequest;
import kg.kadyrbekov.dto.GameZoneResponse;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.entity.GameZone;
import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.repository.ClubRepository;
import kg.kadyrbekov.repository.GameZoneRepository;
import kg.kadyrbekov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GameZoneService {

    private final UserRepository userRepository;

    private final ClubRepository clubRepository;

    private final GameZoneRepository gameZOneRepository;

    public Club findByIdClub(Long id) {
        return clubRepository.findById(id).orElseThrow(() -> new NotFoundException("Club with id not found"));
    }

    @Transactional
    public GameZoneResponse create(GameZoneRequest request) {
        User user = getAuthentication();
        Club club = findByIdClub(request.getClubId());
        Club userClub = user.getClub();
        if (userClub == null || !userClub.getId().equals(request.getClubId())) {
            throw new AccessDeniedException("You can only create football fields for your club.");
        }

        GameZone gameZone = mapToEntity(request);
        gameZone.setClub(club);
        gameZone.setClubId(request.getClubId());
        gameZone.setUser(user);
        gameZone.setUserId(user.getId());
        club.setGameZones(gameZone.getClub().getGameZones());
        gameZOneRepository.save(gameZone);

        return mapToResponse(gameZone);
    }


    @Transactional
    public GameZoneResponse update(Long id, GameZoneRequest request) {
        User user = getAuthentication();
        GameZone gameZone = gameZOneRepository.findById(id).orElseThrow(() -> new NotFoundException("Football with " + id + " id not found"));
        gameZone.setImage(request.getImage());
        gameZone.setPricePerHour(request.getPricePerHour());
        gameZone.setSizeEnum(request.getSizeEnum());
        gameZone.setDescription(request.getDescription());
        gameZone.setUser(user);
        gameZone.setGameType(request.getGameType());
        gameZOneRepository.save(gameZone);
        return mapToResponse(gameZone);
    }

    @Transactional
    public GameZoneResponse updateStatus(Long id, BookingStatus newStatus) {
        GameZone gameZone = gameZOneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Football with " + id + " id not found"));

        if (newStatus == null) {
            throw new IllegalArgumentException("newStatus cannot be null");
        }

        if (gameZOneRepository == null || mapToResponse(gameZone) == null) {
            throw new IllegalStateException("Dependencies are not properly initialized");
        }

        gameZone.setBookingStatus(newStatus);
        gameZOneRepository.save(gameZone);

        return mapToResponse(gameZone);
    }


    public void saveFootball(GameZone gameZone) {
        gameZOneRepository.save(gameZone);
    }

    @Transactional
    public GameZone getByIdGamZone(Long id) {
        GameZone gameZone = gameZOneRepository.findById(id).orElseThrow(() -> new NotFoundException("GameZone with " + id + " id not found!"));
        GameZone response = new GameZone();
        gameZone.setName(response.getName());
        gameZone.setImage(response.getImage());
        gameZone.setBookingStatus(response.getBookingStatus());
        gameZone.setSizeEnum(response.getSizeEnum());
        gameZone.setDescription(response.getDescription());
        gameZone.setPricePerHour(response.getPricePerHour());
        gameZone.setGameType(response.getGameType());

        return response;
    }

    @Transactional
    public GameZoneResponse deleteById(Long id) {
        GameZone gameZone = gameZOneRepository.findById(id).orElseThrow(()
                -> new NotFoundException("GameZone with " + id + " id not found!"));
        GameZoneResponse response = new GameZoneResponse();
        gameZone.setName(response.getName());
        gameZone.setImage(response.getImage());
        gameZone.setBookingStatus(response.getBookingStatus());
        gameZone.setSizeEnum(response.getSizeEnum());
        gameZone.setDescription(response.getDescription());
        gameZone.setPricePerHour(response.getPricePerHour());
        gameZone.setGameType(response.getGameType());
        gameZone.setLocalDateTime(response.getLocalDateTime());

        return response;

    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email not found"));
    }

    public GameZone mapToEntity(GameZoneRequest request) {
        GameZone gameZone = new GameZone();
        gameZone.setDescription(request.getDescription());
        gameZone.setName(request.getName());
        gameZone.setImage(request.getImage());
        gameZone.setPricePerHour(request.getPricePerHour());
        gameZone.setClubId(request.getClubId());
        gameZone.setPricePerHour(request.getPricePerHour());
        gameZone.setSizeEnum(request.getSizeEnum());
        gameZone.setBookingStatus(BookingStatus.FREE);
        gameZone.setLocalDateTime(LocalDateTime.now());
        gameZone.setGameType(request.getGameType());
        return gameZone;
    }

    public GameZoneResponse mapToResponse(GameZone gameZone) {
        if (gameZone == null) {
            return null;
        }
        GameZoneResponse response = new GameZoneResponse();
        response.setPricePerHour(gameZone.getPricePerHour());
        response.setId(gameZone.getId());
        response.setClubId(gameZone.getClubId());
        response.setImage(gameZone.getImage());
        response.setName(gameZone.getName());
        response.setDescription(gameZone.getDescription());
        response.setBookingStatus(BookingStatus.FREE);
        response.setLocalDateTime(gameZone.getLocalDateTime());
        return response;
    }

}