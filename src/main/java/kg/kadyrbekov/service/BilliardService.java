package kg.kadyrbekov.service;

import kg.kadyrbekov.dto.BilliardsRequest;
import kg.kadyrbekov.dto.BilliardsResponse;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Billiards;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.model.enums.GameTypeForHistory;
import kg.kadyrbekov.repository.BilliardsRepository;
import kg.kadyrbekov.repository.ClubRepository;
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
public class BilliardService {

    private final UserRepository userRepository;

    private final ClubRepository clubRepository;

    private final BilliardsRepository billiardsRepository;

    public Club findByIdClub(Long id) {
        return clubRepository.findById(id).orElseThrow(() -> new NotFoundException("Club with id not found"));
    }

    @Transactional
    public BilliardsResponse create(BilliardsRequest request) {
        User user = getAuthentication();
        Club club = findByIdClub(request.getClubId());
        Club userClub = user.getClub();
        if (userClub == null || !userClub.getId().equals(request.getClubId())) {
            throw new AccessDeniedException("You can only create billiard for your club.");
        }
        Billiards billiards = mapToEntity(request);
        billiards.setClub(club);
        billiards.setClubId(request.getClubId());
        billiards.setUser(user);
        billiards.setUserId(user.getId());
        club.setTennis(billiards.getClub().getTennis());
        billiardsRepository.save(billiards);

        return mapToResponse(billiards);
    }


    @Transactional
    public BilliardsResponse update(Long id, BilliardsRequest request) {
        User user = getAuthentication();
        Billiards billiards = billiardsRepository.findById(id).orElseThrow(() -> new NotFoundException("Billiard with " + id + " id not found"));
        billiards.setImage(request.getImage());
        billiards.setPricePerHour(request.getPricePerHour());
        billiards.setDescription(request.getDescription());
        billiards.setUser(user);
        billiards.setLocalDateTime(LocalDateTime.now());
        billiards.setVipCabinPrice(request.getVipCabinPrice());
        billiards.setNumberOfBilliardsTables(request.getNumberOfBilliardsTables());

        billiardsRepository.save(billiards);
        return mapToResponse(billiards);
    }

    @Transactional
    public BilliardsResponse getByIdBilliard(Long id) {
        Billiards billiards = billiardsRepository.findById(id).orElseThrow(() -> new NotFoundException("Billiard with " + id + " id not found!"));
        BilliardsResponse response = new BilliardsResponse();
        billiards.setName(response.getName());
        billiards.setImage(response.getImage());
        billiards.setBookingStatus(response.getBookingStatus());
        billiards.setDescription(response.getDescription());
        billiards.setPricePerHour(response.getPricePerHour());
        billiards.setLocalDateTime(response.getLocalDateTime());
        billiards.setNumberOfBilliardsTables(response.getNumberOfBilliardsTables());
        billiards.setVipCabinPrice(response.getVipCabinPrice());

        return response;
    }

    @Transactional
    public BilliardsResponse deleteById(Long id) {
        Billiards billiards = billiardsRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Billiard with " + id + " id not found!"));
        BilliardsResponse response = new BilliardsResponse();
        billiards.setName(response.getName());
        billiards.setImage(response.getImage());
        billiards.setBookingStatus(response.getBookingStatus());
        billiards.setPricePerHour(response.getPricePerHour());
        billiards.setDescription(response.getDescription());

        billiards.setLocalDateTime(response.getLocalDateTime());

        return response;

    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email not found"));
    }

    public Billiards mapToEntity(BilliardsRequest request) {
        Billiards billiards = new Billiards();
        billiards.setDescription(request.getDescription());
        billiards.setName(request.getName());
        billiards.setImage(request.getImage());
        billiards.setPricePerHour(request.getPricePerHour());
        billiards.setClubId(request.getClubId());
        billiards.setBookingStatus(BookingStatus.FREE);
        billiards.setLocalDateTime(LocalDateTime.now());
        billiards.setNumberOfBilliardsTables(request.getNumberOfBilliardsTables());
        billiards.setVipCabinPrice(request.getVipCabinPrice());
        billiards.setGameTypeForHistory(GameTypeForHistory.BILLIARD);

        return billiards;
    }

    public BilliardsResponse mapToResponse(Billiards billiards) {
        if (billiards == null) {
            return null;
        }
        BilliardsResponse response = new BilliardsResponse();
        response.setPricePerHour(billiards.getPricePerHour());
        response.setId(billiards.getId());
        response.setClubId(billiards.getClubId());
        response.setImage(billiards.getImage());
        response.setName(billiards.getName());
        response.setDescription(billiards.getDescription());
        response.setBookingStatus(BookingStatus.FREE);
        response.setLocalDateTime(billiards.getLocalDateTime());
        response.setNumberOfBilliardsTables(billiards.getNumberOfBilliardsTables());
        return response;
    }
}