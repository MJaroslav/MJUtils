package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemSlab.class)
public interface AccessorItemSlab {
    @Invoker("func_150946_a")
    boolean tryCombineTwoSlabs(@NotNull ItemStack stack, @Nullable EntityPlayer placer, @NotNull World world, int x,
                               int y, int z, int direction);
}
