package com.alseinn.socialmedia.response.general;

import com.alseinn.socialmedia.response.concrete.AbstractResponse;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class GeneralInformationResponse extends AbstractResponse {
    private Boolean isSuccess;
    private String message;
}
