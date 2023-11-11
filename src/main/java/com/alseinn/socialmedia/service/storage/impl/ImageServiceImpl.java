package com.alseinn.socialmedia.service.storage.impl;

import com.alseinn.socialmedia.dao.image.ImageRepository;
import com.alseinn.socialmedia.entity.image.Image;
import com.alseinn.socialmedia.service.post.impl.PostServiceImpl;
import com.alseinn.socialmedia.service.storage.ImageService;
import com.alseinn.socialmedia.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.zip.*;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private static final Logger LOG = Logger.getLogger(PostServiceImpl.class.getName());


    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
        if (Objects.isNull(file)) {
            return null;
        }
        return imageRepository.save(Image.builder()
                .name(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())))
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .build());
    }

    @Override
    public byte[] getImage(Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        if (Objects.isNull(image)) {
            return null;
        }
        return ImageUtils.decompressImage(image.getImageData());
    }

    @Override
    public void deleteImage(Image image) {
        try {
            imageRepository.delete(image);
            LOG.warning("Image deleted successfully");
        } catch (Exception e) {
            LOG.warning(MessageFormat.format("Error occurred while deleting image: {0} : {1}", image.getId(), e.getMessage()));
        }
    }


}
