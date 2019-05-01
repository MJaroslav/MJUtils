package mjaroslav.mcmods.mjutils.object.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Cancelable
public class BlockReactionEvent extends Event {
    public World world;
    public Block block;
    public int meta, x, y, z;
    public EntityLivingBase harvester;

    public BlockReactionEvent(World world, int x, int y, int z, EntityLivingBase harvester, Block block, int meta) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.harvester = harvester;
        this.block = block;
        this.meta = meta;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    /**
     * Fire if any broken by player block return true from
     * {@link mjaroslav.mcmods.mjutils.util.UtilsInteractions#blockBreakingIsDisabledInCreative(ItemStack)
     * blockBreakingIsDisabledInCreative}. Can be canceled.
     */
    @Cancelable
    public static class PigZombieEvent extends BlockReactionEvent {
        public PigZombieEvent(World world, int x, int y, int z, EntityLivingBase harvester, Block block, int meta) {
            super(world, x, y, z, harvester, block, meta);
        }
    }

}
