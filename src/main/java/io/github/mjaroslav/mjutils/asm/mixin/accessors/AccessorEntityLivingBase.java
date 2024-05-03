package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityLivingBase.class)
public interface AccessorEntityLivingBase {
    @Accessor
    double getNewPosX();

    @Accessor
    double getNewPosY();

    @Accessor
    double getNewPosZ();

    @Accessor
    void setNewPosX(double newPosX);

    @Accessor
    void setNewPosY(double newPosY);

    @Accessor
    void setNewPosZ(double newPosZ);
}
