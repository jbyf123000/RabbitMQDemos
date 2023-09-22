package top.jbyf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyConfig {
    @Bean
    public ConcurrentHashMap<String,String> sentMessage(){
        return new ConcurrentHashMap<>();
    }
}
