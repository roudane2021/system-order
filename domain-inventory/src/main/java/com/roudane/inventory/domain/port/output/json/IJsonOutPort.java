package com.roudane.inventory.domain.port.output.json;

public interface IJsonOutPort {
    String toJson(Object object);
    <T> T readValue(String json, Class<T> clazz);
}
