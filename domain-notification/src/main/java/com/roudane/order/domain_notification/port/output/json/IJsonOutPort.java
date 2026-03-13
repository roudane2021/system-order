package com.roudane.order.domain_notification.port.output.json;

public interface IJsonOutPort {
    String toJson(Object object);
    <T> T readValue(String json, Class<T> clazz);
}
