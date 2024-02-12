package kg.kadyrbekov.controller;

import io.swagger.annotations.*;
import kg.kadyrbekov.dto.ChooseCity;
import kg.kadyrbekov.dto.UserResponse;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.User;
import kg.kadyrbekov.model.enums.City;
import kg.kadyrbekov.repository.UserRepository;
import kg.kadyrbekov.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.attribute.UserPrincipal;
import java.util.Locale;

@RestController
@RequestMapping
@RequiredArgsConstructor
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
})
public class ChooseCityController {

    private final UserRepository userRepository;
    private final MessageSource messageSource;

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email not found"));
    }


    @ApiOperation(value = "Update user chosen city")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated users city", response = City.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class)})
    @PostMapping(value = "/update-user-city", produces = "application/json")
    public ResponseEntity<?> updateUserCity(HttpServletRequest request, @RequestBody ChooseCity chooseCity) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale = selectedLanguage != null ? new Locale(selectedLanguage) : new Locale("ru");

        try {
            User user = getAuthentication();

            if (user == null) {
                String messages = messageSource.getMessage("user.not.found", null, locale);
                ApiError apiError = new ApiError();
                apiError.setReason(messages);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
            }
            user.setCity(chooseCity.getCity());
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(user.getCity());
        } catch (Exception e) {
            String messages = messageSource.getMessage("failed.to.update", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(apiError);
        }
    }



}