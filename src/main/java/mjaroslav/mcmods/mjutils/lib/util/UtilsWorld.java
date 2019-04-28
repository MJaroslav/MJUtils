package mjaroslav.mcmods.mjutils.lib.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class UtilsWorld {
    /**
     * Angry pig zombies on the player in the specified cube.
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
            entity.becomeAngryAt(target);
        return list.size();
    }
}
