package mjaroslav.mcmods.mjutils.common.objects;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public abstract class ProxyBase implements IModModule {
	public abstract Minecraft getMinecraft();

	public abstract EntityPlayer getEntityPlayer(MessageContext ctx);

	public abstract void spawnParticle(String name, double x, double y, double z, Object... args);
}
