package com.gearit.common.http.cookie;

public class CookieObjects {

    public interface CookieSettings {
        String getName();

        String value();

        String getPath();

        int getMaxAge();
    }

    public record RefreshCookie(String value) implements CookieSettings {

        public static final String NAME = "refreshToken";

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public String getPath() {
            return "/api/v1/auth/token/refresh";
        }

        @Override
        public int getMaxAge() {
            return 7 * 24 * 60 * 60;
        }
    }
}
