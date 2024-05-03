package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.projectile.EntityThrowable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityThrowable.class)
public interface AccessorEntityThrowable {
    @Accessor("field_145788_c")
    int getTileX();

    @Accessor("field_145786_d")
    int getTileY();

    @Accessor("field_145787_e")
    int getTileZ();

    @Accessor("field_145788_c")
    void setTileX(int tileX);

    @Accessor("field_145786_d")
    void setTileY(int tileY);

    @Accessor("field_145787_e")
    void setTileZ(int tileZ);
}
