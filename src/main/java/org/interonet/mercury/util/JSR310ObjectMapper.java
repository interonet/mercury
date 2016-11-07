package org.interonet.mercury.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JSR310ObjectMapper extends ObjectMapper {
    @PostConstruct
    public void init() {
        registerModule(new JavaTimeModule());
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

}
