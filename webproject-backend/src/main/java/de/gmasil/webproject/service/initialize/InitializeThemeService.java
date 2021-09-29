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
package de.gmasil.webproject.service.initialize;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gmasil.webproject.jpa.globalproperty.Property;
import de.gmasil.webproject.jpa.globalproperty.PropertyRepository;
import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.jpa.theme.ThemeRepository;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserService;

@Service
public class InitializeThemeService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ModelMapper mapper = new ModelMapper();

    @Autowired
    private InitializeProperties properties;

    @Autowired
    private ThemeRepository themeRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public void initTheme() {
        if (themeRepo.count() == 0) {
            Theme theme = new Theme();
            mapper.map(properties.getTheme(), theme);
            theme.setPreset(true);
            theme = themeRepo.save(theme);
            propertyRepo.setProperty(Property.DEFAULT_THEME, "" + theme.getId());
            // set default theme for admin user
            Optional<User> adminUser = userService.findByUsername(properties.getAdmin().getName());
            if (adminUser.isPresent()) {
                User admin = adminUser.get();
                admin.setActiveTheme(theme);
                userService.save(admin);
            } else {
                LOG.warn("Admin user '{}' does not exist, cannot assign default theme",
                        properties.getAdmin().getName());
            }
            LOG.info("Initialized theme");
        }
    }
}
