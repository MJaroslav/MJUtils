package mjaroslav.mcmods.mjutils.lib.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * WIP
 *
 * @author MJaroslav
 */
public class UtilsMultiBlock {
    public static ItemStack replaceBlock(World world, int x, int y, int z, Block block, int meta) {
        ItemStack temp = new ItemStack(Blocks.air, 1, 0);
        if (!world.isRemote) {
            Block oldBlock = world.getBlock(x, y, z);
            int oldMeta = world.getBlockMetadata(x, y, z);
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile == null) {
                if (block != null)
                    temp = new ItemStack(oldBlock, 1, oldMeta);
                world.setBlockToAir(x, y, z);
                world.removeTileEntity(x, y, z);
                world.setBlock(x, y, z, block, meta, 3);
            }
        }
        return temp;
    }
}
