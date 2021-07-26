package com.github.mjaroslav.mjutils.mod.server;

import com.github.mjaroslav.mjutils.mod.common.CommonProxy;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public class ServerProxy extends CommonProxy {
    @Override
    public EntityPlayer getEntityPlayer(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }
}
