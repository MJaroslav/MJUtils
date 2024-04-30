package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockTripWire;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockTripWire.class)
public interface AccessorBlockTripWire {
    @Invoker("func_150138_a")
    void updateTripWireBlockState(@NotNull World world, int x, int y, int z, int meta);

    @Invoker("func_150140_e")
    void triggerTripWire(@NotNull World world, int x, int y, int z);
}
