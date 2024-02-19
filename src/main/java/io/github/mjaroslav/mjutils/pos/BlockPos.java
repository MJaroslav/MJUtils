package io.github.mjaroslav.mjutils.pos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mjaroslav.mjutils.asm.mixin.accessors.*;
import io.github.mjaroslav.sharedjava.tuple.Triplet;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.block.*;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

// И так сойдет
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
        ((AccessorBlock) owner).dropBlockAsItem(world, pos.getX().intValue(), pos.getY().intValue(),
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
        ((AccessorBlockBush) owner).checkAndDropBlock(world, pos.getX().intValue(), pos.getY().intValue(),
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

