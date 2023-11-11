package com.alseinn.socialmedia.service.storage;

import com.alseinn.socialmedia.entity.image.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    Image uploadImage(MultipartFile file) throws IOException;

    byte[] getImage(Long id) throws IOException;

    void deleteImage(Image image);
}
