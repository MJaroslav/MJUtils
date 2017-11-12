package mjaroslav.mcmods.example;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import mjaroslav.mcmods.mjutils.common.thaum.ThaumUtils;
import net.minecraft.util.ChatComponentText;

public class ExampleEventHandler {
  @SubscribeEvent
  public void onJoin(PlayerLoggedInEvent event) {
    if (!event.player.worldObj.isRemote)
      event.player.addChatComponentMessage(new ChatComponentText("Your warp: " + ThaumUtils.getWarp(event.player)));
    // Indicates the amount of warp when entering the server.
  }
}
