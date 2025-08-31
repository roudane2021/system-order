package com.roudane.transverse.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;


    public <R> PageResult<R> map(Function<T, R> mapper) {

        List<R> mappedContent= Optional.ofNullable(this.content)
                .orElseGet(ArrayList::new)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());


        return PageResult.<R>builder()
                .content(mappedContent)
                .number(this.number)
                .size(this.size)
                .totalElements(this.totalElements)
                .totalPages(this.totalPages)
                .build();
    }



}
