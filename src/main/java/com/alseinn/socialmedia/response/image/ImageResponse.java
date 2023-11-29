package com.alseinn.socialmedia.response.image;

import com.alseinn.socialmedia.entity.image.Image;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse extends GeneralInformationResponse {
    private Image image;

    @Builder(builderMethodName = "imageResponseBuilder")
    @SuppressWarnings(value = "unused")
    public ImageResponse(Boolean isSuccess, String message, Image image) {
        super(isSuccess, message);
        this.image = image;
    }
}
