package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockDragonEgg;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockDragonEgg.class)
public interface AccessorBlockDragonEgg {
    @Invoker("func_150018_e")
    void checkAndFall(@NotNull World world, int x, int y, int z);

    @Invoker("func_150019_m")
    void moveRandomly(@NotNull World world, int x, int y, int z);
}
