package kg.kadyrbekov.service;

import kg.kadyrbekov.dto.ClubRequest;
import kg.kadyrbekov.dto.ClubResponse;
import kg.kadyrbekov.dto.ReviewResponse;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.entity.Image;
import kg.kadyrbekov.model.entity.Review;
import kg.kadyrbekov.repository.ClubRepository;
import kg.kadyrbekov.repository.ImageRepository;
import kg.kadyrbekov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final UserRepository userRepository;

    private final ClubRepository clubRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public ClubResponse createClub(ClubRequest request) {
        User user = getAuthentication();
        Club club = mapToEntity(request);

        String logoUrl = request.getLogoUrl();

        Image image = new Image();
        image.setUrl(logoUrl);
        image.setClub(club);
        imageRepository.save(image);

//        club.setUser(user);
        club.setUserId(user.getId());
        clubRepository.save(club);

        return mapToResponse(club);
    }


    @Transactional
    public ClubResponse updateClub(Long id, ClubRequest request) {
        User user = getAuthentication();
        Club club = clubRepository.findById(id).orElseThrow(() -> new NotFoundException("Club with id not found"));

        club.setCity(request.getCity());
        club.setClubName(request.getClubName());
        club.setDescription(request.getDescription());
        club.setAddress(request.getAddress());
        club.setLinkInstagram(request.getLinkInstagram());

//        club.setUser(user);
        clubRepository.save(club);

        return mapToResponse(club);
    }

    @Transactional
    public void deleteClubById(Long id) throws NotFoundException {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Club with id " + id + " not found"));
        clubRepository.delete(club);
    }

    private ClubResponse mapToResponse(Club club) {
        if (club == null) {
            return null;
        }

        ClubResponse clubResponse = new ClubResponse();
        clubResponse.setId(club.getId());
        clubResponse.setClubName(club.getClubName());
        clubResponse.setCity(club.getCity());
        String logoUrl = club.getLogoUrl();
        clubResponse.setLogoUrl(logoUrl);
        clubResponse.setAddress(club.getAddress());
        clubResponse.setDescription(club.getDescription());
        clubResponse.setUserId(club.getUserId());
        clubResponse.setLogoUrl(club.getLogoUrl());
        clubResponse.setLocalDateTime(club.getLocalDateTime());

        return clubResponse;
    }
    @Transactional
    public List<ClubResponse> getAllClubs() {
        List<Club> clubs = clubRepository.findAll();

        return clubs.stream()
                .map(this::mapClubToClubResponse)
                .collect(Collectors.toList());
    }

    private ClubResponse mapClubToClubResponse(Club club) {
        ClubResponse response = new ClubResponse();
        response.setId(club.getId());
        response.setClubName(club.getClubName());
        response.setCity(club.getCity());
        response.setDescription(club.getDescription());
        response.setAddress(club.getAddress());
        response.setLinkInstagram(club.getLinkInstagram());
        response.setLogoUrl(club.getLogoUrl());

        List<Review> clubReviews = club.getReviews();
        List<ReviewResponse> reviewResponses = clubReviews.stream()
                .map(this::mapReviewToReviewResponse)
                .collect(Collectors.toList());

        response.setReviewResponse(reviewResponses);

        return response;
    }

    private ReviewResponse mapReviewToReviewResponse(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setId(review.getId());
        reviewResponse.setComments(review.getComments());
        reviewResponse.setStarRating(review.getStarsRating());
        reviewResponse.setClubId(review.getClub().getId());

        return reviewResponse;
    }
    @Transactional
    public ClubResponse getClubById(Long clubId) throws NotFoundException {
        Club club = clubRepository.findById(clubId).orElseThrow(() -> new NotFoundException("Club with id not found: " + clubId));
        ClubResponse response = new ClubResponse();
        response.setId(club.getId());
        response.setClubName(club.getClubName());
        response.setCity(club.getCity());
        response.setDescription(club.getDescription());
        response.setAddress(club.getAddress());
        response.setLinkInstagram(club.getLinkInstagram());
        response.setLogoUrl(club.getLogoUrl());

        List<Review> carReviews = club.getReviews();
        List<ReviewResponse> reviewResponses = new ArrayList<>();

        for (Review review : carReviews) {
            ReviewResponse reviewResponse = new ReviewResponse();
            reviewResponse.setId(review.getId());
            reviewResponse.setComments(review.getComments());
            reviewResponse.setStarRating(review.getStarsRating());
            reviewResponse.setClubId(review.getClub().getId());
            reviewResponses.add(reviewResponse);
        }

        response.setReviewResponse(reviewResponses);


        return response;

    }


    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("User with email not found"));
    }

    public Club mapToEntity(ClubRequest request) {
        Club club = new Club();
        club.setClubName(request.getClubName());
        club.setCity(request.getCity());
        club.setDescription(request.getDescription());
        club.setAddress(request.getAddress());
        club.setLinkInstagram(request.getLinkInstagram());
        club.setLogoUrl(request.getLogoUrl());
        club.setLocalDateTime(LocalDateTime.now());
        return club;
    }

    public ClubResponse clubResponse(Club club) {
        return ClubResponse.builder()
                .id(club.getId())
                .clubName(club.getClubName())
                .description(club.getDescription())
                .city(club.getCity())
                .address(club.getAddress())
                .logoUrl(club.getLogoUrl())
                .build();
    }
}
