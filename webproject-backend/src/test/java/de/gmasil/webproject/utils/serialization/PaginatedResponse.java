/*
 * Webproject NG
 * Copyright © 2021 - 2022 Gmasil
 *
 * This file is part of Webproject NG.
 *
 * Webproject NG is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International
 * Public License ("Public License").
 *
 * Webproject NG is non-free software: you can redistribute
 * it and/or modify it under the terms of the Public License.
 *
 * You should have received a copy of the Public License along
 * with Webproject NG. If not, see
 * https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt
 */
package de.gmasil.webproject.utils.serialization;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class PaginatedResponse<T> extends PageImpl<T> {

    @JsonCreator(mode = PROPERTIES)
    public PaginatedResponse(@JsonProperty("content") List<T> content, @JsonProperty("number") int number,
            @JsonProperty("size") int size, @JsonProperty("totalElements") Long totalElements,
            @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
            @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") boolean first, @JsonProperty("empty") boolean empty) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    public PaginatedResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PaginatedResponse(List<T> content) {
        super(content);
    }

    public PaginatedResponse() {
        super(new ArrayList<>());
    }
}
