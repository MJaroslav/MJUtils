package io.github.mjaroslav.mjutils.pos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mjaroslav.mjutils.asm.mixin.accessors.*;
import io.github.mjaroslav.sharedjava.tuple.Triplet;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class BlockPos {
    //region Block
    //--------------------------
    public boolean getBlocksMovement(@NotNull Block owner, @NotNull IBlockAccess world,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getBlocksMovement(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public float getBlockHardness(@NotNull Block owner, @NotNull World world,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getBlockHardness(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    @SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(@NotNull Block owner, @NotNull IBlockAccess world,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getMixedBrightnessForBlock(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(@NotNull Block owner, @NotNull IBlockAccess world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        int side) {
        return owner.shouldSideBeRendered(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            side);
    }

    public boolean isBlockSolid(@NotNull Block owner, @NotNull IBlockAccess world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                int side) {
        return owner.isBlockSolid(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), side);
    }

    @SideOnly(Side.CLIENT)
    public @UnknownNullability IIcon getIcon(@NotNull Block owner, @NotNull IBlockAccess world,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                             int meta) {
        return owner.getIcon(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), meta);
    }

    @SuppressWarnings("rawtypes")
    public void addCollisionBoxesToList(@NotNull Block owner, @NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        @NotNull AxisAlignedBB mask, @NotNull List list,
                                        @Nullable Entity collidedEntity) {
        owner.addCollisionBoxesToList(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), mask,
            list, collidedEntity);
    }

    public @Nullable AxisAlignedBB getCollisionBoundingBoxFromPool(@NotNull Block owner, @NotNull World world,
                                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                                       ? extends Number> pos) {
        return owner.getCollisionBoundingBoxFromPool(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    @SideOnly(Side.CLIENT)
    public @NotNull AxisAlignedBB getSelectedBoundingBoxFromPool(@NotNull Block owner, @NotNull World world,
                                                                 @NotNull Triplet<? extends Number, ? extends Number,
                                                                     ? extends Number> pos) {
        return owner.getSelectedBoundingBoxFromPool(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void updateTick(@NotNull Block owner, @NotNull World world,
                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                           @NotNull Random rand) {
        owner.updateTick(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), rand);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(@NotNull Block owner, @NotNull World world,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                  @NotNull Random rand) {
        owner.randomDisplayTick(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), rand);
    }

    public void onNeighborBlockChange(@NotNull Block owner, @NotNull World world,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                      @NotNull Block block) {
        owner.onNeighborBlockChange(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block);
    }

    public void onBlockAdded(@NotNull Block owner, @NotNull World world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.onBlockAdded(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void breakBlock(@NotNull Block owner, @NotNull World world,
                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                           @NotNull Block block, int meta) {
        owner.breakBlock(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block, meta);
    }

    public float getPlayerRelativeBlockHardness(@NotNull Block owner, @NotNull EntityPlayer player,
                                                @NotNull World world, @NotNull Triplet<? extends Number,
        ? extends Number, ? extends Number> pos) {
        return owner.getPlayerRelativeBlockHardness(player, world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void dropBlockAsItem(@NotNull Block owner, @NotNull World world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos, int meta,
                                int fortune) {
        owner.dropBlockAsItem(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), meta,
            fortune);
    }

    public void dropBlockAsItemWithChance(@NotNull Block owner, @NotNull World world,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                          int meta, float chance, int fortune) {
        owner.dropBlockAsItemWithChance(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            meta, chance, fortune);
    }

    public void dropBlockAsItem(@NotNull Block owner, @NotNull World world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                @NotNull ItemStack stack) {
        ((AccessorBlock) owner).callDropBlockAsItem(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), stack);
    }

    public void dropXpOnBlockBreak(@NotNull Block owner, @NotNull World world, @NotNull Triplet<? extends Number,
        ? extends Number, ? extends Number> pos, int xp) {
        owner.dropXpOnBlockBreak(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), xp);
    }

    public MovingObjectPosition collisionRayTrace(@NotNull Block owner, @NotNull World world,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos, @NotNull Vec3 startVec,
                                                  @NotNull Vec3 endVec) {
        return owner.collisionRayTrace(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            startVec, endVec);
    }

    public void onBlockDestroyedByExplosion(@NotNull Block owner, @NotNull World world,
                                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                            @NotNull Explosion explosion) {
        owner.onBlockDestroyedByExplosion(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            explosion);
    }

    public boolean canReplace(@NotNull Block owner, @NotNull World world,
                              @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                              @NotNull ForgeDirection side, @NotNull ItemStack stack) {
        return owner.canReplace(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            side.ordinal(), stack);
    }

    public boolean canPlaceBlockOnSide(@NotNull Block owner, @NotNull World world,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                       @NotNull ForgeDirection side) {
        return owner.canPlaceBlockOnSide(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            side.ordinal());
    }

    public boolean canPlaceBlockAt(@NotNull Block owner, @NotNull World world,
                                   @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canPlaceBlockAt(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean onBlockActivated(@NotNull Block owner, @NotNull World world,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                    @NotNull EntityPlayer player, @NotNull ForgeDirection side,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> hit) {
        return owner.onBlockActivated(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            player, side.ordinal(), hit.getX().floatValue(), hit.getY().floatValue(), hit.getZ().floatValue());
    }

    public void onEntityWalking(@NotNull Block owner, @NotNull World world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                @NotNull Entity entity) {
        owner.onEntityWalking(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), entity);
    }

    public int onBlockPlaced(@NotNull Block owner, @NotNull World world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                             @NotNull ForgeDirection side,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> hit, int meta) {
        return owner.onBlockPlaced(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            side.ordinal(), hit.getX().floatValue(), hit.getY().floatValue(), hit.getZ().floatValue(), meta);
    }

    public void onBlockClicked(@NotNull Block owner, @NotNull World world,
                               @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                               @NotNull EntityPlayer player) {
        owner.onBlockClicked(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), player);
    }

    public void velocityToAddToEntity(@NotNull Block owner, @NotNull World world,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                      @NotNull Entity entity,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> velocity) {
        owner.velocityToAddToEntity(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            entity, Vec3.createVectorHelper(velocity.getX().doubleValue(), velocity.getY().doubleValue(),
                velocity.getZ().doubleValue()));
    }

    public void setBlockBoundsBasedOnState(@NotNull Block owner, @NotNull IBlockAccess world,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.setBlockBoundsBasedOnState(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(@NotNull Block owner, @NotNull IBlockAccess world,
                               @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.colorMultiplier(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int isProvidingWeakPower(@NotNull Block owner, @NotNull IBlockAccess world,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                    @NotNull ForgeDirection side) {
        return owner.isProvidingWeakPower(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            side.ordinal());
    }

    public void onEntityCollidedWithBlock(@NotNull Block owner, @NotNull World world,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                          @NotNull Entity entity) {
        owner.onEntityCollidedWithBlock(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            entity);
    }

    public int isProvidingStrongPower(@NotNull Block owner, @NotNull IBlockAccess world,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                      @NotNull ForgeDirection side) {
        return owner.isProvidingStrongPower(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            side.ordinal());
    }

    public void harvestBlock(@NotNull Block owner, @NotNull World world, @NotNull EntityPlayer player,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos, int meta) {
        owner.harvestBlock(world, player, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), meta);
    }

    public boolean canBlockStay(@NotNull Block owner, @NotNull World world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canBlockStay(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void onBlockPlacedBy(@NotNull Block owner, @NotNull World world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                @NotNull EntityLivingBase placer, @NotNull ItemStack stack) {
        owner.onBlockPlacedBy(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), placer,
            stack);
    }

    public void onPostBlockPlaced(@NotNull Block owner, @NotNull World world,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos, int meta) {
        owner.onPostBlockPlaced(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), meta);
    }

    public boolean onBlockEventReceived(@NotNull Block owner, @NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        int event, int parameter) {
        return owner.onBlockEventReceived(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            event, parameter);
    }

    public void onFallenUpon(@NotNull Block owner, @NotNull World world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                             @NotNull Entity entity, float distance) {
        owner.onFallenUpon(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), entity,
            distance);
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(@NotNull Block owner, @NotNull World world,
                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getItem(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int getDamageValue(@NotNull Block owner, @NotNull World world,
                              @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getDamageValue(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void onBlockHarvested(@NotNull Block owner, @NotNull World world,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos, int meta,
                                 @NotNull EntityPlayer harvester) {
        owner.onBlockHarvested(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), meta,
            harvester);
    }

    public void onBlockPreDestroy(@NotNull Block owner, @NotNull World world,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                  int meta) {
        owner.onBlockPreDestroy(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), meta);
    }

    public void fillWithRain(@NotNull Block owner, @NotNull World world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.fillWithRain(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int getComparatorInputOverride(@NotNull Block owner, @NotNull World world,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                          @NotNull ForgeDirection side) {
        return owner.getComparatorInputOverride(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), side.ordinal());
    }

    public int getLightValue(@NotNull Block owner, @NotNull IBlockAccess world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getLightValue(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean isLadder(@NotNull Block owner, @NotNull IBlockAccess world,
                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                            @NotNull EntityLivingBase entity) {
        return owner.isLadder(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), entity);
    }

    public boolean isNormalCube(@NotNull Block owner, @NotNull IBlockAccess world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isNormalCube(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean isSideSolid(@NotNull Block owner, @NotNull IBlockAccess world,
                               @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                               @NotNull ForgeDirection side) {
        return owner.isSideSolid(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), side);
    }

    public boolean isReplaceable(@NotNull Block owner, @NotNull IBlockAccess world,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isReplaceable(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean isBurning(@NotNull Block owner, @NotNull IBlockAccess world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isBurning(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean isAir(@NotNull Block owner, @NotNull IBlockAccess world,
                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isAir(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean removedByPlayer(@NotNull Block owner, @NotNull World world, @NotNull EntityPlayer player,
                                   @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                   boolean willHarvest) {
        return owner.removedByPlayer(world, player, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            willHarvest);
    }

    @Deprecated
    public boolean removedByPlayer(@NotNull Block owner, @NotNull World world, EntityPlayer player,
                                   @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.removedByPlayer(world, player, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public int getFlammability(@NotNull Block owner, @NotNull IBlockAccess world,
                               @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                               @NotNull ForgeDirection face) {
        return owner.getFlammability(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), face);
    }

    public boolean isFlammable(@NotNull Block owner, @NotNull IBlockAccess world,
                               @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                               @NotNull ForgeDirection face) {
        return owner.isFlammable(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), face);
    }

    public int getFireSpreadSpeed(@NotNull Block owner, @NotNull IBlockAccess world,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                  @NotNull ForgeDirection face) {
        return owner.getFireSpreadSpeed(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            face);
    }

    public boolean isFireSource(@NotNull Block owner, @NotNull World world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                @NotNull ForgeDirection side) {
        return owner.isFireSource(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), side);
    }

    public ArrayList<ItemStack> getDrops(@NotNull Block owner, @NotNull World world,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                         int metadata, int fortune) {
        return owner.getDrops(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), metadata,
            fortune);
    }

    public boolean canSilkHarvest(@NotNull Block owner, @NotNull World world, @NotNull EntityPlayer player,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                  int metadata) {
        return owner.canSilkHarvest(world, player, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            metadata);
    }

    public boolean canCreatureSpawn(@NotNull Block owner, @NotNull EnumCreatureType type, @NotNull IBlockAccess world,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canCreatureSpawn(type, world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean isBed(@NotNull Block owner, @NotNull IBlockAccess world,
                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                         @NotNull EntityLivingBase player) {
        return owner.isBed(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), player);
    }

    public @Nullable Pos getBedSpawnPosition(@NotNull Block owner, @NotNull IBlockAccess world,
                                             @NotNull Triplet<? extends Number, ? extends Number,
                                                 ? extends Number> pos,
                                             @NotNull EntityPlayer player) {
        val coordinates = owner.getBedSpawnPosition(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), player);
        if (coordinates != null) return Pos.of(coordinates);
        return null;
    }

    public void setBedOccupied(@NotNull Block owner, @NotNull IBlockAccess world,
                               @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                               @NotNull EntityPlayer player, boolean occupied) {
        owner.setBedOccupied(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), player,
            occupied);
    }

    public int getBedDirection(@NotNull Block owner, @NotNull IBlockAccess world,
                               @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getBedDirection(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean isBedFoot(@NotNull Block owner, @NotNull IBlockAccess world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isBedFoot(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void beginLeavesDecay(@NotNull Block owner, @NotNull World world,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.beginLeavesDecay(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean canSustainLeaves(@NotNull Block owner, @NotNull IBlockAccess world,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canSustainLeaves(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean isLeaves(@NotNull Block owner, @NotNull IBlockAccess world,
                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isLeaves(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean canBeReplacedByLeaves(@NotNull Block owner, @NotNull IBlockAccess world,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canBeReplacedByLeaves(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean isWood(@NotNull Block owner, @NotNull IBlockAccess world,
                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isWood(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean isReplaceableOreGen(@NotNull Block owner, @NotNull World world,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                       @NotNull Block target) {
        return owner.isReplaceableOreGen(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            target);
    }

    public float getExplosionResistance(@NotNull Block owner, @NotNull Entity entity, @NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        @NotNull Triplet<? extends Number, ? extends Number,
                                            ? extends Number> explosionPos) {
        return owner.getExplosionResistance(entity, world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), explosionPos.getX().doubleValue(), explosionPos.getY().doubleValue(),
            explosionPos.getZ().doubleValue());
    }

    public void onBlockExploded(@NotNull Block owner, @NotNull World world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                @NotNull Explosion explosion) {
        owner.onBlockExploded(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), explosion);
    }

    public boolean canConnectRedstone(@NotNull Block owner, @NotNull IBlockAccess world,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                      @NotNull ForgeDirection side) {
        return owner.canConnectRedstone(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            side.ordinal());
    }

    public boolean canPlaceTorchOnTop(@NotNull Block owner, @NotNull World world,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canPlaceTorchOnTop(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public ItemStack getPickBlock(@NotNull Block owner, MovingObjectPosition target, @NotNull World world,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                  @NotNull EntityPlayer player) {
        return owner.getPickBlock(target, world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            player);
    }

    @Deprecated
    public ItemStack getPickBlock(@NotNull Block owner, @NotNull MovingObjectPosition target, @NotNull World world,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getPickBlock(target, world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean isFoliage(@NotNull Block owner, @NotNull IBlockAccess world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isFoliage(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(@NotNull Block owner, @NotNull World world,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                     int meta, @NotNull EffectRenderer effectRenderer) {
        return owner.addDestroyEffects(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), meta,
            effectRenderer);
    }

    public boolean canSustainPlant(@NotNull Block owner, @NotNull IBlockAccess world,
                                   @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                   @NotNull ForgeDirection side, @NotNull IPlantable plantable) {
        return owner.canSustainPlant(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            side, plantable);
    }

    public void onPlantGrow(@NotNull Block owner, @NotNull World world,
                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> sourcePos) {
        owner.onPlantGrow(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            sourcePos.getX().intValue(), sourcePos.getY().intValue(), sourcePos.getZ().intValue());
    }

    public boolean isFertile(@NotNull Block owner, @NotNull World world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isFertile(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int getLightOpacity(@NotNull Block owner, @NotNull IBlockAccess world,
                               @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getLightOpacity(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean canEntityDestroy(@NotNull Block owner, @NotNull IBlockAccess world,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                    @NotNull Entity entity) {
        return owner.canEntityDestroy(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            entity);
    }

    public boolean isBeaconBase(@NotNull Block owner, @NotNull IBlockAccess world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> beaconPos) {
        return owner.isBeaconBase(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            beaconPos.getX().intValue(), beaconPos.getY().intValue(), beaconPos.getZ().intValue());
    }

    public boolean rotateBlock(@NotNull Block owner, @NotNull World world,
                               @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                               @NotNull ForgeDirection axis) {
        return owner.rotateBlock(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), axis);
    }

    public @NotNull ForgeDirection @Nullable [] getValidRotations(@NotNull Block owner, @NotNull World world,
                                                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getValidRotations(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public float getEnchantPowerBonus(@NotNull Block owner, @NotNull World world,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getEnchantPowerBonus(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean recolourBlock(@NotNull Block owner, @NotNull World world,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                 @NotNull ForgeDirection side, int color) {
        return owner.recolourBlock(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), side,
            color);
    }

    public void onNeighborChange(@NotNull Block owner, @NotNull IBlockAccess world,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> tilePos) {
        owner.onNeighborChange(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            tilePos.getX().intValue(), tilePos.getY().intValue(), tilePos.getZ().intValue());
    }

    public boolean shouldCheckWeakPower(@NotNull Block owner, @NotNull IBlockAccess world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        @NotNull ForgeDirection side) {
        return owner.shouldCheckWeakPower(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            side.ordinal());
    }

    public boolean getWeakChanges(@NotNull Block owner, @NotNull IBlockAccess world,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getWeakChanges(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockBasePressurePlate
    //--------------------------
    public int pressurePlate$getRedstonePowerByCollidedEntities(@NotNull BlockBasePressurePlate owner,
                                                                @NotNull World world,
                                                                @NotNull Triplet<? extends Number,
                                                                    ? extends Number, ? extends Number> pos) {
        return ((AccessorBlockBasePressurePlate) owner).getRedstonePowerByCollidedEntities(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }

    public void pressurePlate$updateAndPlayEffects(@NotNull BlockBasePressurePlate owner, @NotNull World world,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos, int meta) {
        ((AccessorBlockBasePressurePlate) owner).updateAndPlayEffects(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), meta);
    }

    public void pressurePlate$notifyBlocksOfNeighborChange(@NotNull BlockBasePressurePlate owner, @NotNull World world,
                                                           @NotNull Triplet<? extends Number, ? extends Number,
                                                               ? extends Number> pos) {
        ((AccessorBlockBasePressurePlate) owner).notifyBlocksOfNeighborChange(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockBed
    //--------------------------
    public void bed$updateBedOccupied(@NotNull World world,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                      boolean occupied) {
        BlockBed.func_149979_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), occupied);
    }

    public @Nullable Pos getBedSpawnPosition(@NotNull World world,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                             int attempts) {
        val coordinates = BlockBed.func_149977_a(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), attempts);
        if (coordinates != null) return Pos.of(coordinates);
        return null;
    }
    //--------------------------
    //endregion

    //region BlockBush
    //--------------------------
    public void bush$checkAndDropBlock(@NotNull BlockBush owner, @NotNull World world,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockBush) owner).callCheckAndDropBlock(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockButton
    //--------------------------
    public int button$getRotatedMetaByNeighbors(@NotNull BlockButton owner, @NotNull World world,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos) {
        return ((AccessorBlockButton) owner).getRotatedMetaByNeighbors(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean button$checkAndDropBlock(@NotNull BlockButton owner, @NotNull World world,
                                            @NotNull Triplet<? extends Number, ? extends Number,
                                                ? extends Number> pos) {
        return ((AccessorBlockButton) owner).checkAndDropBlock(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void button$updateAndPlayEffects(@NotNull BlockButton owner, @NotNull World world,
                                            @NotNull Triplet<? extends Number, ? extends Number,
                                                ? extends Number> pos) {
        ((AccessorBlockButton) owner).updateAndPlayEffects(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void button$notifyBlocksOfNeighborChange(@NotNull BlockButton owner, @NotNull World world,
                                                    @NotNull Triplet<? extends Number, ? extends Number,
                                                        ? extends Number> pos, int meta) {
        ((AccessorBlockButton) owner).notifyBlocksOfNeighborChange(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), meta);
    }
    //--------------------------
    //endregion

    //region BlockCake
    //--------------------------
    public void cake$eatCake(@NotNull BlockCake owner, @NotNull World world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                             @NotNull EntityPlayer eater) {
        ((AccessorBlockCake) owner).eatCake(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            eater);
    }
    //--------------------------
    //endregion

    //region BlockCauldron
    //--------------------------
    public void cauldron$setFluidAmount(@NotNull BlockCauldron owner, @NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        int amount) {
        owner.func_150024_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), amount);
    }
    //--------------------------
    //endregion

    //region BlockChest
    //--------------------------
    public void chest$rotateAndConnect(@NotNull BlockChest owner, @NotNull World world,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockChest) owner).rotateAndConnect(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public boolean chest$isSameBlockAround(@NotNull BlockChest owner, @NotNull World world,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return ((AccessorBlockChest) owner).isSameBlockAround(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public @Nullable IInventory chest$getInventory(@NotNull BlockChest owner, @NotNull World world,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos) {
        return ((AccessorBlockChest) owner).getInventory(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public boolean chest$isOcelotSittingOnChest(@NotNull World world,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos) {
        return AccessorBlockChest.isOcelotSittingOnChest(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockCrops
    //--------------------------
    public float crops$calculateGrowthChance(@NotNull BlockCrops owner, @NotNull World world,
                                             @NotNull Triplet<? extends Number, ? extends Number,
                                                 ? extends Number> pos) {
        return ((AccessorBlockCrops) owner).calculateGrowthChance(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void crops$tryGrowth(@NotNull BlockCrops owner, @NotNull World world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockCrops) owner).tryGrowth(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockDispenser
    //--------------------------
    public void dispenser$rotateBlockToFreeSide(@NotNull BlockDispenser owner, @NotNull World world,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos) {
        ((AccessorBlockDispenser) owner).rotateBlockToFreeSide(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void dispenser$updateAndPlayEffects(@NotNull BlockDispenser owner, @NotNull World world,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos) {
        ((AccessorBlockDispenser) owner).updateAndPlayEffects(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockDoor
    //--------------------------
    public final int DOOR$MASK_DIRECTION = 0b00011;
    public final int DOOR$MASK_OPEN = 0b000100;
    public final int DOOR$MASK_UPPER_BLOCK = 0b01000;
    public final int DOOR$MASK_HINGE = 0b10000;

    public int door$getRotation(@NotNull BlockDoor owner, @NotNull IBlockAccess world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.func_150013_e(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean door$isOpen(@NotNull BlockDoor owner, @NotNull IBlockAccess world,
                               @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.func_150015_f(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void door$setOpen(@NotNull BlockDoor owner, @NotNull World world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                             boolean open) {
        owner.func_150014_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), open);
    }

    public int door$getBits(@NotNull BlockDoor owner, @NotNull IBlockAccess world,
                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.func_150012_g(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockDoublePlant
    //--------------------------
    public boolean doublePlant$dropBlockAndAddStat(@NotNull BlockDoublePlant owner, @NotNull World world,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos, int meta,
                                                   @NotNull EntityPlayer harvester) {
        return ((AccessorBlockDoublePlant) owner).dropBlockAndAddStat(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), meta, harvester);
    }
    //--------------------------
    //endregion

    //region BlockDragonEgg
    //--------------------------
    public void checkAndFall(@NotNull BlockDragonEgg owner, @NotNull World world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockDragonEgg) owner).checkAndFall(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void moveRandomly(@NotNull BlockDragonEgg owner, @NotNull World world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockDragonEgg) owner).moveRandomly(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockDynamicLiquid
    //--------------------------
    public void dynamicLiquid$makeStill(@NotNull BlockDynamicLiquid owner, @NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockDynamicLiquid) owner).makeStill(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void dynamicLiquid$spreadWaterAndDoActions(@NotNull BlockDynamicLiquid owner, @NotNull World world,
                                                      @NotNull Triplet<? extends Number, ? extends Number,
                                                          ? extends Number> pos, int decay) {
        ((AccessorBlockDynamicLiquid) owner).spreadWaterAndDoActions(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), decay);
    }

    public int dynamicLiquid$getDistanceToPit(@NotNull BlockDynamicLiquid owner, @NotNull World world,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos, int distance, int currentDecay) {
        return ((AccessorBlockDynamicLiquid) owner).getDistanceToPit(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), distance, currentDecay);
    }

    public boolean @NotNull [] dynamicLiquid$getFlowDecayDirections(@NotNull BlockDynamicLiquid owner,
                                                                    @NotNull World world,
                                                                    @NotNull Triplet<? extends Number, ? extends Number,
                                                                        ? extends Number> pos) {
        return ((AccessorBlockDynamicLiquid) owner).getFlowDecayDirections(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean dynamicLiquid$canNotFlowThrow(@NotNull BlockDynamicLiquid owner, @NotNull World world,
                                                 @NotNull Triplet<? extends Number, ? extends Number,
                                                     ? extends Number> pos) {
        return ((AccessorBlockDynamicLiquid) owner).canNotFlowThrow(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public int dynamicLiquid$continueFlowDecay(@NotNull BlockDynamicLiquid owner, @NotNull World world,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos, int currentDecay) {
        return ((AccessorBlockDynamicLiquid) owner).continueFlowDecay(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), currentDecay);
    }

    public boolean dynamicLiquid$canFlowThrow(@NotNull BlockDynamicLiquid owner, @NotNull World world,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos) {
        return ((AccessorBlockDynamicLiquid) owner).canFlowThrow(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockFalling
    //--------------------------
    public void falling$tryFall(@NotNull BlockFalling owner, @NotNull World world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockFalling) owner).tryFall(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public boolean falling$canFallOn(@NotNull World world,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return BlockFalling.func_149831_e(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockFarmland
    //--------------------------
    public boolean farmland$isEmpty(@NotNull BlockFarmland owner, @NotNull World world,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return ((AccessorBlockFarmland) owner).isEmpty(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public boolean farmland$isMoistened(@NotNull BlockFarmland owner, @NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return ((AccessorBlockFarmland) owner).isMoistened(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockFire
    //--------------------------
    @Deprecated
    public void fire$tryCatchFire(@NotNull BlockFire owner,
                                  @NotNull World world,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                  int chance, @NotNull Random rand, int meta) {
        ((AccessorBlockFire) owner).callTryCatchFire(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), chance, rand, meta);
    }

    public void fire$tryCatchFire(@NotNull BlockFire owner,
                                  @NotNull World world,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                  int chance, @NotNull Random rand, int meta, @NotNull ForgeDirection side) {
        ((AccessorBlockFire) owner).callTryCatchFire(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), chance, rand, meta, side);
    }

    public boolean fire$canNeighborBurn(@NotNull BlockFire owner, @NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return ((AccessorBlockFire) owner).callCanNeighborBurn(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public int fire$getChanceOfNeighborsEncouragingFire(@NotNull BlockFire owner, @NotNull World world,
                                                        @NotNull Triplet<? extends Number, ? extends Number,
                                                            ? extends Number> pos) {
        return ((AccessorBlockFire) owner).callGetChanceOfNeighborsEncouragingFire(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }

    @Deprecated
    public boolean fire$canBlockCatchFire(@NotNull BlockFire owner, @NotNull IBlockAccess world,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canBlockCatchFire(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    @Deprecated
    public int fire$getChanceToEncourageFire(@NotNull BlockFire owner, @NotNull World world,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                             int oldChance) {
        return owner.func_149846_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            oldChance);
    }

    public boolean fire$canCatchFire(@NotNull BlockFire owner, @NotNull IBlockAccess world,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                     @NotNull ForgeDirection face) {
        return owner.canCatchFire(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), face);
    }

    public int fire$getChanceToEncourageFire(@NotNull BlockFire owner, @NotNull IBlockAccess world,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                             int oldChance, @NotNull ForgeDirection face) {
        return owner.getChanceToEncourageFire(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), oldChance, face);
    }
    //--------------------------
    //endregion

    //region BlockFlowerPot
    //--------------------------
    public @Nullable TileEntityFlowerPot flowerPot$getFlowerPotTileEntity(@NotNull BlockFlowerPot owner,
                                                                          @NotNull World world,
                                                                          @NotNull Triplet<? extends Number,
                                                                              ? extends Number, ? extends Number> pos) {
        return ((AccessorBlockFlowerPot) owner).getFlowerPotTileEntity(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockFurnace
    //--------------------------
    public void furnace$rotateFaceToFreeSide(@NotNull BlockFurnace owner, @NotNull World world,
                                             @NotNull Triplet<? extends Number, ? extends Number,
                                                 ? extends Number> pos) {
        ((AccessorBlockFurnace) owner).rotateFaceToFreeSide(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void furnace$updateFurnaceBlockState(boolean lit, @NotNull World world,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos) {
        BlockFurnace.updateFurnaceBlockState(lit, world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockHopper
    //--------------------------
    public void hopper$updateHopperBlockState(@NotNull BlockHopper owner, @NotNull World world,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos) {
        ((AccessorBlockHopper) owner).updateHopperBlockState(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public TileEntityHopper hopper$getHopperTileEntity(@NotNull IBlockAccess world,
                                                       @NotNull Triplet<? extends Number, ? extends Number,
                                                           ? extends Number> pos) {
        return BlockHopper.func_149920_e(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockJukebox
    //--------------------------
    public void jukebox$setDisc(@NotNull BlockJukebox owner, @NotNull World world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                @Nullable ItemStack discStack) {
        owner.func_149926_b(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), discStack);
    }

    public void jukebox$dropDisc(@NotNull BlockJukebox owner, @NotNull World world,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.func_149925_e(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockLeaves
    //--------------------------
    public void leaves$removeLeaves(@NotNull BlockLeaves owner, @NotNull World world,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockLeaves) owner).callRemoveLeaves(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void leaves$dropExtra(@NotNull BlockLeaves owner, @NotNull World world,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos, int meta,
                                 int chance) {
        ((AccessorBlockLeaves) owner).dropExtra(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), meta, chance);
    }
    //--------------------------
    //endregion

    //region BlockLever
    //--------------------------
    public boolean lever$checkAndDropBlock(@NotNull BlockLever owner, @NotNull World world,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return ((AccessorBlockLever) owner).checkAndDropBlock(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockLiquid
    //--------------------------
    public int liquid$getFlowDecay(@NotNull BlockLiquid owner, @NotNull World world,
                                   @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return ((AccessorBlockLiquid) owner).getFlowDecay(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public int liquid$getEffectiveFlowDecay(@NotNull BlockLiquid owner, @NotNull IBlockAccess world,
                                            @NotNull Triplet<? extends Number, ? extends Number,
                                                ? extends Number> pos) {
        return ((AccessorBlockLiquid) owner).callGetEffectiveFlowDecay(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public @NotNull Pos liquid$getFlowVector(@NotNull BlockLiquid owner, @NotNull IBlockAccess world,
                                             @NotNull Triplet<? extends Number, ? extends Number,
                                                 ? extends Number> pos) {
        return Pos.of(((AccessorBlockLiquid) owner).callGetFlowVector(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue()));
    }

    @SideOnly(Side.CLIENT)
    public double liquid$getFlowDirection(@NotNull IBlockAccess world,
                                          @NotNull Triplet<? extends Number, ? extends Number,
                                              ? extends Number> pos, @NotNull Material material) {
        return BlockLiquid.getFlowDirection(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            material);
    }

    public void liquid$tryFreezeLava(@NotNull BlockLiquid owner, @NotNull World world,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockLiquid) owner).tryFreezeLava(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void liquid$playEffects(@NotNull BlockLiquid owner, @NotNull World world,
                                   @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockLiquid) owner).playEffects(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockMushroom
    //--------------------------
    public boolean mushroom$growthBigMushroom(@NotNull BlockMushroom owner, @NotNull World world,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos, @NotNull Random rand) {
        return owner.func_149884_c(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), rand);
    }
    //--------------------------
    //endregion

    //region BlockPane
    //--------------------------
    public boolean pane$canPaneConnectTo(@NotNull BlockPane owner, @NotNull IBlockAccess world,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                         @NotNull ForgeDirection dir) {
        return owner.canPaneConnectTo(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), dir);
    }
    //--------------------------
    //endregion

    //region BlockPistonBase
    //--------------------------
    public void pistonBase$updatePistonState(@NotNull BlockPistonBase owner, @NotNull World world,
                                             @NotNull Triplet<? extends Number, ? extends Number,
                                                 ? extends Number> pos) {
        ((AccessorBlockPistonBase) owner).callUpdatePistonState(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public boolean pistonBase$isIndirectlyPowered(@NotNull BlockPistonBase owner, @NotNull World world,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos, int orientation) {
        return ((AccessorBlockPistonBase) owner).callIsIndirectlyPowered(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), orientation);
    }

    public int pistonBase$determineOrientation(@NotNull World world,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos, @NotNull EntityLivingBase placer) {
        return BlockPistonBase.determineOrientation(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), placer);
    }

    public boolean pistonBase$canPushBlock(@NotNull Block blockToPush, @NotNull World world,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                           boolean moveFlag) {
        return AccessorBlockPistonBase.callCanPushBlock(blockToPush, world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), moveFlag);
    }

    public boolean pistonBase$canExtend(@NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        int orientation) {
        return AccessorBlockPistonBase.callCanExtend(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), orientation);
    }

    public boolean pistonBase$tryExtend(@NotNull BlockPistonBase owner, @NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        int orientation) {
        return ((AccessorBlockPistonBase) owner).callTryExtend(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), orientation);
    }
    //--------------------------
    //endregion

    //region BlockPistonMoving
    //--------------------------
    public @Nullable TileEntityPiston pistonMoving$getPistonMovingTileEntity(@NotNull BlockPistonMoving owner,
                                                                             @NotNull IBlockAccess world,
                                                                             @NotNull Triplet<? extends Number,
                                                                                 ? extends Number,
                                                                                 ? extends Number> pos) {
        return ((AccessorBlockPistonMoving) owner).getPistonMovingTileEntity(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockPortal
    //--------------------------
    public boolean portal$tryPlacePortal(@NotNull BlockPortal owner, @NotNull World world,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.func_150000_e(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockRailBase
    //--------------------------
    public boolean railBase$isRail(@NotNull World world,
                                   @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return BlockRailBase.func_150049_b_(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void railBase$onRailNeighborBlockChange(@NotNull BlockRailBase owner, @NotNull World world,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos, int meta, int baseMeta,
                                                   @NotNull Block block) {
        ((AccessorBlockRailBase) owner).onRailNeighborBlockChange(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), meta, baseMeta, block);
    }

    public void railBase$updateRailBaseBlockState(@NotNull BlockRailBase owner, @NotNull World world,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos, boolean forceUpdate) {
        ((AccessorBlockRailBase) owner).updateRailBaseBlockState(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), forceUpdate);
    }

    public boolean railBase$isFlexibleRail(@NotNull BlockRailBase owner, @NotNull IBlockAccess world,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isFlexibleRail(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean railBase$canMakeSlopes(@NotNull BlockRailBase owner, @NotNull IBlockAccess world,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canMakeSlopes(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int railBase$getBasicRailMetadata(@NotNull BlockRailBase owner, @NotNull IBlockAccess world,
                                             @Nullable EntityMinecart cart,
                                             @NotNull Triplet<? extends Number, ? extends Number,
                                                 ? extends Number> pos) {
        return owner.getBasicRailMetadata(world, cart, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public float railBase$getRailMaxSpeed(@NotNull BlockRailBase owner, @NotNull World world,
                                          @Nullable EntityMinecart cart,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getRailMaxSpeed(world, cart, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void railBase$onMinecartPass(@NotNull BlockRailBase owner, @NotNull World world,
                                        @NotNull EntityMinecart cart,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.onMinecartPass(world, cart, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockRailDetector
    //--------------------------
    public void railDetector$updateRailDetectorBlockState(@NotNull BlockRailDetector owner, @NotNull World world,
                                                          @NotNull Triplet<? extends Number, ? extends Number,
                                                              ? extends Number> pos, int meta) {
        ((AccessorBlockRailDetector) owner).updateRailDetectorBlockState(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), meta);
    }
    //--------------------------
    //endregion

    //region BlockRailPowered
    //--------------------------
    public boolean railPowered$isNeighborRailsPowered(@NotNull BlockRailPowered owner, @NotNull World world,
                                                      @NotNull Triplet<? extends Number, ? extends Number,
                                                          ? extends Number> pos, int baseMeta, boolean reverse,
                                                      int distance) {
        return ((AccessorBlockRailPowered) owner).isNeighborRailsPowered(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), baseMeta, reverse, distance);
    }

    public boolean railPowered$isRailPowered(@NotNull BlockRailPowered owner, @NotNull World world,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                             boolean reverse, int distance, int baseMeta) {
        return ((AccessorBlockRailPowered) owner).isRailPowered(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), reverse, distance, baseMeta);
    }
    //--------------------------
    //endregion

    //region BlockRedstoneComparator
    //--------------------------
    public int redstoneComparator$getOutputStrength(@NotNull BlockRedstoneComparator owner, @NotNull World world,
                                                    @NotNull Triplet<? extends Number, ? extends Number,
                                                        ? extends Number> pos, int meta) {
        return ((AccessorBlockRedstoneComparator) owner).callGetOutputStrength(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), meta);
    }

    public TileEntityComparator redstoneComparator$getTileEntityComparator(@NotNull BlockRedstoneComparator owner,
                                                                           @NotNull IBlockAccess world,
                                                                           @NotNull Triplet<? extends Number,
                                                                               ? extends Number,
                                                                               ? extends Number> pos) {
        return owner.getTileEntityComparator(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void redstoneComparator$updateRedstoneComparatorBlockState(@NotNull BlockRedstoneComparator owner,
                                                                      @NotNull World world,
                                                                      @NotNull Triplet<? extends Number,
                                                                          ? extends Number, ? extends Number> pos,
                                                                      @NotNull Random rand) {
        ((AccessorBlockRedstoneComparator) owner).updateRedstoneComparatorBlockState(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), rand);
    }
    //--------------------------
    //endregion

    //region BlockRedstineDiode
    //--------------------------
    public void redstoneDiode$onRedstoneDiodeNeighborBlockChange(@NotNull BlockRedstoneDiode owner,
                                                                 @NotNull World world,
                                                                 @NotNull Triplet<? extends Number, ? extends Number,
                                                                     ? extends Number> pos, @NotNull Block block) {
        ((AccessorBlockRedstoneDiode) owner).onRedstoneDiodeNeighborBlockChange(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), block);
    }

    public boolean redstoneDiode$isPowerLocked(@NotNull BlockRedstoneDiode owner, @NotNull IBlockAccess world,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos, int meta) {
        return ((AccessorBlockRedstoneDiode) owner).isPowerLocked(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), meta);
    }

    public boolean redstoneDiode$isGettingInput(@NotNull BlockRedstoneDiode owner, @NotNull World world,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos, int meta) {
        return ((AccessorBlockRedstoneDiode) owner).callIsGettingInput(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), meta);
    }

    public int redstoneDiode$getInputStrength(@NotNull BlockRedstoneDiode owner, @NotNull World world,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos, int side) {
        return ((AccessorBlockRedstoneDiode) owner).callGetInputStrength(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), side);
    }

    public int redstoneDiode$getInputStrengthFromSide(@NotNull BlockRedstoneDiode owner, @NotNull IBlockAccess world,
                                                      @NotNull Triplet<? extends Number, ? extends Number,
                                                          ? extends Number> pos, int meta) {
        return ((AccessorBlockRedstoneDiode) owner).getInputStrengthFromSide(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), meta);
    }

    public int redstoneDiode$getOutputStrengthFrom(@NotNull BlockRedstoneDiode owner, @NotNull IBlockAccess world,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos, int direction) {
        return ((AccessorBlockRedstoneDiode) owner).getOutputStrengthFrom(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), direction);
    }

    public void redstoneDiode$notifyNeighborPowerBlocks(@NotNull BlockRedstoneDiode owner, @NotNull World world,
                                                        @NotNull Triplet<? extends Number, ? extends Number,
                                                            ? extends Number> pos) {
        ((AccessorBlockRedstoneDiode) owner).notifyNeighborPowerBlocks(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }

    public int redstoneDiode$getOutputSignal(@NotNull BlockRedstoneDiode owner, @NotNull IBlockAccess world,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                             int meta) {
        return ((AccessorBlockRedstoneDiode) owner).getOutputSignal(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), meta);
    }

    public boolean redstoneDiode$isPreviousBlockDiode(@NotNull BlockRedstoneDiode owner, @NotNull World world,
                                                      @NotNull Triplet<? extends Number, ? extends Number,
                                                          ? extends Number> pos, int meta) {
        return ((AccessorBlockRedstoneDiode) owner).isPreviousBlockDiode(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), meta);
    }
    //--------------------------
    //endregion

    //region BlockRedstoneOre
    //--------------------------
    public void redstoneOre$activateOre(@NotNull BlockRedstoneOre owner, @NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockRedstoneOre) owner).activateOre(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void redstoneOre$spawnRedstoneParticles(@NotNull BlockRedstoneOre owner, @NotNull World world,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos) {
        ((AccessorBlockRedstoneOre) owner).spawnRedstoneParticles(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockRedstoneTorch
    //--------------------------
    public boolean redstoneTorch$isOverclocking(@NotNull BlockRedstoneTorch owner, @NotNull World world,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos, boolean cache) {
        return ((AccessorBlockRedstoneTorch) owner).isOverclocking(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), cache);
    }

    public boolean redstoneTorch$isSupportBlockPowered(@NotNull BlockRedstoneTorch owner, @NotNull World world,
                                                       @NotNull Triplet<? extends Number, ? extends Number,
                                                           ? extends Number> pos) {
        return ((AccessorBlockRedstoneTorch) owner).isSupportBlockPowered(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockRedstoneWire
    //--------------------------
    public void redstoneWire$updateRedstoneWireBlockStateAndNotifyOfNeighborChange(@NotNull BlockRedstoneWire owner,
                                                                                   @NotNull World world,
                                                                                   @NotNull Triplet<? extends Number,
                                                                                       ? extends Number,
                                                                                       ? extends Number> pos) {
        ((AccessorBlockRedstoneWire) owner).updateRedstoneWireBlockStateAndNotifyOfNeighborChange(world,
            pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int redstoneWire$getMaxPower(@NotNull BlockRedstoneWire owner, @NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        int power) {
        return ((AccessorBlockRedstoneWire) owner).getMaxPower(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), power);
    }

    public boolean redstoneWire$isPowerProviderOrWire(@NotNull IBlockAccess world,
                                                      @NotNull Triplet<? extends Number, ? extends Number,
                                                          ? extends Number> pos, int specialSide) {
        return BlockRedstoneWire.isPowerProviderOrWire(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            specialSide);
    }

    public boolean redstoneWire$isPowerProvidedFromSide(@NotNull IBlockAccess world,
                                                        @NotNull Triplet<? extends Number, ? extends Number,
                                                            ? extends Number> pos, int specialSide) {
        return BlockRedstoneWire.func_150176_g(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), specialSide);
    }
    //--------------------------
    //endregion

    //region BlockReed
    //--------------------------
    public boolean reed$tryDropBlockWhenCantStay(@NotNull BlockReed owner, @NotNull World world,
                                                 @NotNull Triplet<? extends Number, ? extends Number,
                                                     ? extends Number> pos) {
        return ((AccessorBlockReed) owner).tryDropBlockWhenCantStay(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockSapling
    //--------------------------
    public void sapling$growthToNextStage(@NotNull BlockSapling owner, @NotNull World world,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                          @NotNull Random rand) {
        owner.func_149879_c(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), rand);
    }

    public void sapling$growthTree(@NotNull BlockSapling owner, @NotNull World world,
                                   @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                   @NotNull Random rand) {
        owner.func_149878_d(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), rand);
    }

    public boolean sapling$isSameSapling(@NotNull BlockSapling owner, @NotNull World world,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                         int meta) {
        return owner.func_149880_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), meta);
    }
    //--------------------------
    //endregion

    //region BlockSkull
    //--------------------------
    public void skull$trySpawnWither(@NotNull BlockSkull owner, @NotNull World world,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                     @NotNull TileEntitySkull skull) {
        owner.func_149965_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), skull);
    }

    public boolean skull$isSkull(@NotNull BlockSkull owner, @NotNull World world,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                 int skullType) {
        return ((AccessorBlockSkull) owner).isSkull(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), skullType);
    }
    //--------------------------
    //endregion

    //region BlockSnow
    //--------------------------
    public boolean snow$tryRemoveBlockWhenCantStay(@NotNull BlockSnow owner, @NotNull World world,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos) {
        return ((AccessorBlockSnow) owner).tryRemoveBlockWhenCantStay(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockStairs
    //--------------------------
    public void stairs$updateBlockBounds(@NotNull BlockStairs owner, @NotNull IBlockAccess world,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.func_150147_e(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean stairs$isStairWithSameMeta(@NotNull BlockStairs owner, @NotNull IBlockAccess world,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos, int meta) {
        return ((AccessorBlockStairs) owner).isStairWithSameMeta(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), meta);
    }

    public boolean stairs$setUpperHalfBounds(@NotNull BlockStairs owner, @NotNull IBlockAccess world,
                                             @NotNull Triplet<? extends Number, ? extends Number,
                                                 ? extends Number> pos) {
        return owner.func_150145_f(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean stairs$setUpperHalfCournerBounds(@NotNull BlockStairs owner, @NotNull IBlockAccess world,
                                                    @NotNull Triplet<? extends Number, ? extends Number,
                                                        ? extends Number> pos) {
        return owner.func_150144_g(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockStaticLiquid
    //--------------------------
    public void staticLiquid$setNotStationary(@NotNull BlockStaticLiquid owner, @NotNull World world,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos) {
        ((AccessorBlockStaticLiquid) owner).callSetNotStationary(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public boolean staticLiquid$isFlammable(@NotNull BlockStaticLiquid owner, @NotNull World world,
                                            @NotNull Triplet<? extends Number, ? extends Number,
                                                ? extends Number> pos) {
        return ((AccessorBlockStaticLiquid) owner).callIsFlammable(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockStem
    //--------------------------
    public void stem$growthToNextStage(@NotNull BlockStem owner, @NotNull World world,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.func_149874_m(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public float stem$getFruitProduceChance(@NotNull BlockStem owner, @NotNull World world,
                                            @NotNull Triplet<? extends Number, ? extends Number,
                                                ? extends Number> pos) {
        return ((AccessorBlockStem) owner).getFruitProduceChance(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    @SideOnly(Side.CLIENT)
    public int stem$getState(@NotNull BlockStem owner, @NotNull IBlockAccess world,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getState(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockTNT
    //--------------------------
    public void TNT$onActivated(@NotNull BlockTNT owner, @NotNull World world,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos, int meta,
                                @Nullable EntityLivingBase initiator) {
        owner.func_150114_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), meta,
            initiator);
    }
    //--------------------------
    //endregion

    //region BlockTorch
    //--------------------------
    public boolean torch$isLowerBlockAvailableForTorch(@NotNull BlockTorch owner, @NotNull World world,
                                                       @NotNull Triplet<? extends Number, ? extends Number,
                                                           ? extends Number> pos) {
        return ((AccessorBlockTorch) owner).isLowerBlockAvailableForTorch(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean torch$isTorchConnectedCorrectly(@NotNull BlockTorch owner, @NotNull World world,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos, @NotNull Block block) {
        return ((AccessorBlockTorch) owner).isTorchConnectedCorrectly(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), block);
    }

    public boolean torch$tryDropBlockWhenCantStay(@NotNull BlockTorch owner, @NotNull World world,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos) {
        return ((AccessorBlockTorch) owner).tryDropBlockWhenCantStay(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockTrapDoor
    //--------------------------
    public void trapDoor$setOpen(@NotNull BlockTrapDoor owner, @NotNull World world,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                 boolean open) {
        owner.func_150120_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), open);
    }
    //--------------------------
    //endregion

    //region BlockTripWire
    //--------------------------
    public void tripWire$updateTripWireBlockState(@NotNull BlockTripWire owner, @NotNull World world,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos, int meta) {
        ((AccessorBlockTripWire) owner).updateTripWireBlockState(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), meta);
    }

    public void tripWire$triggerTripWire(@NotNull BlockTripWire owner, @NotNull World world,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorBlockTripWire) owner).triggerTripWire(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    @SideOnly(Side.CLIENT)
    public boolean tripWire$isConnectedWithSide(@NotNull IBlockAccess world,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos, int meta, int side) {
        return BlockTripWire.func_150139_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            meta, side);
    }
    //--------------------------
    //endregion

    //region BlockTripWireHook
    //--------------------------
    public void tripWireHook$updateTripWireHookBlockState(@NotNull BlockTripWireHook owner, @NotNull World world,
                                                          @NotNull Triplet<? extends Number, ? extends Number,
                                                              ? extends Number> pos, boolean connected, int meta,
                                                          boolean notify, int wireLength, int side) {
        ((AccessorBlockTripWireHook) owner).updateTripWireHookBlockState(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), connected, meta, notify, wireLength, side);
    }

    public void tripWireHook$playerSoundOnBlockStateChange(@NotNull BlockTripWireHook owner, @NotNull World world,
                                                           @NotNull Triplet<? extends Number, ? extends Number,
                                                               ? extends Number> pos, boolean connected,
                                                           boolean powered, boolean newConnected, boolean newPowered) {
        ((AccessorBlockTripWireHook) owner).playerSoundOnBlockStateChange(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), connected, powered, newConnected, newPowered);
    }

    public void tripWireHook$notifyBlocksOfNeighborChangeBySide(@NotNull BlockTripWireHook owner, @NotNull World world,
                                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                                    ? extends Number> pos, int side) {
        ((AccessorBlockTripWireHook) owner).notifyBlocksOfNeighborChangeBySide(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue(), side);
    }

    public boolean tripWireHook$tryDropBlockWhenCantStay(@NotNull BlockTripWireHook owner, @NotNull World world,
                                                         @NotNull Triplet<? extends Number, ? extends Number,
                                                             ? extends Number> pos) {
        return ((AccessorBlockTripWireHook) owner).tryDropBlockWhenCantStay(world, pos.getX().intValue(),
            pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockVine
    //--------------------------
    public boolean vine$canVineStayAtBlock(@NotNull BlockVine owner, @NotNull World world,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return ((AccessorBlockVine) owner).canVineStayAtBlock(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region BlockWall
    //--------------------------
    public boolean wall$canConnectWallTo(@NotNull BlockWall owner, @NotNull IBlockAccess world,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canConnectWallTo(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region IGrowable
    //--------------------------
    public boolean growable$canGrowthByBoneMeal(@NotNull IGrowable owner, @NotNull World world,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos,
                                                boolean isClient) {
        return owner.func_149851_a(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            isClient);
    }

    public boolean growable$canGrowth(@NotNull IGrowable owner, @NotNull World world, @NotNull Random rand,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.func_149852_a(world, rand, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void growable$growth(@NotNull IGrowable owner, @NotNull World world, @NotNull Random rand,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.func_149853_b(world, rand, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region IPlantable
    //--------------------------
    public @NotNull EnumPlantType plantable$getPlantType(@NotNull IPlantable owner, @NotNull IBlockAccess world,
                                                         @NotNull Triplet<? extends Number, ? extends Number,
                                                             ? extends Number> pos) {
        return owner.getPlantType(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public @NotNull Block plantable$getPlant(@NotNull IPlantable owner, @NotNull IBlockAccess world,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getPlant(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public @Range(from = 0, to = 16) int plantable$getPlantMetadata(@NotNull IPlantable owner,
                                                                    @NotNull IBlockAccess world,
                                                                    @NotNull Triplet<? extends Number, ? extends Number,
                                                                        ? extends Number> pos) {
        return owner.getPlantMetadata(world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion
}
