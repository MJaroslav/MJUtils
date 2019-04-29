package mjaroslav.mcmods.mjutils.object.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.FishingHooks;

@Cancelable
public class FishingSuccessEvent extends Event {
    public final EntityPlayer fisher;
    public FishingHooks.FishableCategory category;
    public ItemStack catchStack;
    public final EntityFishHook fishHook;
    public final float chance;
    public final int luck;
    public final int speed;
    public final World world;
    public final int x;
    public final int y;
    public final int z;

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
        x = fishHook.serverPosX;
        y = fishHook.serverPosY;
        z = fishHook.serverPosZ;
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
