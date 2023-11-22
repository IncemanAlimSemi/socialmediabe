package com.alseinn.socialmedia.service.storage.impl;

import com.alseinn.socialmedia.dao.image.ImageRepository;
import com.alseinn.socialmedia.entity.image.Image;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import com.alseinn.socialmedia.response.image.ImageResponse;
import com.alseinn.socialmedia.service.storage.ImageService;
import com.alseinn.socialmedia.utils.ImageUtils;
import com.alseinn.socialmedia.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Logger;

import static com.alseinn.socialmedia.utils.contants.AppTRConstants.LOCALIZATION;
import static com.alseinn.socialmedia.utils.contants.AppTRConstants.PICTURE;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ResponseUtils responseUtils;
    private static final Logger LOG = Logger.getLogger(ImageServiceImpl.class.getName());

    @Override
    public ImageResponse uploadImage(MultipartFile file) throws IOException {
        if (Objects.nonNull(file)) {
            try{
                Image image = imageRepository.save(Image.builder()
                        .name(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())))
                        .type(file.getContentType())
                        .imageData(ImageUtils.compressImage(file.getBytes()))
                        .build());

                return ImageResponse.imageResponseBuilder()
                        .isSuccess(true)
                        .message(MessageFormat.format(ResponseUtils.getProperties(LOCALIZATION).getProperty("saved.with.success"), PICTURE))
                        .image(image)
                        .build();
            }catch (Exception e) {
                LOG.warning(MessageFormat.format("Error occurred while uploading image: {0} : {1}"
                        , file.getOriginalFilename(), e.getMessage()));
                return ImageResponse.imageResponseBuilder()
                        .isSuccess(false)
                        .message(MessageFormat.format(ResponseUtils.getProperties(LOCALIZATION).getProperty("could.not.be.saved"), PICTURE))
                        .build();

            }
        }

       return ImageResponse.imageResponseBuilder()
                .isSuccess(false)
                .message(ResponseUtils.getProperties(LOCALIZATION).getProperty("empty"))
                .build();

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
    public GeneralInformationResponse deleteImage(Image image) throws IOException {
        if (Objects.nonNull(image)){
            try {
                imageRepository.delete(image);
                LOG.warning(MessageFormat.format("Image deleted with success: {0}", image.getName()));
                return responseUtils.createGeneralInformationResponse(true,
                        MessageFormat.format(ResponseUtils.getProperties(LOCALIZATION).getProperty("deleted.with.success"), PICTURE));
            } catch (Exception e) {
                LOG.warning("Error occurred while deleting image: " + e);
                return responseUtils.createGeneralInformationResponse(false,
                        MessageFormat.format(ResponseUtils.getProperties(LOCALIZATION).getProperty("could.not.be.deleted"), PICTURE));
            }
        }

        LOG.warning("Image is null!");
        return responseUtils.createGeneralInformationResponse(false,
                MessageFormat.format(ResponseUtils.getProperties(LOCALIZATION).getProperty("empty"), PICTURE));

    }

}
