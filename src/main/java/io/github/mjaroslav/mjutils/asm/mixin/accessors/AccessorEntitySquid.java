package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.passive.EntitySquid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntitySquid.class)
public interface AccessorEntitySquid {
    @Accessor
    float getRandomMotionVecX();

    @Accessor
    float getRandomMotionVecY();

    @Accessor
    float getRandomMotionVecZ();

    @Accessor
    void setRandomMotionVecX(float randomMotionVecX);

    @Accessor
    void setRandomMotionVecY(float randomMotionVecY);

    @Accessor
    void setRandomMotionVecZ(float randomMotionVecZ);
}
