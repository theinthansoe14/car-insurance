package com.tts.car_insurance.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carInsuranceApi(){
        return new OpenAPI().components(new Components())
                            .info(new Info()
                                    .title("Car Insurance Api")
                                    .version("1.1.0")
                                    .description("Rest Api Documentation"));
    }
}
