package com.alseinn.socialmedia.utils;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseUtils {

    private final MessageSource messageSource;

    public GeneralInformationResponse createGeneralInformationResponse(Boolean isSuccess, String message) {
        return GeneralInformationResponse.builder()
                .isSuccess(isSuccess)
                .message(message)
                .build();
    }

    public String getMessage(String value, Object... args) {
        return messageSource.getMessage(value, args, LocaleContextHolder.getLocale());
    }
}
