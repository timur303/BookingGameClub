package kg.kadyrbekov.service;

import kg.kadyrbekov.dto.*;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.entity.Playstation;
import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.repository.ClubRepository;
import kg.kadyrbekov.repository.PlaystationRepository;
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
public class PlaystationService {

    private final UserRepository userRepository;

    private final ClubRepository clubRepository;

    private final PlaystationRepository playstationRepository;

    public Club findByIdClub(Long id) {
        return clubRepository.findById(id).orElseThrow(() -> new NotFoundException("Club with id not found"));
    }

    @Transactional
    public PlayStationResponse create(PlayStationRequest request) {
        User user = getAuthentication();
        Club club = findByIdClub(request.getClubId());
        Club userClub = user.getClub();
        if (userClub == null || !userClub.getId().equals(request.getClubId())) {
            throw new AccessDeniedException("You can only create playstation for your club.");
        }
        Playstation tennis = mapToEntity(request);
        tennis.setClub(club);
        tennis.setClubId(request.getClubId());
        tennis.setUser(user);
        tennis.setUserId(user.getId());
        club.setTennis(tennis.getClub().getTennis());
        playstationRepository.save(tennis);

        return mapToResponse(tennis);
    }


    @Transactional
    public PlayStationResponse update(Long id, PlayStationRequest request) {
        User user = getAuthentication();
        Playstation playstation = playstationRepository.findById(id).orElseThrow(() -> new NotFoundException("Playstation with " + id + " id not found"));
        playstation.setImage(request.getImage());
        playstation.setPricePerHour(request.getPricePerHour());
        playstation.setDescription(request.getDescription());
        playstation.setUser(user);
        playstation.setNumberOfPlaystation(request.getNumberOfPlaystation());
        playstation.setNumberOfPlaystationCabins(request.getNumberOfPlaystationCabins());
        playstation.setNumberOfVIPPlaystationCabins(request.getNumberOfVIPPlaystationCabins());
        playstation.setLocalDateTime(LocalDateTime.now());
        playstationRepository.save(playstation);

        return mapToResponse(playstation);
    }

    @Transactional
    public PlayStationResponse getByIdFootball(Long id) {
        Playstation playstation = playstationRepository.findById(id).orElseThrow(() -> new NotFoundException("Playstation with " + id + " id not found!"));
        PlayStationResponse response = new PlayStationResponse();
        playstation.setName(response.getName());
        playstation.setImage(response.getImage());
        playstation.setBookingStatus(response.getBookingStatus());
        playstation.setDescription(response.getDescription());
        playstation.setPricePerHour(response.getPricePerHour());
        playstation.setLocalDateTime(response.getLocalDateTime());
        playstation.setNumberOfPlaystation(response.getNumberOfPlaystation());
        playstation.setNumberOfPlaystationCabins(response.getNumberOfPlaystationCabins());
        playstation.setPricePerHourVIPCabin(response.getVipCabinPrice());

        return response;
    }

    @Transactional
    public PlayStationResponse deleteById(Long id) {
        Playstation playstation = playstationRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Playstation with " + id + " id not found!"));
        PlayStationResponse response = new PlayStationResponse();
        playstation.setName(response.getName());
        playstation.setImage(response.getImage());
        playstation.setBookingStatus(response.getBookingStatus());
        playstation.setDescription(response.getDescription());
        playstation.setPricePerHour(response.getPricePerHour());

        playstation.setLocalDateTime(response.getLocalDateTime());

        return response;

    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email not found"));
    }

    public Playstation mapToEntity(PlayStationRequest request) {
        Playstation playstation = new Playstation();
        playstation.setDescription(request.getDescription());
        playstation.setName(request.getName());
        playstation.setImage(request.getImage());
        playstation.setPricePerHour(request.getPricePerHour());
        playstation.setClubId(request.getClubId());
        playstation.setBookingStatus(BookingStatus.FREE);
        playstation.setPricePerHourVIPCabin(request.getPricePerHour());
        playstation.setNumberOfVIPPlaystationCabins(request.getNumberOfVIPPlaystationCabins());
        playstation.setNumberOfPlaystationCabins(request.getNumberOfPlaystationCabins());
        playstation.setNumberOfPlaystation(request.getNumberOfPlaystation());
        playstation.setLocalDateTime(LocalDateTime.now());
        playstation.setPricePerHourVIPCabin(request.getVipCabinPrice());

        return playstation;
    }

    public PlayStationResponse mapToResponse(Playstation playstation) {
        if (playstation == null) {
            return null;
        }
        PlayStationResponse response = new PlayStationResponse();
        response.setPricePerHour(playstation.getPricePerHour());
        response.setId(playstation.getId());
        response.setClubId(playstation.getClubId());
        response.setImage(playstation.getImage());
        response.setName(playstation.getName());
        response.setDescription(playstation.getDescription());
        response.setBookingStatus(BookingStatus.FREE);
        response.setVipCabinPrice(response.getVipCabinPrice());
        response.setNumberOfPlaystation(playstation.getNumberOfPlaystation());
        response.setNumberOfPlaystationCabins(playstation.getNumberOfPlaystationCabins());
        response.setNumberOfVIPPlaystationCabins(response.getNumberOfVIPPlaystationCabins());

        return response;
    }

}
