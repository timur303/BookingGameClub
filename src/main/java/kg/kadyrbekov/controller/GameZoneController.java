package kg.kadyrbekov.controller;

import io.swagger.annotations.*;
import kg.kadyrbekov.dto.GameZoneRequest;
import kg.kadyrbekov.dto.GameZoneResponse;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.entity.GameZone;
import kg.kadyrbekov.repository.UserRepository;
import kg.kadyrbekov.service.GameZoneService;
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
@RequestMapping("/api/gameZone")
@RequiredArgsConstructor
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")})
public class GameZoneController {

    private final UserRepository userRepository;

    private final GameZoneService gameZoneService;

    private final MessageSource messageSource;

    @ApiOperation(value = "Create new gameZone field")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = GameZoneResponse.class), @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @PostMapping(value = "/createGameZone", produces = "application/json")
    public ResponseEntity<?> createGameZOneField(HttpServletRequest request, @RequestBody GameZoneRequest gameZoneRequest) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }

        try {
            Club userClub = getUserClub();
            if (userClub == null || !userClub.getId().equals(gameZoneRequest.getClubId())) {
                String errorMessage = messageSource.getMessage("football.creation.conflict", null, locale);
                ApiError apiError = new ApiError();
                apiError.setReason(errorMessage);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
            }

            GameZoneResponse gameZoneResponse = gameZoneService.create(gameZoneRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(gameZoneResponse);
        } catch (AccessDeniedException e) {
            String errorMessage = messageSource.getMessage("football.creation.access.denied", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(errorMessage);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("football.failed", null, locale);
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


    @ApiOperation(value = "Update new gameZone field")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = GameZoneResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @PatchMapping(value = "/updateGameZoneById/{id}", produces = "application/json")
    public ResponseEntity<?> updateFootball(HttpServletRequest request, @PathVariable Long id, @RequestBody GameZoneRequest gameZoneRequest) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        GameZoneResponse gameZoneResponse = gameZoneService.update(id, gameZoneRequest);
        if (gameZoneResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(gameZoneResponse);
        }
        String messages = messageSource.getMessage("football.not.found!", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ApiOperation(value = "Get football field")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = GameZoneResponse.class),
            @ApiResponse(code = 404, message = "Unauthorized", response = ApiError.class),})
    @GetMapping(value = "/getByIdGameZone/{id}", produces = "application/json")
    public ResponseEntity<?> getById(HttpServletRequest request, @PathVariable Long id) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        GameZone gameZone = gameZoneService.getByIdGamZone(id);
        if (gameZone != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(gameZone);
        }
        String messages = messageSource.getMessage("football.not.found!", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }


    @ApiOperation(value = "Delete by id Game Zone ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = GameZoneResponse.class),
            @ApiResponse(code = 404, message = "Not found!", response = ApiError.class),
            @ApiResponse(code = 500, message = "error from server !", response = ApiError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @DeleteMapping(value = "/deleteGameZOneById/{id}", produces = "application/json")
    public ResponseEntity<?> deleteById(HttpServletRequest request, @PathVariable Long id) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        GameZoneResponse response = gameZoneService.deleteById(id);
        if (response != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        String messages = messageSource.getMessage("football.not.found!", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
