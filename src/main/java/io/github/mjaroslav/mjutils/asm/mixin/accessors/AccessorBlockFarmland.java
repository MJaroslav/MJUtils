package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockFarmland;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockFarmland.class)
public interface AccessorBlockFarmland {
    @Invoker("func_149822_e")
    boolean isEmpty(@NotNull World world, int x, int y, int z);

    @Invoker("func_149821_m")
    boolean isMoistened(@NotNull World world, int x, int y, int z);
}
