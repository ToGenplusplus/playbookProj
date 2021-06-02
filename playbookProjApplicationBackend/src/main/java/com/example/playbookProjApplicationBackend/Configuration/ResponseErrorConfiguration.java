package com.example.playbookProjApplicationBackend.Configuration;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResponseErrorConfiguration {

    @Bean
    public ResponseError responseError(){
        return new ResponseError();
    }


}
