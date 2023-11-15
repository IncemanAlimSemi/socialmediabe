package com.alseinn.socialmedia.response.general;

import com.alseinn.socialmedia.response.concrete.AbstractResponse;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralInformationResponse extends AbstractResponse {
    private Boolean isSuccess;
    private String message;
}
