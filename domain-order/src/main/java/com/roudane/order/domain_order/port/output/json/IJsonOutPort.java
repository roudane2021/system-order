package com.roudane.order.domain_order.port.output.json;



public interface IJsonOutPort {

    String toJson(Object object);


    Object readValue(String payload, Class<?> cl);

}