package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockStairs;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockStairs.class)
public interface AccessorBlockStairs {
    @Invoker("func_150146_f")
    boolean isStairWithSameMeta(@NotNull IBlockAccess world, int x, int y, int z, int meta);
}
