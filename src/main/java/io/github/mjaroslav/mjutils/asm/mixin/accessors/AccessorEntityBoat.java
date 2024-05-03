package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityBoat.class)
public interface AccessorEntityBoat {
    @Accessor
    double getBoatX();

    @Accessor
    double getBoatY();

    @Accessor
    double getBoatZ();

    @Accessor
    void setBoatX(double boatX);

    @Accessor
    void setBoatY(double boatY);

    @Accessor
    void setBoatZ(double boatZ);

    @SideOnly(Side.CLIENT)
    @Accessor
    double getVelocityX();

    @SideOnly(Side.CLIENT)
    @Accessor
    double getVelocityY();

    @SideOnly(Side.CLIENT)
    @Accessor
    double getVelocityZ();

    @SideOnly(Side.CLIENT)
    @Accessor
    void setVelocityX(double velocityX);

    @SideOnly(Side.CLIENT)
    @Accessor
    void setVelocityY(double velocityY);

    @SideOnly(Side.CLIENT)
    @Accessor
    void setVelocityZ(double velocityZ);
}
