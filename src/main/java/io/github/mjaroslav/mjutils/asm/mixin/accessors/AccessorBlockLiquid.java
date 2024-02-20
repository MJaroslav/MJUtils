package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockLiquid;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockLiquid.class)
public interface AccessorBlockLiquid {
    @Invoker("func_149804_e")
    int getFlowDecay(@NotNull World world, int x, int y, int z);

    @Invoker("getEffectiveFlowDecay")
    int getEffectiveFlowDecay(@NotNull IBlockAccess world, int x, int y, int z);

    @Invoker("getFlowVector")
    @NotNull Vec3 getFlowVector(@NotNull IBlockAccess world, int x, int y, int z);

    @Invoker("func_149805_n")
    void tryFreezeLava(@NotNull World world, int x, int y, int z);

    @Invoker("func_149799_m")
    void playEffects(@NotNull World world, int x, int y, int z);
}
