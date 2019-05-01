package mjaroslav.mcmods.mjutils.module;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Template for creating mods proxies.
 */
public abstract class Proxy implements Initializator {
    /**
     * Get proxy player from packet message.
     *
     * @param ctx packet message.
     * @return EntityPlayer from packet message context or
     * {@link Minecraft#thePlayer}.
     */
    public abstract EntityPlayer getEntityPlayer(MessageContext ctx);

    /**
     * Spawn custom particle on client side. Use {@link Minecraft#theWorld}.
     *
     * @param name - name of custom particle.
     * @param x    - coordinate.
     * @param y    - coordinate.
     * @param z    - coordinate.
     * @param args - extra options.
     */
    public void spawnParticle(String name, double x, double y, double z, Object... args) {
    }

    /**
     * Get side if game.
     *
     * @return True if side is client.
     */
    public static boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }
}
