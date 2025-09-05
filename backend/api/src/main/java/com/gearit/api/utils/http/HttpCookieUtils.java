package com.gearit.api.utils.http;

import com.gearit.api.constants.http.CookieObjects.CookieSettings;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class HttpCookieUtils {

    public static void setHttpCookie(HttpServletResponse response, CookieSettings cookieSettings) {
        Cookie cookie = new Cookie(cookieSettings.getName(), cookieSettings.value());
        cookie.setPath(cookieSettings.getPath());
        cookie.setMaxAge(cookieSettings.getMaxAge());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
