package se.simjarr.global;

import com.vaadin.server.VaadinService;

import javax.servlet.http.Cookie;

public abstract class Cookies {

    public static void createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        VaadinService.getCurrentResponse().addCookie(cookie);
    }

    public static Cookie getCookieByName(String name) {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    public static final String LEAGUE_COOKIE = "LEAGUE";
}
