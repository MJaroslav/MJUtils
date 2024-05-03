package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.projectile.EntityFireball;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityFireball.class)
public interface AccessorEntityFireball {
    @Accessor("field_145795_e")
    int getTileX();

    @Accessor("field_145793_f")
    int getTileY();

    @Accessor("field_145794_g")
    int getTileZ();

    @Accessor("field_145795_e")
    void setTileX(int tileX);

    @Accessor("field_145793_f")
    void setTileY(int tileY);

    @Accessor("field_145794_g")
    void setTileZ(int tileZ);
}
