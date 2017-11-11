package mjaroslav.mcmods.mjutils.common.reaction;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ReactionUtils {
    private static ArrayList<ItemStack> pigAngryBlockList = new ArrayList<ItemStack>();

    public static void addBlockToPigAngryList(Block block, int meta) {
        pigAngryBlockList.add(new ItemStack(block, 1, meta));
    }

    public static boolean checkBlockToPigAngryList(Block block, int meta) {
        for (ItemStack stack : pigAngryBlockList)
            if (stack.getItem() == Item.getItemFromBlock(block)
                    && (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack.getItemDamage() == meta))
                return true;
        return false;
    }

    public static ArrayList<ItemStack> getPigAngryBlockList() {
        return pigAngryBlockList;
    }

    /**
     * Angry pig zombies on the player in the specified cube.
     *
     * @param world  - target world.
     * @param player - target.
     * @param x      - x center of cube.
     * @param y      - y center of cube.
     * @param z      - z center of cube.
     * @param expand - expansion from the center.
     * @return The number of angry pig zombies.
     */
    public static int pigZombiesBecomeAngryInRadius(World world, EntityPlayer player, double x, double y, double z,
                                                    double expand) {
        if (world == null || world.isRemote || player == null)
            return 0;
        List<EntityPigZombie> list = world.getEntitiesWithinAABB(EntityPigZombie.class,
                AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(expand, expand, expand));
        for (EntityPigZombie entity : list)
            entity.becomeAngryAt(player);
        return list.size();
    }
}
