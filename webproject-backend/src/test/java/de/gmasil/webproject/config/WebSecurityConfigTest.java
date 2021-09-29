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
package de.gmasil.webproject.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gmasil.webproject.controller.PermitAll;
import de.gmasil.webproject.utils.SetupTestContext;

@SetupTestContext
class WebSecurityConfigTest {

    private static final List<Class<? extends Annotation>> MAPPING_ANNOTATIONS = Arrays.asList( //
            DeleteMapping.class, //
            PatchMapping.class, //
            PostMapping.class, //
            PutMapping.class, //
            RequestMapping.class, //
            GetMapping.class //
    );

    @Autowired
    private ListableBeanFactory listableBeanFactory;

    @Test
    void testAllRestMethodsAreProtected() {
        List<String> violations = new LinkedList<>();

        for (Method m : getAllRequestMappingMethods()) {
            PreAuthorize preAuthorize = m.getDeclaredAnnotation(PreAuthorize.class);
            if (preAuthorize == null && m.getDeclaredAnnotation(PermitAll.class) == null) {
                violations.add(m.getDeclaringClass().getName() + "." + m.getName());
            }
        }

        if (!violations.isEmpty()) {
            Collections.sort(violations);
            String msg = String.format(
                    "There are %d methods in RestControllers not protected by @PreAuthorize or explicitly made public with @PermitAll:\n\t- %s\n",
                    violations.size(), String.join("\n\t- ", violations));
            assertThat(msg, violations, hasSize(0));
        }
    }

    private boolean hasAnyAnnotation(Method method) {
        for (Class<? extends Annotation> annotation : MAPPING_ANNOTATIONS) {
            if (method.getDeclaredAnnotation(annotation) != null) {
                return true;
            }
        }
        return false;
    }

    private List<Method> getAllRequestMappingMethods() {
        List<Method> methods = new LinkedList<>();
        Map<String, Object> repos = listableBeanFactory.getBeansWithAnnotation(RestController.class);
        for (Object bean : repos.values()) {
            Class<?> c = AopUtils.getTargetClass(bean);
            if (c.getPackage().getName().startsWith("de.gmasil")) {
                Arrays.asList(c.getDeclaredMethods()).forEach(m -> {
                    if (hasAnyAnnotation(m)) {
                        methods.add(m);
                    }
                });
            }
        }
        return methods;
    }
}
