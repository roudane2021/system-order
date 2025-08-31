package com.roudane.transverse.criteria;

import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CriteriaApplication {
    private String name;
    private Object value;
    private List<Object> listValue;
    private OperatorApplication operator;
}