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

import java.awt.Color;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.collection.internal.AbstractPersistentCollection;
import org.hibernate.collection.internal.PersistentSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.SerializationHint;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.gmasil.webproject.jpa.Auditable;
import de.gmasil.webproject.jpa.role.Role;
import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.jpa.user.User;

@Configuration
@SerializationHint(types = { //
        Auditable.class, //
        User.class, //
        Role.class, //
        Theme.class, //
        Date.class, //
        Timestamp.class, //
        HashSet.class, //
        HashMap.class, //
        Set.class, //
        UserDetails.class, //
        GrantedAuthority.class, //
        Serializable.class, //
        AbstractPersistentCollection.class, //
        PersistentSet.class, //
        Color.class //
})
public class JpaHints {
}
