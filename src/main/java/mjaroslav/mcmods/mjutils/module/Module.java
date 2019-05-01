package mjaroslav.mcmods.mjutils.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker for modules that need to be loaded.
 * Use with {@link ModuleSystem}. Used only on
 * classes that implement {@link Modular}.
 * All instances of modules are created in
 * {@link cpw.mods.fml.common.event.FMLConstructionEvent
 * FMLConstructionEvent}!
 *
 * @see ModuleSystem
 * @see Modular
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {
    /**
     * Mod ID. Should not be null. Other mod IDs
     * will cause this module to be ignored by
     * the correct {@link ModuleSystem}.
     *
     * @return Mod owner ID.
     */
    String value();
}