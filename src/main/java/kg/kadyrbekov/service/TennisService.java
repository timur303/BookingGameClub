package kg.kadyrbekov.service;

import kg.kadyrbekov.dto.TennisRequest;
import kg.kadyrbekov.dto.TennisResponse;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.entity.Tennis;
import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.model.enums.GameTypeForHistory;
import kg.kadyrbekov.repository.ClubRepository;
import kg.kadyrbekov.repository.TennisRepository;
import kg.kadyrbekov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TennisService {

    private final UserRepository userRepository;

    private final ClubRepository clubRepository;

    private final TennisRepository tennisRepository;

    public Club findByIdClub(Long id) {
        return clubRepository.findById(id).orElseThrow(() -> new NotFoundException("Club with id not found"));
    }

    @Transactional
    public TennisResponse create(TennisRequest request) {
        User user = getAuthentication();
        Club club = findByIdClub(request.getClubId());
        Club userClub = user.getClub();
        if (userClub == null || !userClub.getId().equals(request.getClubId())) {
            throw new AccessDeniedException("You can only create tennis for your club.");
        }
        Tennis tennis = mapToEntity(request);
        tennis.setClub(club);
        tennis.setClubId(request.getClubId());
        tennis.setUser(user);
        tennis.setUserId(user.getId());
        club.setTennis(tennis.getClub().getTennis());
        tennisRepository.save(tennis);

        return mapToResponse(tennis);
    }


    @Transactional
    public TennisResponse update(Long id, TennisRequest request) {
        User user = getAuthentication();
        Tennis tennis = tennisRepository.findById(id).orElseThrow(() -> new NotFoundException("Tennis with " + id + " id not found"));
        tennis.setImage(request.getImage());
        tennis.setPricePerHour(request.getPricePerHour());
        tennis.setDescription(request.getDescription());
        tennis.setUser(user);
        tennis.setVipPriceCabin(request.getVipCabinPrice());
        tennis.setName(request.getName());
        tennisRepository.save(tennis);
        return mapToResponse(tennis);
    }

    @Transactional
    public TennisResponse getByIdFootball(Long id) {
        Tennis tennis = tennisRepository.findById(id).orElseThrow(() -> new NotFoundException("Tennis with " + id + " id not found!"));
        TennisResponse response = new TennisResponse();
        tennis.setName(response.getName());
        tennis.setImage(response.getImage());
        tennis.setBookingStatus(response.getBookingStatus());
        tennis.setDescription(response.getDescription());
        tennis.setPricePerHour(response.getPricePerHour());
        tennis.setLocalDateTime(response.getLocalDateTime());
        tennis.setVipPriceCabin(response.getVipCabinPrice());
        tennis.setNumberOfTennisTables(response.getNumberOfTennisTables());

        return response;
    }

    @Transactional
    public TennisResponse deleteById(Long id) {
        Tennis tennis = tennisRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Tennis with " + id + " id not found!"));
        TennisResponse response = new TennisResponse();
        tennis.setName(response.getName());
        tennis.setImage(response.getImage());
        tennis.setBookingStatus(response.getBookingStatus());
//        tennis.setHour(response.getHour());
        tennis.setDescription(response.getDescription());
//        tennis.setPrice(response.getPrice());

        tennis.setLocalDateTime(response.getLocalDateTime());

        return response;

    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email not found"));
    }

    public Tennis mapToEntity(TennisRequest request) {
        Tennis tennis = new Tennis();
        tennis.setDescription(request.getDescription());
        tennis.setName(request.getName());
        tennis.setImage(request.getImage());
        tennis.setPricePerHour(request.getPricePerHour());
        tennis.setClubId(request.getClubId());
        tennis.setBookingStatus(BookingStatus.FREE);
        tennis.setLocalDateTime(LocalDateTime.now());
        tennis.setGameTypeForHistory(GameTypeForHistory.TENNIS);
        tennis.setNumberOfTennisTables(request.getNumberOfTennisTables());
        tennis.setVipPriceCabin(request.getVipCabinPrice());
        return tennis;
    }

    public TennisResponse mapToResponse(Tennis tennis) {
        if (tennis == null) {
            return null;
        }
        TennisResponse response = new TennisResponse();
        response.setPricePerHour(tennis.getPricePerHour());
        response.setId(tennis.getId());
        response.setClubId(tennis.getClubId());
        response.setImage(tennis.getImage());
        response.setName(tennis.getName());
        response.setDescription(tennis.getDescription());
        response.setBookingStatus(BookingStatus.FREE);
        response.setLocalDateTime(tennis.getLocalDateTime());
        response.setNumberOfTennisTables(tennis.getNumberOfTennisTables());
        response.setVipCabinPrice(tennis.getVipPriceCabin());

        return response;
    }

}
