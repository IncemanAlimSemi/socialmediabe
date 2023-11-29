package com.alseinn.socialmedia.utils.exceptions;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralInformationResponse httpMessageNotReadableException(Exception exception) {
        return GeneralInformationResponse.builder()
                .isSuccess(false)
                .message(exception.getMessage())
                .build();
    }
}
