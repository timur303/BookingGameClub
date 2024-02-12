package kg.kadyrbekov.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kg.kadyrbekov.dto.ImageResponse;
import kg.kadyrbekov.dto.MessageInvalid;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.mapper.LoginResponse;
import kg.kadyrbekov.model.entity.Image;
import kg.kadyrbekov.repository.ImageRepository;
import kg.kadyrbekov.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class MediaController {

    private final MessageSource messageSource;

    private final ImageRepository imageRepository;
    private final MediaService mediaService;
    private final Cloudinary cloudinary;


    @ApiOperation(value = "image Upload")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = ImageResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class)})
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> uploadImage(HttpServletRequest request, @RequestPart("file") MultipartFile file) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("No file uploaded.");
            }

            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String avatarUrl = (String) uploadResult.get("secure_url");

            MessageInvalid messageInvalid = new MessageInvalid();
            messageInvalid.setAvatarUrl(avatarUrl);
            return ResponseEntity.ok(messageInvalid);
        } catch (IOException e) {
            String message = messageSource.getMessage("image.failed", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
        } catch (IllegalArgumentException e) {
            String message = messageSource.getMessage("no.file.uploaded", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
        }
    }


    @ApiOperation(value = "Get image ")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = ImageResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class)})
    @RequestMapping(value = "/getImage/{imageId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ImageResponse> getImageById(@PathVariable Long imageId) {
        try {
            ImageResponse image = mediaService.getImageById(imageId);
            return ResponseEntity.ok(image);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @ApiOperation(value = "Delete image by id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = ImageResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class)})
    @RequestMapping(value = "/deleteImageById/{imageId}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> deleteAvatar(HttpServletRequest request, @PathVariable Long imageId) {
        String selectedLanguage = (String) request.getSession().getAttribute("language");
        Locale locale;
        if (selectedLanguage != null) {
            locale = new Locale(selectedLanguage);
        } else {
            locale = new Locale("ru");
        }

        try {
            String message = messageSource.getMessage("avatar.getID", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(message);
            Image image = imageRepository.findById(imageId).get();
            cloudinary.uploader().destroy(image.getUrl(), ObjectUtils.emptyMap());
            imageRepository.delete(image);
            String messages = messageSource.getMessage("avatar.deleted", null, locale);
            ApiError apiError1 = new ApiError();
            apiError1.setReason(messages);
            return ResponseEntity.ok(apiError1);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            String messages = messageSource.getMessage("avatar.getID", null, locale);
            ApiError apiError = new ApiError();
            apiError.setReason(messages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        }
    }

}
