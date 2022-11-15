package io.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * You should mark all your modules with this annotation.
 * All methods with name 'listen' and any {@link cpw.mods.fml.common.event.FMLEvent FMLEvent}
 * in parameter will be called by loader on this event.
 * <br>
 * See also {@link ModuleLoader}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubscribeModule {
    /**
     * Mod ID for this module. As default value used an activeModContainer mod id.
     * <br>
     * Make sure for your jar must contain only one mod, if used default value.
     *
     * @return Mod id or empty string for default value.
     */
    @NotNull String value() default "";

    /**
     * List of required mods ids for this module.
     * If at least one of these is not in mod pack then module not be loaded.
     *
     * @return Array of mods IDs or empty array if this option not needed.
     */
    @NotNull String @NotNull [] modDependencies() default {};

    /**
     * Module loading priority, loading working from smallest to largest.
     * <br>
     * Recommendations: use 0 for blocks and items registering,
     * 1 for crafts and OreDicts, 3 for world things, 10+ for mod compatability
     * modules (don't forget about 'after' in {@link Mod#dependencies()} if it needed)
     * and {@link Proxy} priority will auto sets to {@link Integer#MAX_VALUE}.
     *
     * @return Module load priority, 0 used as default value.
     */
    int priority() default 0;

    /**
     * {@link ModState} for class loading, module class not be loaded before this state.
     * Note that all not loaded modules will be loaded on
     * {@link cpw.mods.fml.common.event.FMLInterModComms.IMCEvent IMCEvent} event.
     *
     * @return Any state which possible at loading or {@link ModState#CONSTRUCTED CONSTRUCTED} as default.
     */
    @NotNull ModState loadOn() default ModState.CONSTRUCTED;
}
