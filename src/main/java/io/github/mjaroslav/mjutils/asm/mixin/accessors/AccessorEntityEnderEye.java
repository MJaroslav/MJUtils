package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.item.EntityEnderEye;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityEnderEye.class)
public interface AccessorEntityEnderEye {
    @Accessor
    double getTargetX();

    @Accessor
    double getTargetY();

    @Accessor
    double getTargetZ();

    @Accessor
    void setTargetX(double targetX);

    @Accessor
    void setTargetY(double targetY);

    @Accessor
    void setTargetZ(double targetZ);
}
