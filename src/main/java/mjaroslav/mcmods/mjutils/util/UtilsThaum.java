package mjaroslav.mcmods.mjutils.util;

import static thaumcraft.common.lib.events.EventHandlerRunic.*;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearch;

/**
 * ThaumCraft util.
 *
 * @author MJaroslav
 */
public class UtilsThaum {
    private static boolean thaumExist = false;

    /**
     * Thaumcraft is loaded.
     */
    public static void activate() {
        thaumExist = true;
    }

    public static boolean exist() {
        return thaumExist;
    }

    /**
     * Get total (all types) warp count from player.
     *
     * @param player
     *            - target.
     * @return Warp count, all types in one.
     */
    public static int getWarpTotal(EntityPlayer player) {
        if (!thaumExist)
            return 0;
        return getWarpInventory(player) + getWarpPerm(player) + getWarpSticky(player) + getWarpTemp(player);
    }

    public static int getWarpSticky(EntityPlayer player) {
        if (!thaumExist)
            return 0;
        return Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(player.getCommandSenderName());
    }

    public static int getWarpTemp(EntityPlayer player) {
        if (!thaumExist)
            return 0;
        return Thaumcraft.proxy.getPlayerKnowledge().getWarpTemp(player.getCommandSenderName());
    }

    public static int getWarpPerm(EntityPlayer player) {
        if (!thaumExist)
            return 0;
        return Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(player.getCommandSenderName());
    }

    public static int getWarpInventory(EntityPlayer player) {
        if (!thaumExist)
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

    public static void complete(EntityPlayer player, String research) {
        if (!thaumExist)
            return;
        if (isComplete(player, research))
            return;
        Thaumcraft.proxy.getResearchManager().completeResearch(player, research);
        if (player instanceof EntityPlayerMP)
            PacketHandler.INSTANCE.sendTo(new PacketSyncResearch(player), (EntityPlayerMP) player);
    }

    public static boolean isComplete(EntityPlayer player, String research) {
        if (!thaumExist)
            return false;
        return ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), research);
    }
}
