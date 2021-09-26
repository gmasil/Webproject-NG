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
package de.gmasil.webproject.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.service.UserProvider;

@RestController
@RequestMapping("/api/users")
public class CurrentUserProxyController {

    @Autowired
    private Environment environment;

    @Autowired
    private UserProvider userProvider;

    private int port = -1;

    @EventListener(ApplicationReadyEvent.class)
    public void setupPort() {
        port = Integer.parseInt(environment.getProperty("local.server.port"));
    }

    @GetMapping({ "/current", "/current/{param}" })
    public ResponseEntity<String> currentUser(@PathVariable(required = false) Optional<String> param,
            @RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request)
            throws URISyntaxException {

        if (port == -1) {
            throw new IllegalStateException("Proxy port could not be initialized");
        }
        User user = userProvider.getCurrent();
        if (user == null) {
            throw new IllegalStateException("User api call without authorized user");
        }

        URI uri = new URI("http", null, "127.0.0.1", port, null, null, null);
        String path = "/api/users/" + user.getId();
        if (param.isPresent()) {
            path += "/" + param.get();
        }

        uri = UriComponentsBuilder.fromUri(uri).path(path).query(request.getQueryString()).build(true).toUri();

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.set(headerName, request.getHeader(headerName));
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, method, httpEntity, String.class);
            return modify(responseEntity, user.getId());
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        }
    }

    private ResponseEntity<String> modify(ResponseEntity<String> responseEntity, long id) {
        String body = responseEntity.getBody();
        if (body == null) {
            return responseEntity;
        }
        body = body.replace("/api/users/" + id, "/api/users/current");
        return ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(body);
    }
}
