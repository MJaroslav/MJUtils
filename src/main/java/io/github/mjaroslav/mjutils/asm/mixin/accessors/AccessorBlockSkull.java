package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockSkull;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockSkull.class)
public interface AccessorBlockSkull {
    @Invoker("func_149966_a")
    boolean isSkull(@NotNull World world, int x, int y, int z, int skullType);
}
