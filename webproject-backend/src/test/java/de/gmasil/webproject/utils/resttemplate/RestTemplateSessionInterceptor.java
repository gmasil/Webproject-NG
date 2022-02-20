/*
 * Webproject NG
 * Copyright © 2022 Gmasil
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
package de.gmasil.webproject.utils.resttemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class RestTemplateSessionInterceptor implements ClientHttpRequestInterceptor {

    private Map<String, String> cookies = new HashMap<>();

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        if (!cookies.isEmpty()) {
            request.getHeaders().add(HttpHeaders.COOKIE, createCookieHeader());
        }
        ClientHttpResponse response = execution.execute(request, body);
        List<String> setCookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (setCookies != null) {
            setCookies.forEach(this::addCookie);
        }
        return response;
    }

    private String createCookieHeader() {
        return cookies.entrySet().stream() //
                .map(entry -> entry.getKey() + "=" + entry.getValue()) //
                .collect(Collectors.joining("; "));
    }

    private void addCookie(String cookie) {
        String keyValue = cookie.split(";")[0];
        String[] data = keyValue.split("=");
        if (data.length > 1) {
            cookies.put(data[0], data[1]);
        }
    }
}
