package com.alejandro.OpenEarth.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration("pictureConfiguration")
public class PictureConfiguration {

    @Value("${app.picture.url.prefix}")
    private String urlPrefix;

    public String getUrlPrefix() {
        return urlPrefix;
    }

}
