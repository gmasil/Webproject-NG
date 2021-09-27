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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import de.gmasil.webproject.service.dataimport.CleanFinishedEvent;

@Service
public class InitializeService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private InitializeProperties properties;

    @Autowired
    private InitializeAdminService adminService;

    @Autowired
    private InitializeThemeService themeService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @EventListener(CleanFinishedEvent.class)
    public void initialization() {
        if (!properties.isSkip()) {
            StopWatch watch = new StopWatch();
            watch.start();
            adminService.initAdminUser();
            themeService.initTheme();
            watch.stop();
            LOG.info("Initialized application in {}s", watch.getTotalTimeSeconds());
        }
        eventPublisher.publishEvent(new InitializeFinishedEvent(this));
    }
}
