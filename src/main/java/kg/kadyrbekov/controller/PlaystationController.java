package kg.kadyrbekov.controller;


import io.swagger.annotations.*;
import kg.kadyrbekov.dto.PlayStationRequest;
import kg.kadyrbekov.dto.PlayStationResponse;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.repository.UserRepository;
import kg.kadyrbekov.service.PlaystationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
@RequestMapping("/api/playstation")
@RequiredArgsConstructor
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")})
public class PlaystationController {
    private final UserRepository userRepository;

    private final PlaystationService playstationService;

    private final MessageSource messageSource;

    @ApiOperation(value = "Create new playstation")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = PlayStationResponse.class), @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @PostMapping(value = "/createPlaystation", produces = "application/json")
    public ResponseEntity<?> createPlaystation(HttpServletRequest request, @RequestBody PlayStationRequest playStationRequest) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }

        try {
            Club userClub = getUserClub();
            if (userClub == null || !userClub.getId().equals(playStationRequest.getClubId())) {
                String errorMessage = messageSource.getMessage("playstation.creation.conflict", null, locale);
                ApiError apiError = new ApiError();
                apiError.setReason(errorMessage);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
            }

            PlayStationResponse playStationResponse = playstationService.create(playStationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(playStationResponse);
        } catch (AccessDeniedException e) {
            String errorMessage = messageSource.getMessage("playstation.creation.access.denied", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(errorMessage);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("playstation.failed", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
        }
    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email not found"));
    }

    private Club getUserClub() {
        User user = getAuthentication();
        return user != null ? user.getClub() : null;
    }


    @ApiOperation(value = "Update new playstation ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = PlayStationResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @PatchMapping(value = "/updatePlaystation/{id}", produces = "application/json")
    public ResponseEntity<?> updatePlaystation(HttpServletRequest request, @PathVariable Long id, @RequestBody PlayStationRequest playStationRequest) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        PlayStationResponse playStationResponse = playstationService.update(id, playStationRequest);
        if (playStationResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(playStationResponse);
        }
        String messages = messageSource.getMessage("playstation.not.found!", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ApiOperation(value = "Get playstation field")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = PlayStationResponse.class),
            @ApiResponse(code = 404, message = "Unauthorized", response = ApiError.class),})
    @GetMapping(value = "/getByPlaystation/{id}", produces = "application/json")
    public ResponseEntity<?> getById(HttpServletRequest request, @PathVariable Long id) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        PlayStationResponse playStationResponse = playstationService.getByIdFootball(id);
        if (playStationResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(playStationResponse);
        }
        String messages = messageSource.getMessage("playstation.not.found", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }


    @ApiOperation(value = "Delete by id playstation field")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = PlayStationResponse.class),
            @ApiResponse(code = 404, message = "Not found!", response = ApiError.class),
            @ApiResponse(code = 500, message = "error from server !", response = ApiError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @DeleteMapping(value = "/deletePlaystationId/{id}", produces = "application/json")
    public ResponseEntity<?> deleteById(HttpServletRequest request, @PathVariable Long id) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        PlayStationResponse response = playstationService.deleteById(id);
        if (response != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        String messages = messageSource.getMessage("playstation.not.found", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
