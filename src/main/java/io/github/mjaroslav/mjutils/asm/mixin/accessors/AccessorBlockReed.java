package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockReed;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockReed.class)
public interface AccessorBlockReed {
    @Invoker("func_150170_e")
    boolean tryDropBlockWhenCantStay(@NotNull World world, int x, int y, int z);
}
