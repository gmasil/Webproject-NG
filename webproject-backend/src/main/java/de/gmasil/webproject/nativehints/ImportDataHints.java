/*
 * Webproject NG
 * Copyright © 2021 Gmasil
 *
 * This file is part of Webproject NG.
 *
 * Webproject NG is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Webproject NG is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Webproject NG. If not, see <https://www.gnu.org/licenses/>.
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
