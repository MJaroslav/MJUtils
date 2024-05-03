package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.projectile.EntityArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityArrow.class)
public interface AccessorEntityArrow {
    @Accessor("field_145791_d")
    int getTileX();

    @Accessor("field_145792_e")
    int getTileY();

    @Accessor("field_145789_f")
    int getTileZ();

    @Accessor("field_145791_d")
    void setTileX(int tileX);

    @Accessor("field_145792_e")
    void setTileY(int tileY);

    @Accessor("field_145789_f")
    void setTileZ(int tileZ);
}
