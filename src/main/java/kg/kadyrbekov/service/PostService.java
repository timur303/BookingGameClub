package kg.kadyrbekov.service;

import kg.kadyrbekov.dto.ClubResponse;
import kg.kadyrbekov.dto.PostRequest;
import kg.kadyrbekov.dto.PostResponse;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Post;
import kg.kadyrbekov.repository.PostRepository;
import kg.kadyrbekov.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final ClubService clubService;

    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, ClubService clubService, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.clubService = clubService;
    }

    @Transactional
    public PostResponse savePost(Long clubId, PostRequest postRequest) {
        User user = getAuthentication();
        ClubResponse club = clubService.getClubById(clubId);
        Post post = mapToEntity(postRequest);
        postRequest.setUserId(user.getId());
        postRequest.setClubId(club.getId());
        postRepository.save(post);

        return mapToResponse(post);
    }


    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email not found"));
    }

    public Post mapToEntity(PostRequest request) {
        Post post = new Post();
        post.setClubId(request.getClubId());
        post.setClubId(request.getClubId());
        post.setImageUrl(request.getImageUrl());
        post.setLocalDateTime(request.getLocalDateTime());
        post.setDescription(request.getDescription());
        post.setId(request.getClubId());

        return post;
    }

    public PostResponse mapToResponse(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setClub(post.getClub());
        postResponse.setLocalDateTime(post.getLocalDateTime());
        postResponse.setDescription(post.getDescription());
        postResponse.setImageUrl(post.getImageUrl());
        postResponse.setClubId(post.getClubId());

        return postResponse;
    }


}
