package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockLever;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockLever.class)
public interface AccessorBlockLever {
    @Invoker("func_149820_e")
    boolean checkAndDropBlock(@NotNull World world, int x, int y, int z);
}
