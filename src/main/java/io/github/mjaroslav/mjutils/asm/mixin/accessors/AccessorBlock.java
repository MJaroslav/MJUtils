package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Block.class)
public interface AccessorBlock {
    @Invoker("dropBlockAsItem")
    void invokeDropBlockAsItem(@NotNull World world, int x, int y, int z, @NotNull ItemStack stack);
}
