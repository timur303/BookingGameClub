package kg.kadyrbekov.controller;

import io.jsonwebtoken.MalformedJwtException;
import io.swagger.annotations.*;
import kg.kadyrbekov.dto.ManagerRequest;
import kg.kadyrbekov.dto.UserDTO;
import kg.kadyrbekov.dto.UserResponse;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.exceptions.IncorrectLoginException;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.entity.Club;
import kg.kadyrbekov.model.enums.Role;
import kg.kadyrbekov.repository.UserRepository;
import kg.kadyrbekov.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
})
public class ManagerController {

    private final ManagerService managerService;

    private final MessageSource messageSource;

    private final UserRepository userRepository;

    @ApiOperation(value = "Create manager")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully", response = UserResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),
            @ApiResponse(code = 409, message = "Conflict", response = ApiError.class),
            @ApiResponse(code = 404, message = "Club not found", response = ApiError.class),
            @ApiResponse(code = 403, message = "JWT", response = ApiError.class)
    })
    @PostMapping(value = "/createManager", produces = "application/json")
    public ResponseEntity<?> createManager(HttpServletRequest request, @RequestBody ManagerRequest userRequest) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");

        }
        try {
            if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
                String errorMessage = messageSource.getMessage("email.exists", null, locale);
                ApiError response = new ApiError();
                response.setReason(errorMessage);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            if (userRepository.findByPhoneNumber(userRequest.getPhoneNumber()).isPresent()) {
                String errorMessage = messageSource.getMessage("phone.exists", null, locale);
                ApiError response = new ApiError();
                response.setReason(errorMessage);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            if (userRepository.findByClubId(userRequest.getClubId()).isPresent()) {
                String errorMessage = messageSource.getMessage("manager.exists.for.club", null, locale);
                ApiError apiError = new ApiError();
                apiError.setReason(errorMessage);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
            }

            UserResponse userResponse = managerService.createManager(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (
                NotFoundException e) {
            String messages = messageSource.getMessage("club.not.found", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (
                HttpClientErrorException.Unauthorized e) {
            String messages = messageSource.getMessage("un.authorized", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
        } catch (
                MalformedJwtException e) {
            String errorMessage = messageSource.getMessage("m.jwt", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
        }

    }


    @ApiOperation(value = "update manager")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = UserResponse.class),
            @ApiResponse(code = 404, message = "not found", response = ApiError.class),
    })
    @PatchMapping("/updateUser/{userId}")
    public ResponseEntity<?> updateUser(HttpServletRequest request, @PathVariable Long userId, @RequestBody ManagerRequest userRequest) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }
        try {
            UserResponse userResponse = managerService.updateManager(userId, userRequest);
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        } catch (NotFoundException ex) {
            String messages = messageSource.getMessage("user.notfound", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        }

    }

    @ApiOperation(value = "Delete manager")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class),
    })
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<?> deleteUser(HttpServletRequest request, @PathVariable Long userId) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }
        try {
            String messages = messageSource.getMessage("user.successfully.deleted", null, locale);
            managerService.deleteManager(userId);
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.ok(apiError);
        } catch (NotFoundException ex) {
            String messages = messageSource.getMessage("user.notfound", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "Success ", response = UserDTO.class),
            @ApiResponse(code = 404, message = "User not found ", response = ApiError.class)
    })
    @GetMapping(value = "getUser/{userId}", produces = "application/json")
    public ResponseEntity<?> getUserByID(HttpServletRequest request, @PathVariable Long userId) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }

        try {
            UserDTO user = managerService.getUserByID(userId);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            ApiError error = new ApiError();
            String message = messageSource.getMessage("user.notfound", null, locale);
            error.setReason(message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

}
