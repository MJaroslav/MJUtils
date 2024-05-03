package io.github.mjaroslav.mjutils.pos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mjaroslav.mjutils.asm.mixin.accessors.*;
import io.github.mjaroslav.sharedjava.tuple.Triplet;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class EntityPos {
    //region Entity
    //--------------------------
    @Contract(value = "_ -> new")
    public @NotNull Pos getPrevPos(@NotNull Entity owner) {
        return Pos.of(owner.prevPosX, owner.prevPosY, owner.prevPosZ);
    }

    public void setPrevPos(@NotNull Entity owner,
                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> prevPos) {
        owner.prevPosX = prevPos.getX().doubleValue();
        owner.prevPosY = prevPos.getY().doubleValue();
        owner.prevPosZ = prevPos.getZ().doubleValue();
    }

    @Contract(value = "_ -> new")
    public @NotNull Pos getPos(@NotNull Entity owner) {
        return Pos.of(owner.posX, owner.posY, owner.posZ);
    }

    public void setPos(@NotNull Entity owner,
                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.posX = pos.getX().doubleValue();
        owner.posY = pos.getY().doubleValue();
        owner.posZ = pos.getZ().doubleValue();
    }

    @Contract(value = "_ -> new")
    public @NotNull Pos getMotion(@NotNull Entity owner) {
        return Pos.of(owner.motionX, owner.motionY, owner.motionZ);
    }

    public void setMotion(@NotNull Entity owner,
                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> motion) {
        owner.motionX = motion.getX().doubleValue();
        owner.motionY = motion.getY().doubleValue();
        owner.motionZ = motion.getZ().doubleValue();
    }

    @Contract(value = "_ -> new")
    public @NotNull Pos getLastTickPos(@NotNull Entity owner) {
        return Pos.of(owner.lastTickPosX, owner.lastTickPosY, owner.lastTickPosZ);
    }

    @Contract(value = "_ -> new")
    public @NotNull Pos getChunkCoord(@NotNull Entity owner) {
        return Pos.of(owner.chunkCoordX, owner.chunkCoordY, owner.chunkCoordZ);
    }

    @Contract(value = "_ -> new")
    @SideOnly(Side.CLIENT)
    public @NotNull Pos getServerPos(@NotNull Entity owner) {
        return Pos.of(owner.serverPosX, owner.serverPosY, owner.serverPosZ);
    }

    public void setPosition(@NotNull Entity owner,
                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.setPosition(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
    }

    public boolean isOffsetPositionInLiquid(@NotNull Entity owner,
                                            @NotNull Triplet<? extends Number, ? extends Number,
                                                ? extends Number> pos) {
        return owner.isOffsetPositionInLiquid(pos.getX().doubleValue(), pos.getY().doubleValue(),
            pos.getZ().doubleValue());
    }

    public void moveEntity(@NotNull Entity owner,
                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.moveEntity(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
    }

    public void playStepSound(@NotNull Entity owner,
                              @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                              @NotNull Block block) {
        ((AccessorEntity) owner).playStepSound(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            block);
    }

    public void moveFlying(@NotNull Entity owner,
                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.moveFlying(pos.getX().floatValue(), pos.getY().floatValue(), pos.getZ().floatValue());
    }

    public void setPositionAndRotation(@NotNull Entity owner,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                       float yaw, float pitch) {
        owner.setPositionAndRotation(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(), yaw,
            pitch);
    }

    public void setLocationAndAngles(@NotNull Entity owner,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                     float yaw, float pitch) {
        owner.setLocationAndAngles(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(), yaw,
            pitch);
    }

    public double getDistanceSq(@NotNull Entity owner,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getDistanceSq(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
    }

    public double getDistance(@NotNull Entity owner,
                              @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getDistance(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
    }

    public void addVelocity(@NotNull Entity owner,
                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.addVelocity(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRender3d(@NotNull Entity owner,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isInRangeToRender3d(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(@NotNull Entity owner,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        float yaw, float pitch, int rotationIncrement) {
        owner.setPositionAndRotation2(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(),
            yaw, pitch, rotationIncrement);
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(@NotNull Entity owner,
                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.setVelocity(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
    }

    public boolean tryCorrectMotionOnBlockCollision(@NotNull Entity owner,
                                                    @NotNull Triplet<? extends Number, ? extends Number,
                                                        ? extends Number> pos) {
        return ((AccessorEntity) owner).tryCorrectMotionOnBlockCollision(pos.getX().doubleValue(),
            pos.getY().doubleValue(), pos.getZ().doubleValue());
    }

    public float getExplosionResistance(@NotNull Entity owner, @NotNull Explosion explosion, @NotNull World world,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        @NotNull Block block) {
        return owner.func_145772_a(explosion, world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), block);
    }

    public boolean isExplosionAffectBlock(@NotNull Entity owner, @NotNull Explosion explosion, @NotNull World world,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                          @NotNull Block block, float explosionSize) {
        return owner.func_145774_a(explosion, world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), block, explosionSize);
    }
    //--------------------------
    //endregion

    //region EntityCreature
    //--------------------------
    public float creature$getBlockPathWeight(@NotNull EntityCreature owner,
                                             @NotNull Triplet<? extends Number, ? extends Number,
                                                 ? extends Number> pos) {
        return owner.getBlockPathWeight(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean creature$isWithinHomeDistance(@NotNull EntityCreature owner,
                                                 @NotNull Triplet<? extends Number, ? extends Number,
                                                     ? extends Number> pos) {
        return owner.isWithinHomeDistance(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void creature$setHomeArea(@NotNull EntityCreature owner,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                     int maxDistance) {
        owner.setHomeArea(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), maxDistance);
    }
    //--------------------------
    //endregion

    //region EntityLeashKnot
    //--------------------------
    public @NotNull EntityLeashKnot leashKnot$forceSpawnLeashKnot(@NotNull World world,
                                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                                      ? extends Number> pos) {
        return EntityLeashKnot.func_110129_a(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public @Nullable EntityLeashKnot leashKnot$getKnotForBlock(@NotNull World world,
                                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                                   ? extends Number> pos) {
        return EntityLeashKnot.getKnotForBlock(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region EntityLivingBase
    //--------------------------
    @Contract(value = "_ -> new")
    public @NotNull Pos livingBase$getNewPos(@NotNull EntityLivingBase owner) {
        return Pos.of(((AccessorEntityLivingBase) owner).getNewPosX(), ((AccessorEntityLivingBase) owner).getNewPosY(),
            ((AccessorEntityLivingBase) owner).getNewPosZ());
    }

    public void livingBase$setNewPos(@NotNull Entity owner,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> newPos) {
        ((AccessorEntityLivingBase) owner).setNewPosX(newPos.getX().doubleValue());
        ((AccessorEntityLivingBase) owner).setNewPosY(newPos.getY().doubleValue());
        ((AccessorEntityLivingBase) owner).setNewPosZ(newPos.getZ().doubleValue());
    }

    public void livingBase$setPositionAndUpdate(@NotNull EntityLivingBase owner,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos) {
        owner.setPositionAndUpdate(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
    }
    //--------------------------
    //endregion

    //region IProjectile
    //--------------------------
    public void projectile$setThrowableHeading(@NotNull IProjectile owner,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos, float yaw, float pitch) {
        owner.setThrowableHeading(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(), yaw,
            pitch);
    }
    //--------------------------
    //endregion

    //region EntityWither
    //--------------------------
    public void wither$throwSkull(@NotNull EntityWither owner, int head,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                  boolean invulnerable) {
        ((AccessorEntityWither) owner).throwSkull(head, pos.getX().doubleValue(), pos.getY().doubleValue(),
            pos.getZ().doubleValue(), invulnerable);
    }
    //--------------------------
    //endregion

    //region EntityBoat
    //--------------------------
    @Contract(value = "_ -> new")
    public @NotNull Pos boat$getBoat(@NotNull EntityBoat owner) {
        return Pos.of(((AccessorEntityBoat) owner).getBoatX(), ((AccessorEntityBoat) owner).getBoatY(),
            ((AccessorEntityBoat) owner).getBoatZ());
    }

    public void boat$setBoat(@NotNull EntityBoat owner,
                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> boat) {
        ((AccessorEntityBoat) owner).setBoatX(boat.getX().doubleValue());
        ((AccessorEntityBoat) owner).setBoatY(boat.getY().doubleValue());
        ((AccessorEntityBoat) owner).setBoatZ(boat.getZ().doubleValue());
    }

    @SideOnly(Side.CLIENT)
    @Contract(value = "_ -> new")
    public @NotNull Pos boat$getVelocity(@NotNull EntityBoat owner) {
        return Pos.of(((AccessorEntityBoat) owner).getVelocityX(), ((AccessorEntityBoat) owner).getVelocityY(),
            ((AccessorEntityBoat) owner).getVelocityZ());
    }

    @SideOnly(Side.CLIENT)
    public void boat$setVelocity(@NotNull EntityBoat owner,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> velocity) {
        ((AccessorEntityBoat) owner).setVelocityX(velocity.getX().doubleValue());
        ((AccessorEntityBoat) owner).setVelocityY(velocity.getY().doubleValue());
        ((AccessorEntityBoat) owner).setVelocityZ(velocity.getZ().doubleValue());
    }
    //--------------------------
    //endregion

    //region EntityEnderEye
    //--------------------------
    @Contract(value = "_ -> new")
    public @NotNull Pos enderEye$getTarget(@NotNull EntityEnderEye owner) {
        return Pos.of(((AccessorEntityEnderEye) owner).getTargetX(), ((AccessorEntityEnderEye) owner).getTargetY(),
            ((AccessorEntityEnderEye) owner).getTargetZ());
    }

    public void enderEye$setTarget(@NotNull EntityEnderEye owner,
                                   @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> target) {
        ((AccessorEntityEnderEye) owner).setTargetX(target.getX().doubleValue());
        ((AccessorEntityEnderEye) owner).setTargetY(target.getY().doubleValue());
        ((AccessorEntityEnderEye) owner).setTargetZ(target.getZ().doubleValue());
    }

    public void enderEye$moveTowards(@NotNull EntityEnderEye owner,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.moveTowards(pos.getX().doubleValue(), pos.getY().intValue(), pos.getZ().doubleValue());
    }
    //--------------------------
    //endregion

    //region EntityMinecart
    //--------------------------
    @Contract(value = "_ -> new")
    public @NotNull Pos minecart$getMinecart(@NotNull EntityMinecart owner) {
        return Pos.of(((AccessorEntityMinecart) owner).getMinecartX(), ((AccessorEntityMinecart) owner).getMinecartY(),
            ((AccessorEntityMinecart) owner).getMinecartZ());
    }

    public void minecart$setMinecart(@NotNull EntityMinecart owner,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> minecart) {
        ((AccessorEntityMinecart) owner).setMinecartX(minecart.getX().doubleValue());
        ((AccessorEntityMinecart) owner).setMinecartY(minecart.getY().doubleValue());
        ((AccessorEntityMinecart) owner).setMinecartZ(minecart.getZ().doubleValue());
    }

    @SideOnly(Side.CLIENT)
    @Contract(value = "_ -> new")
    public @NotNull Pos minecart$getVelocity(@NotNull EntityMinecart owner) {
        return Pos.of(((AccessorEntityMinecart) owner).getVelocityX(), ((AccessorEntityMinecart) owner).getVelocityY(),
            ((AccessorEntityMinecart) owner).getVelocityZ());
    }

    @SideOnly(Side.CLIENT)
    public void minecart$setVelocity(@NotNull EntityMinecart owner,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> velocity) {
        ((AccessorEntityMinecart) owner).setVelocityX(velocity.getX().doubleValue());
        ((AccessorEntityMinecart) owner).setVelocityY(velocity.getY().doubleValue());
        ((AccessorEntityMinecart) owner).setVelocityZ(velocity.getZ().doubleValue());
    }

    public void minecart$onActivatorRailPass(@NotNull EntityMinecart owner,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                             boolean powered) {
        owner.onActivatorRailPass(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), powered);
    }

    public void minecart$updateMovementOnRail(@NotNull EntityMinecart owner,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos, double speed, double slopeAdjustment,
                                              @NotNull Block rail, int railMeta) {
        ((AccessorEntityMinecart) owner).updateMovementOnRail(pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), speed, slopeAdjustment, rail, railMeta);
    }

    @SideOnly(Side.CLIENT)
    public @Nullable Pos minecart$getNextPosition(@NotNull EntityMinecart owner,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos, double partial) {
        val vec = owner.func_70495_a(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(),
            partial);
        return vec != null ? Pos.of(vec) : null;
    }

    public @Nullable Pos minecart$getNextPosition(@NotNull EntityMinecart owner,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos) {
        val vec = owner.func_70489_a(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
        return vec != null ? Pos.of(vec) : null;
    }

    public void minecart$moveMinecartOnRail(@NotNull EntityMinecart owner,
                                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                            double slopeAdjustment) {
        owner.moveMinecartOnRail(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), slopeAdjustment);
    }
    //--------------------------
    //endregion

    //region EntityEnderman
    //--------------------------
    public boolean enderman$teleportTo(@NotNull EntityEnderman owner, @NotNull Triplet<? extends Number,
        ? extends Number, ? extends Number> pos) {
        return ((AccessorEntityEnderman) owner).callTeleportTo(pos.getX().doubleValue(), pos.getY().doubleValue(),
            pos.getZ().doubleValue());
    }
    //--------------------------
    //endregion

    //region EntityGhast
    //--------------------------
    @Contract(value = "_ -> new")
    public @NotNull Pos ghast$getWaypoint(@NotNull EntityGhast owner) {
        return Pos.of(owner.waypointX, owner.waypointY, owner.waypointZ);
    }

    public void ghast$setWaypoint(@NotNull EntityGhast owner,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> waypoint) {
        owner.waypointX = waypoint.getX().doubleValue();
        owner.waypointY = waypoint.getY().doubleValue();
        owner.waypointZ = waypoint.getZ().doubleValue();
    }

    public boolean ghast$isCourseTraversable(@NotNull EntityGhast owner,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                             double distance) {
        return ((AccessorEntityGhast) owner).callIsCourseTraversable(pos.getX().doubleValue(), pos.getY().doubleValue(),
            pos.getZ().doubleValue(), distance);
    }
    //--------------------------
    //endregion

    //region EntitySquid
    //--------------------------
    @Contract(value = "_ -> new")
    public @NotNull Pos squid$getRandomMotionVec(@NotNull EntitySquid owner) {
        return Pos.of(((AccessorEntitySquid) owner).getRandomMotionVecX(),
            ((AccessorEntitySquid) owner).getRandomMotionVecY(), ((AccessorEntitySquid) owner).getRandomMotionVecZ());
    }

    public void squid$setRandomMotionVec(@NotNull EntitySquid owner,
                                         @NotNull Triplet<? extends Number, ? extends Number,
                                             ? extends Number> randomMotionVec) {
        ((AccessorEntitySquid) owner).setRandomMotionVecX(randomMotionVec.getX().floatValue());
        ((AccessorEntitySquid) owner).setRandomMotionVecY(randomMotionVec.getY().floatValue());
        ((AccessorEntitySquid) owner).setRandomMotionVecZ(randomMotionVec.getZ().floatValue());
    }
    //--------------------------
    //endregion

    //region EntityPlayer
    //--------------------------
    public @NotNull Pos player$getPlayerLocation(@NotNull EntityPlayer owner) {
        return Pos.of(((AccessorEntityPlayer) owner).getPlayerLocation());
    }

    public void player$setPlayerLocation(@NotNull EntityPlayer owner,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorEntityPlayer) owner).setPlayerLocation(Pos.of(pos).asChunkCoordinates());
    }

    public @Nullable Pos player$getStartMinecartRidingCoordinate(@NotNull EntityPlayer owner) {
        val coordinate = ((AccessorEntityPlayer) owner).getStartMinecartRidingCoordinate();
        return coordinate != null ? Pos.of(coordinate) : null;
    }

    public void player$setStartMinecartRidingCoordinate(@NotNull EntityPlayer owner,
                                                        @Nullable Triplet<? extends Number, ? extends Number,
                                                            ? extends Number> pos) {
        ((AccessorEntityPlayer) owner).setStartMinecartRidingCoordinate(pos != null ? Pos.of(pos).asChunkCoordinates()
            : null);
    }

    public float player$getBreakSpeed(@NotNull EntityPlayer owner, @NotNull Block block, boolean cantBeHarvest,
                                      int meta,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getBreakSpeed(block, cantBeHarvest, meta, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void player$displayGUIEnchantment(@NotNull EntityPlayer owner,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                             @Nullable String title) {
        owner.displayGUIEnchantment(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), title);
    }

    public void player$displayGUIAnvil(@NotNull EntityPlayer owner,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.displayGUIAnvil(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void player$displayGUIWorkbench(@NotNull EntityPlayer owner,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.displayGUIWorkbench(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public @NotNull EntityPlayer.EnumStatus player$sleepInBedAt(@NotNull EntityPlayer owner,
                                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                                    ? extends Number> pos) {
        return owner.sleepInBedAt(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public @Nullable Pos player$verifyRespawnCoordinates(@NotNull World world,
                                                         @NotNull Triplet<? extends Number,
                                                             ? extends Number, ? extends Number> pos,
                                                         boolean forced) {
        val chunkCoordinates = EntityPlayer.verifyRespawnCoordinates(world, Pos.of(pos).asChunkCoordinates(), forced);
        return chunkCoordinates != null ? Pos.of(chunkCoordinates) : null;
    }

    @Deprecated
    public @Nullable Pos player$getBedLocation(@NotNull EntityPlayer owner) {
        val location = owner.getBedLocation();
        return location != null ? Pos.of(location) : null;
    }

    public void player$setSpawnChunk(@NotNull EntityPlayer owner, @NotNull Triplet<? extends Number, ? extends Number,
        ? extends Number> pos, boolean forced) {
        owner.setSpawnChunk(Pos.of(pos).asChunkCoordinates(), forced);
    }

    public void player$addMovementStat(@NotNull EntityPlayer owner,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> delta) {
        owner.addMovementStat(delta.getX().doubleValue(), delta.getY().doubleValue(), delta.getZ().doubleValue());
    }

    public void player$addMountedMovementStat(@NotNull EntityPlayer owner,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> delta) {
        ((AccessorEntityPlayer) owner).callAddMountedMovementStat(delta.getX().doubleValue(),
            delta.getY().doubleValue(), delta.getZ().doubleValue());
    }

    public void player$openGui(@NotNull EntityPlayer owner, @NotNull Object mod, int modGuiId, @NotNull World world,
                               @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.openGui(mod, modGuiId, world, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public @Nullable Pos player$getBedLocation(@NotNull EntityPlayer owner, int dimension) {
        val location = owner.getBedLocation(dimension);
        return location != null ? Pos.of(location) : null;
    }

    public void player$setSpawnChunk(@NotNull EntityPlayer owner, @NotNull Pos pos, boolean forced, int dimension) {
        owner.setSpawnChunk(Pos.of(pos).asChunkCoordinates(), forced, dimension);
    }
    //--------------------------
    //endregion

    //region EntityArrow
    //--------------------------
    public @NotNull Pos arrow$getTile(@NotNull EntityArrow owner) {
        return Pos.of(((AccessorEntityArrow) owner).getTileX(), ((AccessorEntityArrow) owner).getTileY(),
            ((AccessorEntityArrow) owner).getTileZ());
    }

    public void arrow$setTile(@NotNull EntityArrow owner,
                              @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorEntityArrow) owner).setTileX(pos.getX().intValue());
        ((AccessorEntityArrow) owner).setTileY(pos.getY().intValue());
        ((AccessorEntityArrow) owner).setTileZ(pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region EntityFireball
    //--------------------------
    public @NotNull Pos fireball$getTile(@NotNull EntityFireball owner) {
        return Pos.of(((AccessorEntityFireball) owner).getTileX(), ((AccessorEntityFireball) owner).getTileY(),
            ((AccessorEntityFireball) owner).getTileZ());
    }

    public void fireball$setTile(@NotNull EntityFireball owner,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorEntityFireball) owner).setTileX(pos.getX().intValue());
        ((AccessorEntityFireball) owner).setTileY(pos.getY().intValue());
        ((AccessorEntityFireball) owner).setTileZ(pos.getZ().intValue());
    }

    public @NotNull Pos fireball$getAcceleration(@NotNull EntityFireball owner) {
        return Pos.of(owner.accelerationX, owner.accelerationY, owner.accelerationZ);
    }

    public void fireball$setAcceleration(@NotNull EntityFireball owner,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.accelerationX = pos.getX().doubleValue();
        owner.accelerationY = pos.getY().doubleValue();
        owner.accelerationZ = pos.getZ().doubleValue();
    }
    //--------------------------
    //endregion

    //region EntityFishHook
    //--------------------------
    public @NotNull Pos fishHook$getTile(@NotNull EntityFishHook owner) {
        return Pos.of(((AccessorEntityFishHook) owner).getTileX(), ((AccessorEntityFishHook) owner).getTileY(),
            ((AccessorEntityFishHook) owner).getTileZ());
    }

    public void fishHook$setTile(@NotNull EntityFishHook owner,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorEntityFishHook) owner).setTileX(pos.getX().intValue());
        ((AccessorEntityFishHook) owner).setTileY(pos.getY().intValue());
        ((AccessorEntityFishHook) owner).setTileZ(pos.getZ().intValue());
    }

    public void fishHook$initFishHook(@NotNull EntityFishHook owner,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                      float totalMultiplier, float randMultiplier) {
        owner.func_146035_c(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(),
            totalMultiplier, randMultiplier);
    }
    //--------------------------
    //endregion

    //region EntityThrowable
    //--------------------------
    public @NotNull Pos throwable$getTile(@NotNull EntityThrowable owner) {
        return Pos.of(((AccessorEntityThrowable) owner).getTileX(), ((AccessorEntityThrowable) owner).getTileY(),
            ((AccessorEntityThrowable) owner).getTileZ());
    }

    public void throwable$setTile(@NotNull EntityThrowable owner,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        ((AccessorEntityThrowable) owner).setTileX(pos.getX().intValue());
        ((AccessorEntityThrowable) owner).setTileY(pos.getY().intValue());
        ((AccessorEntityThrowable) owner).setTileZ(pos.getZ().intValue());
    }
    //--------------------------
    //endregion
}
