package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockLeaves;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockLeaves.class)
public interface AccessorBlockLeaves {
    @Invoker("removeLeaves")
    void removeLeaves(@NotNull World world, int x, int y, int z);

    @Invoker("func_150124_c")
    void dropExtra(@NotNull World world, int x, int y, int z, int meta, int chance);
}
