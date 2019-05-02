package mjaroslav.mcmods.mjutils.object.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class BlockReactionEvent extends Event {
    /**
     * Interaction world.
     */
    public World world;
    /**
     * Block event caller.
     */
    public Block block;
    /**
     * Metadata and position of block event caller.
     */
    public int meta, x, y, z;
    /**
     * The entity that triggered the event.
     */
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
     * {@link mjaroslav.mcmods.mjutils.util.UtilsInteractions#blockIsPigmanTrigger(Block, int)
     * blockIsPigmanTrigger}. Can be canceled. Bus: {@link MinecraftForge#EVENT_BUS}.
     */
    @Cancelable
    public static class PigmanTriggerEvent extends BlockReactionEvent {
        public PigmanTriggerEvent(World world, int x, int y, int z, EntityLivingBase harvester, Block block, int meta) {
            super(world, x, y, z, harvester, block, meta);
        }
    }

}
