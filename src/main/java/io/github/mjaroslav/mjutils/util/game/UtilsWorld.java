package io.github.mjaroslav.mjutils.util.game;

import io.github.mjaroslav.mjutils.asm.mixin.accessors.AccessorEntityPigZombie;
import io.github.mjaroslav.mjutils.util.object.game.DropChanceExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * A set of utilities for the events of the world, entities and blocks.
 */
public class UtilsWorld {
    /**
     * Angry pigmans on the player in the specified cube.
     *
     * @param world  target world.
     * @param target target.
     * @param x      x center of cube.
     * @param y      y center of cube.
     * @param z      z center of cube.
     * @param expand expansion from the center.
     * @return The number of angry pig zombies.
     */
    @SuppressWarnings("unchecked")
    public static int pigZombiesBecomeAngryInRadius(World world, EntityLivingBase target, double x, double y, double z,
                                                    double expand) {
        if (world == null || world.isRemote || target == null)
            return 0;
        List<EntityPigZombie> list = world.getEntitiesWithinAABB(EntityPigZombie.class,
            AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(expand, expand, expand));
        for (EntityPigZombie entity : list)
            ((AccessorEntityPigZombie) entity).callBecomeAngryAt(target);
        return list.size();
    }

    /**
     * Change side with a meta and rotation calculation.
     *
     * @param meta specified meta.
     * @param side specified side;
     * @return Changed side.
     */
    public static int getSideFromMeta(int meta, int side) {
        switch (ForgeDirection.getOrientation(meta)) {
            case EAST:
                return side == 4 ? 2 : side == 2 ? 4 : side == 3 ? 5 : side == 5 ? 3 : side;
            case NORTH:
                return side == 3 ? 2 : side == 2 ? 3 : side;
            case WEST:
                return side == 4 ? 3 : side == 3 ? 4 : side == 2 ? 5 : side == 5 ? 2 : side;
            case SOUTH:
                return side == 4 ? 5 : side == 5 ? 4 : side;
            default:
                return side;
        }
    }

    /**
     * Get the metadata of the rotation from the angle of view of the entity.
     *
     * @param target player to check.
     * @return rotation metadata or 0.
     */
    public static int getMetaFromRotation(EntityLivingBase target) {
        switch (MathHelper.floor_double((target.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) {
            case 0:
                return ForgeDirection.SOUTH.ordinal();
            case 1:
                return ForgeDirection.WEST.ordinal();
            case 2:
                return ForgeDirection.NORTH.ordinal();
            case 3:
                return ForgeDirection.EAST.ordinal();
        }
        return 0;
    }

    public static DropChanceExplosion newExplosionWithDropChance(World world, Entity placer, double x, double y, double z, float strength, boolean flaming, boolean smoking, float dropChance) {
        DropChanceExplosion explosion = new DropChanceExplosion(world, placer, x, y, z, strength, dropChance);
        explosion.isFlaming = flaming;
        explosion.isSmoking = smoking;
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }
}
