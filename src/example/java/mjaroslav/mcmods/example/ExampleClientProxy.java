package mjaroslav.mcmods.example;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ExampleClientProxy extends ExampleCommonProxy {
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
  public EntityPlayer getEntityPlayer(MessageContext ctx) {
    return ctx.side == Side.CLIENT ? Minecraft.getMinecraft().thePlayer : super.getEntityPlayer(ctx);
  }

  @Override
  public Minecraft getMinecraft() {
    return Minecraft.getMinecraft();
  }

  @Override
  public void spawnParticle(String name, double x, double y, double z, Object... args) {
  }
}
