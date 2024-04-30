package io.github.mjaroslav.mjutils.pos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mjaroslav.mjutils.asm.mixin.accessors.AccessorItemHangingEntity;
import io.github.mjaroslav.mjutils.asm.mixin.accessors.AccessorItemSlab;
import io.github.mjaroslav.sharedjava.tuple.Triplet;
import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

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

    //region ItemBlock
    //--------------------------
    @SideOnly(Side.CLIENT)
    public boolean block$canPlaceBlockOnSide(@NotNull ItemBlock owner, @NotNull World world,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                             int side, @Nullable EntityPlayer placer, @NotNull ItemStack stack) {
        return owner.func_150936_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), side,
            placer, stack);
    }

    public boolean block$placeBlockAt(@NotNull ItemBlock owner, @NotNull ItemStack stack, @Nullable EntityPlayer player,
                                      @NotNull World world,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                      int side,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> hitPos,
                                      int metadata) {
        return owner.placeBlockAt(stack, player, world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), side, hitPos.getX().floatValue(), hitPos.getY().floatValue(),
            hitPos.getZ().floatValue(), metadata);
    }
    //--------------------------
    //endregion

    //region ItemBucket
    //--------------------------
    public boolean bucket$tryPlaceContainedLiquid(@NotNull ItemBucket owner, @NotNull World world,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos) {
        return owner.tryPlaceContainedLiquid(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region ItemDoor
    //--------------------------
    public void door$placeDoorBlock(@NotNull World world,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                    int side, @NotNull Block block) {
        ItemDoor.placeDoorBlock(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), side,
            block);
    }
    //--------------------------
    //endregion

    //region ItemDye
    //--------------------------
    public boolean dye$applyBonemeal(@NotNull ItemStack stack, @NotNull World world,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return ItemDye.func_150919_a(stack, world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean dye$applyBonemeal(@NotNull ItemStack stack, @NotNull World world,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                     @NotNull EntityPlayer player) {
        return ItemDye.applyBonemeal(stack, world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            player);
    }

    @SideOnly(Side.CLIENT)
    public void dye$spawnBonemealParticles(@NotNull World world,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                           int count) {
        ItemDye.func_150918_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), count);
    }
    //--------------------------
    //endregion

    //region ItemHangingEntity
    //--------------------------
    public @Nullable EntityHanging hangingEntity$createHangingEntity(@NotNull ItemHangingEntity owner,
                                                                     @NotNull World world,
                                                                     @NotNull Triplet<? extends Number,
                                                                         ? extends Number, ? extends Number> pos,
                                                                     int direction) {
        return ((AccessorItemHangingEntity) owner).callCreateHangingEntity(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), direction);
    }
    //--------------------------
    //endregion

    //region ItemLead
    //--------------------------
    public boolean lead$tryConnectLeadToFace(@NotNull EntityPlayer leader, @NotNull World world,
                                             @NotNull Triplet<? extends Number, ? extends Number,
                                                 ? extends Number> pos) {
        return ItemLead.func_150909_a(leader, world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region ItemMonsterPlacer
    //--------------------------
    public @Nullable Entity monsterPlacer$spawnCreature(@NotNull World world, int entityId,
                                                        @NotNull Triplet<? extends Number, ? extends Number,
                                                            ? extends Number> pos) {
        return ItemMonsterPlacer.spawnCreature(world, entityId, pos.getX().doubleValue(), pos.getY().doubleValue(),
            pos.getZ().doubleValue());
    }
    //--------------------------
    //endregion

    //region ItemSlab
    //--------------------------
    public boolean slab$tryCombineTwoSlabs(@NotNull ItemSlab owner, @NotNull ItemStack stack,
                                           @Nullable EntityPlayer placer, @NotNull World world,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                           int direction) {
        return ((AccessorItemSlab) owner).tryCombineTwoSlabs(stack, placer, world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), direction);
    }
    //--------------------------
    //endregion

    //region ItemStack
    //--------------------------
    public boolean stack$tryPlaceItemIntoWorld(@NotNull ItemStack owner, @NotNull EntityPlayer placer,
                                               @NotNull World world,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos, int side,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> hitPos) {
        return owner.tryPlaceItemIntoWorld(placer, world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), side, hitPos.getX().floatValue(), hitPos.getY().floatValue(),
            hitPos.getZ().floatValue());
    }

    public void stack$onBlockDestroyed(@NotNull ItemStack owner, @NotNull World world, @NotNull Block block,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                       @NotNull EntityPlayer destroyer) {
        owner.func_150999_a(world, block, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            destroyer);
    }
    //--------------------------
    //endregion

    //region IShearable
    //--------------------------
    public boolean shearable$isShearable(@NotNull IShearable owner, @NotNull ItemStack item,
                                         @NotNull IBlockAccess world,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isShearable(item, world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public @Nullable ArrayList<ItemStack> shearable$onSheared(@NotNull IShearable owner, @NotNull ItemStack item,
                                                              @NotNull IBlockAccess world,
                                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                                  ? extends Number> pos, int fortune) {
        return owner.onSheared(item, world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            fortune);
    }
    //--------------------------
    //endregion
}
