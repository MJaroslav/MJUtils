package com.github.mjaroslav.mjutils.util.net;

import lombok.extern.log4j.Log4j2;

import java.net.URI;

@Log4j2
public class UtilsNet {
    public static void openURL(URI url) {
        try {
            Class<?> clazz = Class.forName("java.awt.Desktop");
            Object object = clazz.getMethod("getDesktop", new Class[0]).invoke(null);
            clazz.getMethod("browse", new Class[] {URI.class}).invoke(object, url);
        } catch (Throwable throwable) {
            log.error("Couldn't open link", throwable);
        }
    }
}
