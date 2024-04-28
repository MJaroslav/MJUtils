package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockFurnace;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockFurnace.class)
public interface AccessorBlockFurnace {
    @Invoker("func_149930_e")
    void rotateFaceToFreeSide(@NotNull World world, int x, int y, int z);
}
