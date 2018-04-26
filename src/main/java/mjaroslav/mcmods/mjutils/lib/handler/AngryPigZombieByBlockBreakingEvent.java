package mjaroslav.mcmods.mjutils.lib.handler;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@Cancelable
public class AngryPigZombieByBlockBreakingEvent extends Event {
    public World world;
    public Block block;
    public int meta;
    public int x;
    public int y;
    public int z;
    public EntityPlayer player;

    public AngryPigZombieByBlockBreakingEvent(Block block, int meta, EntityPlayer player, int x, int y, int z, World world) {
        this.block = block;
        this.world = world;
        this.meta = meta;
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = player;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
