package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Random;

@Mixin(BlockRedstoneComparator.class)
public interface AccessorBlockRedstoneComparator {
    @Invoker
    int callGetOutputStrength(@NotNull World world, int x, int y, int z, int meta);

    @Invoker("func_149972_c")
    void updateRedstoneComparatorBlockState(@NotNull World world, int x, int y, int z, @NotNull Random rand);
}
