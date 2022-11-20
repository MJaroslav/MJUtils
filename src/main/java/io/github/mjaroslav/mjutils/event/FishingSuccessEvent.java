package io.github.mjaroslav.mjutils.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import lombok.AllArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.FishingHooks.FishableCategory;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Random;

/**
 * Calls on successful (item was caught) fishing. Should be register in {@link MinecraftForge#EVENT_BUS} bus.
 */
@AllArgsConstructor
@Cancelable
public class FishingSuccessEvent extends Event {
    /**
     * Player that owner of fishhook, should not be null usually else event will cancel.
     */
    public final @Nullable EntityPlayer fishman;
    /**
     * Entity event-caller, you can (and should) get world and position from it.
     */
    public final @NotNull EntityFishHook fishHook;
    /**
     * Caught item fishable category, set null for cancel event.
     */
    public @Nullable FishableCategory caughtCategory;
    /**
     * Caught item, set null for cancel event.
     */
    public @Nullable ItemStack caughtItem;
    /**
     * Caught experience, set 0 if no drop needed.
     */
    public int caughtExperience;
    /**
     * Float of shitty math of initial caught item.
     *
     * @see net.minecraftforge.common.FishingHooks#getRandomFishable(Random, float, int, int)
     */
    @Range(from = 0, to = 1)
    public final float chance;
    /**
     * Luck enchantment level.
     */
    public final int luck;
    /**
     * Lure enchantment level.
     */
    public final int lure;
    /**
     * Increase player fishing statistic entry.
     */
    public boolean increaseStatistic;

    @Override
    public boolean isCanceled() {
        return super.isCanceled() || caughtCategory == null || caughtItem == null || fishman == null;
    }
}
