package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockRailDetector;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockRailDetector.class)
public interface AccessorBlockRailDetector {
    @Invoker("func_150054_a")
    void updateRailDetectorBlockState(@NotNull World world, int x, int y, int z, int meta);
}
