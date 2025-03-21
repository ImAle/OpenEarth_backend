package com.alejandro.OpenEarth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration("geoConfiguration")
public class GeolocationConfiguration {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
