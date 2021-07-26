package com.github.mjaroslav.mjutils.mod.client;

import com.github.mjaroslav.mjutils.mod.common.CommonProxy;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientProxy extends CommonProxy {
    @Override
    public EntityPlayer getEntityPlayer(MessageContext ctx) {
        return ctx.side == Side.CLIENT ? Minecraft.getMinecraft().thePlayer : ctx.getServerHandler().playerEntity;
    }
}
