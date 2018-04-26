package mjaroslav.mcmods.mjutils.mod.client;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import mjaroslav.mcmods.mjutils.mod.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientProxy extends CommonProxy {
    @Override
    public EntityPlayer getEntityPlayer(MessageContext ctx) {
        return ctx.side == Side.CLIENT ? Minecraft.getMinecraft().thePlayer : ctx.getServerHandler().playerEntity;
    }

    @Override
    public Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Override
    public void init(FMLInitializationEvent event) {
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }
}
