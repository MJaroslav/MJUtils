package io.github.mjaroslav.mjutils.event.world;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import lombok.AllArgsConstructor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.FishingHooks.FishableCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

@AllArgsConstructor
@Cancelable
public class FishingSuccessEvent extends Event {
    public final @Nullable Entity fisher;
    public final @NotNull EntityFishHook fishHook;
    public @Nullable FishableCategory category;
    public @Nullable ItemStack itemStack;
    public int experience;
    @Range(from = 0, to = 1)
    public final float chance;
    public final int luck;
    public final int lure;
    public boolean incStat;

    @Override
    public boolean isCanceled() {
        return super.isCanceled() || category == null || itemStack == null;
    }
}
