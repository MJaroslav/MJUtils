package io.github.mjaroslav.mjutils.event.player;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.AllArgsConstructor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
@AllArgsConstructor
@Cancelable
public class PlayerDestroyBlockInCreativeEvent extends Event {
    public final @NotNull ItemStack heldItem;
}
