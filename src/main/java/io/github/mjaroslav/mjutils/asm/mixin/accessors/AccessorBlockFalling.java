package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockFalling;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockFalling.class)
public interface AccessorBlockFalling {
    @Invoker("func_149830_m")
    void tryFall(@NotNull World world, int x, int y, int z);
}
