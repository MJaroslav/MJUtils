package io.github.mjaroslav.mjutils.asm.mixin;

import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoadController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = FMLModContainer.class, remap = false)
public interface AccessorFMLModContainer {
    @Accessor("controller")
    LoadController getController();
}
