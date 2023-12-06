package com.alseinn.socialmedia.response.image;

import com.alseinn.socialmedia.entity.image.Image;
import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse extends GeneralInformationResponse {
    private Image image;
}
