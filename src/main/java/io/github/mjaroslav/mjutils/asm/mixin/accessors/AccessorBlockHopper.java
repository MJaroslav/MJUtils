package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockHopper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockHopper.class)
public interface AccessorBlockHopper {
    @Invoker("func_149919_e")
    void updateHopperBlockState(@NotNull World world, int x, int y, int z);
}
