package mjaroslav.mcmods.mjutils.client;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import mjaroslav.mcmods.mjutils.common.MJUtilsCommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Client proxy for MJUtils mod.
 * 
 * @author MJaroslav
 *
 */
public class MJUtilsClientProxy extends MJUtilsCommonProxy {
	@Override
	public EntityPlayer getEntityPlayer(MessageContext ctx) {
		return ctx.side == Side.CLIENT ? Minecraft.getMinecraft().thePlayer : super.getEntityPlayer(ctx);
	}

	@Override
	public Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}