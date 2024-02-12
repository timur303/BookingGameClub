package kg.kadyrbekov.controller;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import kg.kadyrbekov.dto.PostRequest;
import kg.kadyrbekov.dto.PostResponse;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
})
public class PostController {


    private final MessageSource messageSource;

    private final PostService postService;


    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<?> createPost(HttpServletRequest servletRequest, @RequestBody PostRequest request) {
        String selectLanguage = (String) servletRequest.getSession().getAttribute("language");
        Locale locale = null;
        if (selectLanguage != null) {
            locale = new Locale("ru");
        }
        try {
            PostResponse postResponse = postService.savePost(request.getClubId(),request);
            return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
        } catch (NotFoundException ex) {
            String errorMessage = messageSource.getMessage("club.not.found", null, locale);
            ApiError response = new ApiError();
            response.setReason(errorMessage);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

    }

}
