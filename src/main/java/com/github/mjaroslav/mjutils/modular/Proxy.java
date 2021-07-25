package com.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public interface Proxy extends Modular, IGuiHandler {
    EntityPlayer getEntityPlayer(MessageContext ctx);

    boolean isGuiHandlerUsed();

    static boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }
}
