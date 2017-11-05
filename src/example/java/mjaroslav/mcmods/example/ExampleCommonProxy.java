package mjaroslav.mcmods.example;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import mjaroslav.mcmods.mjutils.common.objects.ProxyBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ExampleCommonProxy extends ProxyBase {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
	}

	@Override
	public void init(FMLInitializationEvent event) {
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}

	@Override
	public Minecraft getMinecraft() {
		return null;
	}

	@Override
	public EntityPlayer getEntityPlayer(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}

	@Override
	public void spawnParticle(String name, double x, double y, double z, Object... args) {
	}
}
