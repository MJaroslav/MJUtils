package io.github.mjaroslav.mjutils.util.net;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.net.URI;

@Log4j2
@UtilityClass
public class UtilsDesktop {
    public void openURL(URI url) {
        try {
            val clazz = Class.forName("java.awt.Desktop");
            val object = clazz.getMethod("getDesktop", new Class[0]).invoke(null);
            clazz.getMethod("browse", new Class[]{URI.class}).invoke(object, url);
        } catch (Throwable throwable) {
            log.error("Couldn't open link", throwable);
        }
    }
}
