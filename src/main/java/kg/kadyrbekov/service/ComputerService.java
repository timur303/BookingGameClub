package kg.kadyrbekov.service;

import kg.kadyrbekov.dto.ComputerRequest;
import kg.kadyrbekov.dto.ComputerResponse;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.entity.Computer;
import kg.kadyrbekov.model.enums.BookingStatus;
import kg.kadyrbekov.model.enums.GameTypeForHistory;
import kg.kadyrbekov.repository.ClubRepository;
import kg.kadyrbekov.repository.ComputerRepository;
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
public class ComputerService {

    private final UserRepository userRepository;

    private final ClubRepository clubRepository;

    private final ComputerRepository computerRepository;

    public Club findByIdClub(Long id) {
        return clubRepository.findById(id).orElseThrow(() -> new NotFoundException("Club with id not found"));
    }

    @Transactional
    public ComputerResponse create(ComputerRequest request) {
        User user = getAuthentication();
        Club club = findByIdClub(request.getClubId());
        Club userClub = user.getClub();
        if (userClub == null || !userClub.getId().equals(request.getClubId())) {
            throw new AccessDeniedException("You can only create computer for your club.");
        }
        Computer computer = mapToEntity(request);
        computer.setClub(club);
        computer.setClubId(request.getClubId());
        computer.setUser(user);
        computer.setUserId(user.getId());
        club.setComputers(computer.getClub().getComputers());

        computerRepository.save(computer);

        return mapToResponse(computer);
    }


    @Transactional
    public ComputerResponse update(Long id, ComputerRequest request) {
        User user = getAuthentication();
        Computer computer = computerRepository.findById(id).orElseThrow(() -> new NotFoundException("Computer with " + id + " id not found"));
        computer.setImage(request.getImage());
        computer.setPricePerHour(request.getPricePerHour());
        computer.setDescription(request.getDescription());
        computer.setUser(user);
        computer.setVipCabinPrice(request.getVipCabinPrice());
        computer.setLocalDateTime(LocalDateTime.now());

        computerRepository.save(computer);
        return mapToResponse(computer);
    }

    @Transactional
    public ComputerResponse getByIdComputer(Long id) {
        Computer computer = computerRepository.findById(id).orElseThrow(() -> new NotFoundException("Computer with " + id + " id not found!"));
        ComputerResponse response = new ComputerResponse();
        computer.setName(response.getName());
        computer.setImage(response.getImage());
        computer.setBookingStatus(response.getBookingStatus());
        computer.setPricePerHour(response.getPricePerHour());
        computer.setDescription(response.getDescription());
        computer.setLocalDateTime(response.getLocalDateTime());
        computer.setNumberOfComputers(response.getNumberOfComputers());
        computer.setVipCabinPrice(response.getVipCabinPrice());
        return response;
    }

    @Transactional
    public ComputerResponse deleteById(Long id) {
        Computer computer = computerRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Computer with " + id + " id not found!"));
        ComputerResponse response = new ComputerResponse();
        computer.setName(response.getName());
        computer.setImage(response.getImage());
        computer.setBookingStatus(response.getBookingStatus());
        computer.setDescription(response.getDescription());
        computer.setPricePerHour(response.getPricePerHour());
        computer.setLocalDateTime(response.getLocalDateTime());

        return response;

    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email not found"));
    }

    public Computer mapToEntity(ComputerRequest request) {
        Computer computer = new Computer();
        computer.setDescription(request.getDescription());
        computer.setName(request.getName());
        computer.setImage(request.getImage());
        computer.setClubId(request.getClubId());
        computer.setPricePerHour(request.getPricePerHour());
        computer.setBookingStatus(BookingStatus.FREE);
        computer.setLocalDateTime(LocalDateTime.now());
        computer.setVipCabinPrice(request.getVipCabinPrice());
        computer.setGameTypeForHistory(GameTypeForHistory.COMPUTER);
        computer.setNumberOfComputers(computer.getNumberOfComputers());


        return computer;
    }

    public ComputerResponse mapToResponse(Computer computer) {
        if (computer == null) {
            return null;
        }
        ComputerResponse response = new ComputerResponse();
        response.setPricePerHour(computer.getPricePerHour());
        response.setId(computer.getId());
        response.setClubId(computer.getClubId());
        response.setImage(computer.getImage());
        response.setName(computer.getName());
        response.setDescription(computer.getDescription());
        response.setBookingStatus(BookingStatus.FREE);
        response.setLocalDateTime(computer.getLocalDateTime());
        response.setVipCabinPrice(computer.getVipCabinPrice());

        return response;
    }
}
