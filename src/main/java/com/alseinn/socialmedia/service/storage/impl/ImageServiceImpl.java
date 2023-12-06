package com.alseinn.socialmedia.service.storage.impl;

import com.alseinn.socialmedia.dao.image.ImageRepository;
import com.alseinn.socialmedia.entity.image.Image;
import com.alseinn.socialmedia.response.image.ImageResponse;
import com.alseinn.socialmedia.service.storage.ImageService;
import com.alseinn.socialmedia.utils.ImageUtils;
import com.alseinn.socialmedia.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;

import static com.alseinn.socialmedia.utils.contants.AppTRConstants.*;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ResponseUtils responseUtils;
    private static final Logger LOG = Logger.getLogger(ImageServiceImpl.class.getName());

    @Override
    public ImageResponse uploadImage(MultipartFile file) {
        if (Objects.nonNull(file) && !file.isEmpty()) {
            if (!isImage(file.getContentType())) {
                LOG.warning(responseUtils.getMessage("not.an.image", file.getOriginalFilename()));
                return ImageResponse.imageResponseBuilder()
                        .isSuccess(false)
                        .message(responseUtils.getMessage("not.an.image", file.getOriginalFilename()))
                        .build();
            }
            try{
                Image image = imageRepository.save(Image.builder()
                        .name(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())))
                        .type(file.getContentType())
                        .imageData(ImageUtils.compressImage(file.getBytes()))
                        .timeCreated(new Date(System.currentTimeMillis()))
                        .timeModified(new Date(System.currentTimeMillis()))
                        .build());

                LOG.info(responseUtils.getMessage("saved.with.success", IMAGE) + ": " + image.getName());
                return ImageResponse.imageResponseBuilder()
                        .isSuccess(true)
                        .message(responseUtils.getMessage("saved.with.success", IMAGE))
                        .image(image)
                        .build();
            }catch (Exception e) {
                LOG.warning(MessageFormat.format("Error occurred while uploading image: {0} : {1}"
                        , file.getOriginalFilename(), e.getMessage()));
                return ImageResponse.imageResponseBuilder()
                        .isSuccess(false)
                        .message(responseUtils.getMessage("could.not.be.saved", IMAGE))
                        .build();
            }
        }

        LOG.warning(responseUtils.getMessage("null", IMAGE));
        return ImageResponse.imageResponseBuilder()
                .isSuccess(false)
                .message(responseUtils.getMessage("null", IMAGE))
                .build();

    }

    @Override
    public byte[] getImage(Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        if (Objects.isNull(image)) {
            LOG.warning(responseUtils.getMessage("this.id.is.not.found.in.database", IMAGE, id));
            return null;
        }
        LOG.warning(responseUtils.getMessage("created.with.success", DECOMPRESS));
        return ImageUtils.decompressImage(image.getImageData());
    }

    @Override
    public void deleteImage(Image image) {
        if (Objects.nonNull(image)){
            try {
                imageRepository.delete(image);
                LOG.info(responseUtils.getMessage("deleted.with.success", IMAGE) + ": " + image.getName());
            } catch (Exception e) {
                LOG.warning("Error occurred while deleting image: " + e);
            }
        }
        LOG.warning(responseUtils.getMessage("null", IMAGE));
    }

    private boolean isImage(String contentType) {
        return Objects.nonNull(contentType) && contentType.startsWith("image/");
    }

}
