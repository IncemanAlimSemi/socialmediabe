package com.alseinn.socialmedia.request.image;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageRequest {
    @NotBlank(message = "{image.can.not.be.empty}")
    private MultipartFile image;
}
