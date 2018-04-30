package mjaroslav.mcmods.mjutils.lib.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import mjaroslav.mcmods.mjutils.lib.utils.UtilsReaction;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
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
     * Fired when player break the block in
     * {@link UtilsReaction#getPigAngryBlockList()}.
     * 
     * @author MJaroslav
     */
    @Cancelable
    public static class PigZombieEvent extends BlockReactionEvent {
        public PigZombieEvent(World world, int x, int y, int z, EntityLivingBase harvester, Block block, int meta) {
            super(world, x, y, z, harvester, block, meta);
        }
    }

}
