package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockCrops;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockCrops.class)
public interface AccessorBlockCrops {
    @Invoker("func_149864_n")
    float calculateGrowthChance(@NotNull World world, int x, int y, int z);

    @Invoker("func_149863_m")
    void tryGrowth(@NotNull World world, int x, int y, int z);
}
