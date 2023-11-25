package com.alseinn.socialmedia.utils;

import com.alseinn.socialmedia.response.general.GeneralInformationResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class ResponseUtils {

    public GeneralInformationResponse createGeneralInformationResponse(Boolean isSuccess, String message) {
        return GeneralInformationResponse.builder()
                .isSuccess(isSuccess)
                .message(message)
                .build();
    }

    public static Properties getProperties(String localization) throws IOException {
        InputStream input = ResponseUtils.class.getClassLoader().getResourceAsStream("localization-" + localization + ".properties");
        Properties properties = new Properties();
        properties.load(input);

        return properties;
    }
}
