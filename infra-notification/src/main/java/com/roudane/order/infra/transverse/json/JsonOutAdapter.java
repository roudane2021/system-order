package com.roudane.order.infra.transverse.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roudane.order.domain_notification.port.output.json.IJsonOutPort;
import com.roudane.transverse.exception.InternalErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonOutAdapter implements IJsonOutPort {

    private final ObjectMapper objectMapper;

    @Override
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Error serializing outbox payload: " + e.getMessage());
        }
    }

    @Override
    public <T> T readValue(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException("Error deserializing outbox payload: " + e.getMessage());
        }
    }
}
