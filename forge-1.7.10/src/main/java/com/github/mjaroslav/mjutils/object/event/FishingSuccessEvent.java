package com.github.mjaroslav.mjutils.object.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.MinecraftForge;

/**
 * Fire when player catch a item out of the water by fishing rod.
 */
@Cancelable
public class FishingSuccessEvent extends Event {
    /**
     * FishHook rod owner.
     */
    public final EntityPlayer fisher;
    /**
     * Can be changed. Should not be null;
     */
    public FishingHooks.FishableCategory category;
    /**
     * Can be changed. Can be null.
     */
    public ItemStack catchStack;
    /**
     * Event caller.
     */
    public final EntityFishHook fishHook;
    /**
     * Original change. Value [0..1].
     */
    public final float chance;
    /**
     * Level of luck enchantment.
     */
    public final int luck;
    /**
     * Level of speed enchantment.
     */
    public final int speed;
    /**
     * FishHook world.
     */
    public final World world;
    /**
     * FishHook posX.
     */
    public final double x;
    /**
     * FishHook posY.
     */
    public final double y;
    /**
     * FishHook posZ.
     */
    public final double z;

    /**
     * See class documentation. Bus: {@link MinecraftForge#EVENT_BUS}.
     *
     * @param fisher     see {@link FishingSuccessEvent#fisher}
     * @param fishHook   see {@link FishingSuccessEvent#fishHook}
     * @param category   see {@link FishingSuccessEvent#category}
     * @param catchStack see {@link FishingSuccessEvent#catchStack}
     * @param chance     see {@link FishingSuccessEvent#chance}
     * @param luck       see {@link FishingSuccessEvent#luck}
     * @param speed      see {@link FishingSuccessEvent#speed}
     */
    public FishingSuccessEvent(EntityPlayer fisher, EntityFishHook fishHook, FishingHooks.FishableCategory category,
                               ItemStack catchStack, float chance, int luck, int speed) {
        this.fisher = fisher;
        this.fishHook = fishHook;
        this.catchStack = catchStack;
        this.category = category;
        this.chance = chance;
        this.luck = luck;
        this.speed = speed;
        world = fishHook.worldObj;
        x = fishHook.posX;
        y = fishHook.posY;
        z = fishHook.posZ;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    @Override
    public void setCanceled(boolean cancel) {
        super.setCanceled(cancel);
        category = null;
        catchStack = null;
    }
}
