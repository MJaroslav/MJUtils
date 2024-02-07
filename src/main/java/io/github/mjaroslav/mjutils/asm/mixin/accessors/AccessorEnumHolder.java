package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import cpw.mods.fml.common.discovery.asm.ModAnnotation.EnumHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = EnumHolder.class, remap = false)
public interface AccessorEnumHolder {
    @Accessor("desc")
    String getDesc();

    @Accessor("value")
    String getValue();
}
