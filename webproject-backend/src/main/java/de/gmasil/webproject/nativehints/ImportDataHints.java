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

import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;

import de.gmasil.webproject.service.dataimport.ImportData;
import de.gmasil.webproject.service.dataimport.ImportData.ImportTheme;
import de.gmasil.webproject.service.dataimport.ImportData.ImportUser;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo.ImportComment;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo.ImportFile;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo.ImportRating;

@Configuration
@TypeHint(types = { //
        ImportData.class, //
        ImportUser.class, //
        ImportTheme.class, //
        ImportVideo.class, //
        ImportFile.class, //
        ImportComment.class, //
        ImportRating.class //
}, access = { //
        TypeAccess.PUBLIC_CLASSES, //
        TypeAccess.PUBLIC_CONSTRUCTORS, //
        TypeAccess.PUBLIC_FIELDS, //
        TypeAccess.PUBLIC_METHODS //
})
public class ImportDataHints {
}
