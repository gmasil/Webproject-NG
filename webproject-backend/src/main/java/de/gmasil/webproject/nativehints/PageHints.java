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
package de.gmasil.webproject.nativehints;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;

@Configuration
@TypeHint(types = { //
        Page.class, //
        Slice.class, //
        List.class, //
        Sort.class, //
        Pageable.class, //
        Streamable.class //
}, access = { //
        TypeAccess.DECLARED_CLASSES, //
        TypeAccess.DECLARED_CONSTRUCTORS, //
        TypeAccess.DECLARED_FIELDS, //
        TypeAccess.DECLARED_METHODS //
})
public class PageHints {
}
