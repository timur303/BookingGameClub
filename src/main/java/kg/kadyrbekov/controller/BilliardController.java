package kg.kadyrbekov.controller;

import io.swagger.annotations.*;
import kg.kadyrbekov.dto.BilliardsRequest;
import kg.kadyrbekov.dto.BilliardsResponse;
import kg.kadyrbekov.dto.GameZoneResponse;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.service.BilliardService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
@RequestMapping("/api/billiard")
@RequiredArgsConstructor
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")})
public class BilliardController {

    private final BilliardService billiardService;

    private final MessageSource messageSource;


    @ApiOperation(value = "Create new billiard")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = BilliardsResponse.class), @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @PostMapping(value = "/createBilliard", produces = "application/json")
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody BilliardsRequest billiardsRequest) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }
        BilliardsResponse billiardsResponse = billiardService.create(billiardsRequest);
        if (billiardsResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(billiardsResponse);
        }
        String messages = messageSource.getMessage("billiard.failed", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ApiOperation(value = "Update new billiard table")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = GameZoneResponse.class),
            @ApiResponse(code = 404, message = "Not found", response = ApiError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @PatchMapping(value = "/update/{id}", produces = "application/json")
    public ResponseEntity<?> updateBilliard(HttpServletRequest request, @PathVariable Long id, @RequestBody BilliardsRequest billiardsRequest) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        BilliardsResponse footballResponse = billiardService.update(id, billiardsRequest);
        if (footballResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(footballResponse);
        }
        String messages = messageSource.getMessage("football.not.found! ", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }


    @ApiOperation(value = "Get billiard table")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = GameZoneResponse.class),
            @ApiResponse(code = 404, message = "Unauthorized", response = ApiError.class),})
    @GetMapping(value = "/getByBilliard/{id}", produces = "application/json")
    public ResponseEntity<?> getById(HttpServletRequest request, @PathVariable Long id) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        BilliardsResponse billiardsResponse = billiardService.getByIdBilliard(id);
        if (billiardsResponse != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(billiardsResponse);
        }
        String messages = messageSource.getMessage("football.not.found", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }


    @ApiOperation(value = "Delete by id billiard table")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = GameZoneResponse.class),
            @ApiResponse(code = 404, message = "Not found!", response = ApiError.class),
            @ApiResponse(code = 500, message = "error from server !", response = ApiError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @DeleteMapping(value = "/deleteBilliardId/{id}", produces = "application/json")
    public ResponseEntity<?> deleteById(HttpServletRequest request, @PathVariable Long id) {
        String selectLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectLanguage != null) {
            locale = new Locale(selectLanguage);
        } else {
            locale = new Locale("ru");
        }
        BilliardsResponse response = billiardService.deleteById(id);
        if (response != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        String messages = messageSource.getMessage("football.not.found", null, locale);
        ApiError apiError = new ApiError();
        apiError.setReason(messages);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
