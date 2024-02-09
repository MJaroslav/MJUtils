package io.github.mjaroslav.mjutils.util.game;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.Loader;
import io.github.mjaroslav.mjutils.util.object.game.ResearchItemShadow;
import io.github.mjaroslav.sharedjava.function.LazySupplier;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearch;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static thaumcraft.common.lib.events.EventHandlerRunic.*;

/**
 * Utilities for Thaumcraft internal integration. It works
 * only when Thaumcraft is loaded.
 */
@UtilityClass
public class UtilsThaumcraft {
    private final Pattern COPY_PATTERN = Pattern.compile("^_COPY_\\d+$");
    private final LazySupplier<Boolean> thaumcraftExist = new LazySupplier<>(() -> Loader.isModLoaded("Thaumcraft"));

    /**
     * Thaumcraft is loaded.
     *
     * @return True if loaded.
     */
    public boolean exist() {
        return thaumcraftExist.orElse(false);
    }

    public void checkAndThrow() {
        if (!exist()) throw new IllegalStateException("Thaumcraft not installed");
    }

    /**
     * Get total (all types) player's warp.
     *
     * @param player player to check.
     * @return Total player's warp or 0 if thaumcraft not loaded.
     */
    public int getWarpTotal(EntityPlayer player) {
        checkAndThrow();
        return getWarpInventory(player) + getWarpPerm(player) + getWarpSticky(player) + getWarpTemp(player);
    }

    /**
     * Calculate total player's sticky warp.
     *
     * @param player player to check.
     * @return Total player's sticky warp or 0 if thaumcraft not loaded.
     */
    public int getWarpSticky(EntityPlayer player) {
        checkAndThrow();
        return Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(player.getCommandSenderName());
    }

    /**
     * Calculate total player's temporal warp.
     *
     * @param player player to check.
     * @return Total player's temporal warp or 0 if thaumcraft not loaded.
     */
    public int getWarpTemp(EntityPlayer player) {
        checkAndThrow();
        return Thaumcraft.proxy.getPlayerKnowledge().getWarpTemp(player.getCommandSenderName());
    }

    /**
     * Calculate total player's permanent warp.
     *
     * @param player player to check.
     * @return Total player's permanent warp or 0 if thaumcraft not loaded.
     */
    public int getWarpPerm(EntityPlayer player) {
        checkAndThrow();
        return Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(player.getCommandSenderName());
    }

    /**
     * Calculate total warp from items in player inventory
     * (included armor and baubles).
     *
     * @param player player to check.
     * @return Total player's inventory warp or 0 if thaumcraft not loaded.
     */
    public int getWarpInventory(EntityPlayer player) {
        checkAndThrow();
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
    public void complete(EntityPlayer player, String research) {
        checkAndThrow();
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
    public boolean isComplete(EntityPlayer player, String research) {
        checkAndThrow();
        return ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), research);
    }

    public @NotNull String getNextCopyResearchKey(@NotNull String originalResearchKey) {
        checkAndThrow();
        val research = ResearchCategories.getResearch(originalResearchKey);
        if (research == null)
            throw new IllegalArgumentException("No one research with key " + originalResearchKey + " found");
        val siblings = research.siblings;
        if (siblings == null || siblings.length == 0) return originalResearchKey + "_COPY_0";
        return originalResearchKey + "_COPY_" + Arrays.stream(siblings).map(key -> key.replace(originalResearchKey, ""))
            .map(COPY_PATTERN::matcher).filter(Matcher::matches).count();
    }

    public @NotNull ResearchItem createCopyResearch(@NotNull String originalResearchKey, @NotNull String category,
                                                    int col, int row) {
        checkAndThrow();
        val research = ResearchCategories.getResearch(originalResearchKey);
        if (research == null)
            throw new IllegalArgumentException("No one research with key " + originalResearchKey + " found");
        return research.icon_item != null ? new ResearchItemShadow(originalResearchKey, category, col, row, research.icon_item) :
            new ResearchItemShadow(originalResearchKey, category, col, row, research.icon_resource);
    }
}
