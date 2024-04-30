package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockVine;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockVine.class)
public interface AccessorBlockVine {
    @Invoker("func_150094_e")
    boolean canVineStayAtBlock(@NotNull World world, int x, int y, int z);
}
