package io.github.mjaroslav.mjutils.pos;

import io.github.mjaroslav.sharedjava.tuple.Triplet;
import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ItemPos {
    //region Item
    //-------------------------
    public boolean onItemUse(@NotNull Item owner, @NotNull ItemStack stack, @NotNull EntityPlayer user,
                             @NotNull World world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                             @NotNull ForgeDirection side,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> hitPos) {
        return owner.onItemUse(stack, user, world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            side.ordinal(), hitPos.getX().floatValue(), hitPos.getY().floatValue(), hitPos.getZ().floatValue());
    }

    public boolean onBlockDestroyed(@NotNull Item owner, @NotNull ItemStack stack, @NotNull World world,
                                    @NotNull Block block,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                    @NotNull EntityLivingBase initiator) {
        return owner.onBlockDestroyed(stack, world, block, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), initiator);
    }

    public boolean onItemUseFirst(@NotNull Item owner, @NotNull ItemStack stack, @NotNull EntityPlayer user,
                                  @NotNull World world,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                  @NotNull ForgeDirection side,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> hitPos) {
        return owner.onItemUseFirst(stack, user, world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), side.ordinal(), hitPos.getX().floatValue(), hitPos.getY().floatValue(),
            hitPos.getZ().floatValue());
    }

    public boolean onBlockStartBreak(@NotNull Item owner, @NotNull ItemStack stack,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                     @NotNull EntityPlayer breaker) {
        return owner.onBlockStartBreak(stack, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            breaker);
    }

    public boolean doesSneakBypassUse(@NotNull Item owner, @NotNull World world,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                      @NotNull EntityPlayer user) {
        return owner.doesSneakBypassUse(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            user);
    }
    //-------------------------
    //endregion

}
