package mjaroslav.mcmods.mjutils.lib.module;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigCategory {
    String modid();
    
    String name();
    
    String comment() default "No description.";
    
    boolean requiresWorldRestart() default false;
    
    boolean requiresMcRestart() default false;
}
