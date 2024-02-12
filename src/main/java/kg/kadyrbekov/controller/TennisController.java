package kg.kadyrbekov.controller;

import io.swagger.annotations.*;
import kg.kadyrbekov.dto.BilliardsResponse;
import kg.kadyrbekov.dto.TennisRequest;
import kg.kadyrbekov.dto.TennisResponse;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.repository.UserRepository;
import kg.kadyrbekov.service.TennisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
@RequestMapping("/api/tennis")
@RequiredArgsConstructor
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")})
public class TennisController {

    private final TennisService tennisService;

    private final MessageSource messageSource;

    private final UserRepository userRepository;


    @ApiOperation(value = "Create new tennis")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = BilliardsResponse.class), @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @PostMapping(value = "/createTennis", produces = "application/json")
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody TennisRequest tennisRequest) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }
        TennisResponse tennisResponse = tennisService.create(tennisRequest);
        if (tennisResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tennisResponse);
        }
        String messages = messageSource.getMessage("tennis.failed", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
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


    @ApiOperation(value = "Update new tennis ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = TennisResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @PatchMapping(value = "/updateTennis/{id}", produces = "application/json")
    public ResponseEntity<?> updateTennis(HttpServletRequest request, @PathVariable Long id, @RequestBody TennisRequest tennisRequest) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        TennisResponse tennisResponse = tennisService.update(id, tennisRequest);
        if (tennisResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tennisResponse);
        }
        String messages = messageSource.getMessage("tennis.not.found", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ApiOperation(value = "Get tennis table")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = TennisResponse.class),
            @ApiResponse(code = 404, message = "Unauthorized", response = ApiError.class),})
    @GetMapping(value = "/getByTennis/{id}", produces = "application/json")
    public ResponseEntity<?> getById(HttpServletRequest request, @PathVariable Long id) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        TennisResponse tennisResponse = tennisService.getByIdFootball(id);
        if (tennisResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tennisResponse);
        }
        String messages = messageSource.getMessage("tennis.not.found", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }


    @ApiOperation(value = "Delete by id tennis table")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = TennisResponse.class),
            @ApiResponse(code = 404, message = "Not found!", response = ApiError.class),
            @ApiResponse(code = 500, message = "error from server !", response = ApiError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @DeleteMapping(value = "/deleteFootballId/{id}", produces = "application/json")
    public ResponseEntity<?> deleteById(HttpServletRequest request, @PathVariable Long id) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        TennisResponse response = tennisService.deleteById(id);
        if (response != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        String messages = messageSource.getMessage("tennis.not.found", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

}
