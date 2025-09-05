package com.gearit.common.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class HttpCookieUtils {

    public static void setHttpCookie(
            HttpServletResponse response,
            CookieObjects.CookieSettings cookieSettings
    ) {
        Cookie cookie = new Cookie(cookieSettings.getName(), cookieSettings.value());
        cookie.setPath(cookieSettings.getPath());
        cookie.setMaxAge(cookieSettings.getMaxAge());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
