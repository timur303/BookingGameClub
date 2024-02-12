package kg.kadyrbekov.controller;

import io.swagger.annotations.*;
import kg.kadyrbekov.dto.ClubRequest;
import kg.kadyrbekov.dto.ClubResponse;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
})
public class ClubController {

    private final ClubService clubService;

    private final MessageSource messageSource;


    @ApiOperation(value = "Create new club")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = ClubResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),
    })
    @PostMapping(value = "/createClub", produces = "application/json")
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody ClubRequest clubRequest) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }

        try {
            ClubResponse clubResponse = clubService.createClub(clubRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(clubResponse);
        } catch (NotFoundException ex) {
            String errorMessage = messageSource.getMessage("user.not.found", null, locale);
            ApiError response = new ApiError();
            response.setReason(errorMessage);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    @ApiOperation(value = "Patch club method")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Club", response = ClubResponse.class),
            @ApiResponse(code = 404, message = "Club not found", response = ApiError.class)
    })
    @PatchMapping(value = "/update/{id}", produces = "application/json")
    public ResponseEntity<?> update(HttpServletRequest httpServletRequest, @PathVariable Long id, @RequestBody ClubRequest request) {
        String selectedLanguage = (String) httpServletRequest.getSession().getAttribute("language");
        Locale locale;

        locale = new Locale(Objects.requireNonNullElse(selectedLanguage, "ru"));

        try {
            ClubResponse response = clubService.updateClub(id, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            String messages = messageSource.getMessage("club.getID", null, locale);
            ApiError errorMessage = new ApiError();
            errorMessage.setReason(messages);
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }

    }

    @ApiOperation(value = "Get club method")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Club", response = ClubResponse.class),
            @ApiResponse(code = 404, message = "Club not found", response = ApiError.class)
    })
    @GetMapping(value = "/clubs/{id}", produces = "application/json")
    public ResponseEntity<?> getClubById(HttpServletRequest request, @PathVariable("id") Long id) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }

        try {
            ClubResponse response = clubService.getClubById(id);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            String messages = messageSource.getMessage("club.getID", null, locale);
            ApiError response = new ApiError();
            response.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ApiOperation(value = "Get all clubs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of clubs", response = ClubResponse.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "No clubs found", response = ApiError.class)
    })
    @GetMapping(value = "/getClubs", produces = "application/json")
    public ResponseEntity<?> getAllClubs() {
        try {
            List<ClubResponse> response = clubService.getAllClubs();
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ApiError errorResponse = new ApiError();
            errorResponse.setReason("No clubs found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @ApiOperation(value = "Delete club method")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Club not found", response = ApiError.class)})
    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
    public ResponseEntity<?> deleteCLub(HttpServletRequest request, @PathVariable("id") Long id) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }

        try {
            clubService.deleteClubById(id);
            String messages = messageSource.getMessage("club.deleted.success", null, locale);
            ApiError response = new ApiError();
            response.setReason(messages);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            String messages = messageSource.getMessage("club.getID", null, locale);
            ApiError invalid = new ApiError();
            invalid.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(invalid);
        }
    }
}
