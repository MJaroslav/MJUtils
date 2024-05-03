package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecart;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityMinecart.class)
public interface AccessorEntityMinecart {
    @Accessor
    double getMinecartX();

    @Accessor
    double getMinecartY();

    @Accessor
    double getMinecartZ();

    @Accessor
    void setMinecartX(double minecartX);

    @Accessor
    void setMinecartY(double minecartY);

    @Accessor
    void setMinecartZ(double minecartZ);

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

    @Invoker("func_145821_a")
    void updateMovementOnRail(int x, int y, int z, double speed, double slopeAdjustment, @NotNull Block rail,
                              int railMeta);
}
