package kg.kadyrbekov.controller;

import io.swagger.annotations.*;
import kg.kadyrbekov.dto.*;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.exceptions.BookingConflictException;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.service.BookingService;
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
@RequestMapping("/api/booking")
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")})
public class BookingController {

    private final BookingService bookingService;

    private final MessageSource messageSource;


    @ApiOperation(value = "Booking to GameZone ")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = BookingResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
            @ApiResponse(code = 409, message = "Conflict", response = ApiError.class)})
    @PostMapping(value = "/bookingGameZone", produces = "application/json")
    public ResponseEntity<?> bookGameZone(HttpServletRequest request, @RequestBody BookingRequestFootball bookingRequestFootball) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale = selectedLanguage != null ? new Locale(selectedLanguage) : new Locale("ru");

        try {
            BookingResponse bookingResponse = bookingService.bookGameZone(bookingRequestFootball);
            return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            String messages = messageSource.getMessage("gameZone.not.found", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (BookingConflictException e) {
            String messages = messageSource.getMessage("gameZone.busy", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        }
    }


    @ApiOperation(value = "Booking to tennis")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = BookingResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
            @ApiResponse(code = 409, message = "Conflict", response = ApiError.class)})
    @PostMapping(value = "/bookingTennis", produces = "application/json")
    public ResponseEntity<?> bookTennis(@RequestBody BookingRequestTennis bookingRequestTennis) {
        try {
            BookingResponseTennis bookingResponse = bookingService.bookTennis(bookingRequestTennis);
            return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            String messages = messageSource.getMessage("tennis.not.found", null, Locale.getDefault());
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (BookingConflictException e) {
            String messages = messageSource.getMessage("tennis.busy", null, Locale.getDefault());
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        } catch (RuntimeException e) {
            String messages = messageSource.getMessage("tennis.busy", null, Locale.getDefault());
            ApiError response = new ApiError();
            response.setReason(messages);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @ApiOperation(value = "Booking to billiard")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = BookingResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
            @ApiResponse(code = 409, message = "Conflict", response = ApiError.class)})
    @PostMapping(value = "/bookingBilliard", produces = "application/json")
    public ResponseEntity<?> bookBilliard(@RequestBody BookingRequestBilliard bookingRequestBilliard) {
        try {
            BookingResponseBilliard bookingResponse = bookingService.bookBilliard(bookingRequestBilliard);
            return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            String messages = messageSource.getMessage("billiard.not.found", null, Locale.getDefault());
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (BookingConflictException e) {
            String messages = messageSource.getMessage("billiard.busy", null, Locale.getDefault());
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        } catch (RuntimeException e) {
            String messages = messageSource.getMessage("billiard.busy", null, Locale.getDefault());
            ApiError response = new ApiError();
            response.setReason(messages);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @ApiOperation(value = "Booking to playstation")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = BookingResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
            @ApiResponse(code = 409, message = "Conflict", response = ApiError.class)})
    @PostMapping(value = "/bookingPlaystation", produces = "application/json")
    public ResponseEntity<?> bookPlaystation(@RequestBody BookingRequestPlaystation bookingRequestPlaystation) {
        try {
            BookingResponsePlaystation bookingResponse = bookingService.bookPlaystation(bookingRequestPlaystation);
            return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            String messages = messageSource.getMessage("playstation.not.found", null, Locale.getDefault());
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (BookingConflictException e) {
            String messages = messageSource.getMessage("playstation.busy", null, Locale.getDefault());
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        } catch (RuntimeException e) {
            String messages = messageSource.getMessage("playstation.busy", null, Locale.getDefault());
            ApiError response = new ApiError();
            response.setReason(messages);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @ApiOperation(value = "Booking to computer")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = BookingResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
            @ApiResponse(code = 409, message = "Conflict", response = ApiError.class)})
    @PostMapping(value = "/bookingComputer", produces = "application/json")
    public ResponseEntity<?> bookComputer(@RequestBody BookingRequestComputer bookingRequestComputer) {
        try {
            BookingResponseComputer bookingResponse = bookingService.bookComputer(bookingRequestComputer);
            return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            String messages = messageSource.getMessage("computer.not.found", null, Locale.getDefault());
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (BookingConflictException e) {
            String messages = messageSource.getMessage("computer.busy", null, Locale.getDefault());
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
        } catch (RuntimeException e) {
            String messages = messageSource.getMessage("server.computer", null, Locale.getDefault());
            ApiError response = new ApiError();
            response.setReason(messages);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
