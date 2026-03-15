package com.roudane.inventory.domain.port.output.json;



public interface IJsonOutPort {

    String toJson(Object object);
    Object readValue(String payload, Class<?> cl);

}
