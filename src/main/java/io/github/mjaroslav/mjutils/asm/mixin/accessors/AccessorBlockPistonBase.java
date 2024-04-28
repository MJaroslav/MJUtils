package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockPistonBase.class)
public interface AccessorBlockPistonBase {
    @Invoker("updatePistonState")
    void updatePistonState(@NotNull World world, int x, int y, int z);

    @Invoker("isIndirectlyPowered")
    boolean isIndirectlyPowered(@NotNull World world, int x, int y, int z, int orientation);

    @Invoker("canPushBlock")
    static boolean canPushBlock(@NotNull Block blockToPush, @NotNull World world, int x, int y, int z,
                                boolean moveFlag) {
        throw new AssertionError();
    }

    @Invoker("canExtend")
    static boolean canExtend(@NotNull World world, int x, int y, int z, int orientation) {
        throw new AssertionError();
    }

    @Invoker("tryExtend")
    boolean tryExtend(@NotNull World world, int x, int y, int z, int orientation);
}
