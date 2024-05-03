package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.projectile.EntityFishHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityFishHook.class)
public interface AccessorEntityFishHook {
    @Accessor("field_146037_g")
    int getTileX();

    @Accessor("field_146048_h")
    int getTileY();

    @Accessor("field_146050_i")
    int getTileZ();

    @Accessor("field_146037_g")
    void setTileX(int tileX);

    @Accessor("field_146048_h")
    void setTileY(int tileY);

    @Accessor("field_146050_i")
    void setTileZ(int tileZ);
}
