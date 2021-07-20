package com.github.mjaroslav.mjutils.util;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearch;

import static thaumcraft.common.lib.events.EventHandlerRunic.getFinalWarp;

/**
 * Utilities for Thaumcraft mod integration. It works
 * only when Thaumcraft is loaded.
 */
public class UtilsThaumcraft {
    private static boolean thaumcraftExist = false;

    /**
     * Activate this integration. Needed for the library.
     * Please do not use.
     */
    public static void activate() {
        thaumcraftExist = true;
    }

    /**
     * Thaumcraft is loaded.
     *
     * @return True if loaded.
     */
    public static boolean exist() {
        return thaumcraftExist;
    }

    /**
     * Get total (all types) player's warp.
     *
     * @param player player to check.
     * @return Total player's warp or 0 if thaumcraft not loaded.
     */
    public static int getWarpTotal(EntityPlayer player) {
        if (!thaumcraftExist)
            return 0;
        return getWarpInventory(player) + getWarpPerm(player) + getWarpSticky(player) + getWarpTemp(player);
    }

    /**
     * Calculate total player's sticky warp.
     *
     * @param player player to check.
     * @return Total player's sticky warp or 0 if thaumcraft not loaded.
     */
    public static int getWarpSticky(EntityPlayer player) {
        if (!thaumcraftExist)
            return 0;
        return Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(player.getCommandSenderName());
    }

    /**
     * Calculate total player's temporal warp.
     *
     * @param player player to check.
     * @return Total player's temporal warp or 0 if thaumcraft not loaded.
     */
    public static int getWarpTemp(EntityPlayer player) {
        if (!thaumcraftExist)
            return 0;
        return Thaumcraft.proxy.getPlayerKnowledge().getWarpTemp(player.getCommandSenderName());
    }

    /**
     * Calculate total player's permanent warp.
     *
     * @param player player to check.
     * @return Total player's permanent warp or 0 if thaumcraft not loaded.
     */
    public static int getWarpPerm(EntityPlayer player) {
        if (!thaumcraftExist)
            return 0;
        return Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(player.getCommandSenderName());
    }

    /**
     * Calculate total warp from items in player inventory
     * (included armor and baubles).
     *
     * @param player player to check.
     * @return Total player's inventory warp or 0 if thaumcraft not loaded.
     */
    public static int getWarpInventory(EntityPlayer player) {
        if (!thaumcraftExist)
            return 0;
        int warp = 0;
        warp += getFinalWarp(player.getCurrentEquippedItem(), player);
        for (int a = 0; a < 4; ++a)
            warp += getFinalWarp(player.inventory.armorItemInSlot(a), player);
        IInventory baubles = BaublesApi.getBaubles(player);
        for (int a = 0; a < 4; ++a)
            warp += getFinalWarp(baubles.getStackInSlot(a), player);
        return warp;
    }

    /**
     * Add specified research to player knowledge.
     *
     * @param player   player for knowledge modification.
     * @param research specified research.
     */
    public static void complete(EntityPlayer player, String research) {
        if (!thaumcraftExist)
            return;
        if (isComplete(player, research))
            return;
        Thaumcraft.proxy.getResearchManager().completeResearch(player, research);
        if (player instanceof EntityPlayerMP)
            PacketHandler.INSTANCE.sendTo(new PacketSyncResearch(player), (EntityPlayerMP) player);
    }

    /**
     * Check if the player’s knowledge contains
     * the specified research.
     *
     * @param player   player to check.
     * @param research research to check.
     * @return True if player knowledge has this research.
     */
    public static boolean isComplete(EntityPlayer player, String research) {
        if (!thaumcraftExist)
            return false;
        return ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), research);
    }
}
