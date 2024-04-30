package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockPistonBase.class)
public interface AccessorBlockPistonBase {
    @Invoker
    void callUpdatePistonState(@NotNull World world, int x, int y, int z);

    @Invoker
    boolean callIsIndirectlyPowered(@NotNull World world, int x, int y, int z, int orientation);

    @Invoker
    static boolean callCanPushBlock(@NotNull Block blockToPush, @NotNull World world, int x, int y, int z,
                                    boolean moveFlag) {
        throw new AssertionError();
    }

    @Invoker
    static boolean callCanExtend(@NotNull World world, int x, int y, int z, int orientation) {
        throw new AssertionError();
    }

    @Invoker
    boolean callTryExtend(@NotNull World world, int x, int y, int z, int orientation);
}
