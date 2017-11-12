package mjaroslav.mcmods.mjutils.common;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import mjaroslav.mcmods.mjutils.common.objects.ProxyBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MJUtilsCommonProxy extends ProxyBase {
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
