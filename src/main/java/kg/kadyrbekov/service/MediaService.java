package kg.kadyrbekov.service;

import kg.kadyrbekov.dto.ImageResponse;
import kg.kadyrbekov.exceptions.NotFoundException;
import kg.kadyrbekov.model.entity.Image;
import kg.kadyrbekov.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final ImageRepository imageRepository;

    @Transactional
    public ImageResponse getImageById(Long imageId) throws NotFoundException {
        ImageResponse response = new ImageResponse();
        Image image = new Image();
        response.setUrl(image.getUrl());
        imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image with id not found: " + imageId));
        return response;
    }

}

