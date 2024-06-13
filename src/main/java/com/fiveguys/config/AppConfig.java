package com.fiveguys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        if(System.getProperty("os.name").charAt(0) == 'W'){
            System.out.println("WINDOW");
            registry.addResourceHandler("images/**")
                    .addResourceLocations("file:///C:/fiveguys_image/");
        }else {
            System.out.println("MAC");
            registry.addResourceHandler("images/**")
                    .addResourceLocations("file:///Users/fiveguys_image/");
        }

    }
}
