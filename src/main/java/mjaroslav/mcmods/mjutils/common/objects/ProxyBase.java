package mjaroslav.mcmods.mjutils.common.objects;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Base proxy for mods.
 * 
 * @author MJaroslav
 *
 */
public abstract class ProxyBase implements IModModule {
	/**
	 * Get proxy minecraft.
	 * 
	 * @return Instance of {@link Minecraft}. Null on server side.
	 */
	public abstract Minecraft getMinecraft();

	/**
	 * Get proxy player from packet message.
	 * 
	 * @return EntityPlayer from packet message context or
	 *         {@link Minecraft#thePlayer}.
	 */
	public abstract EntityPlayer getEntityPlayer(MessageContext ctx);

	/**
	 * Spawn custom particle on client side. Use {@link Minecraft#theWorld}.
	 * 
	 * @param name
	 *            - name of custom particle.
	 * @param x
	 *            - coordinate.
	 * @param y
	 *            - coordinate.
	 * @param z
	 *            - coordinate.
	 * @param args
	 *            - extra options.
	 */
	public abstract void spawnParticle(String name, double x, double y, double z, Object... args);

	@Override
	public final String getModuleName() {
		return "Proxy";
	}
	
	@Override
	public final int getPriority() {
		return 100;
	}
}
