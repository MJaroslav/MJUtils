package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockDynamicLiquid.class)
public interface AccessorBlockDynamicLiquid {
    @Invoker("func_149811_n")
    void makeStill(@NotNull World world, int x, int y, int z);

    @Invoker("func_149813_h")
    void spreadWaterAndDoActions(@NotNull World world, int x, int y, int z, int decay);

    @Invoker("func_149812_c")
    int getDistanceToPit(@NotNull World world, int x, int y, int z, int distance, int currentDecay);

    @Invoker("func_149808_o")
    boolean @NotNull [] getFlowDecayDirections(@NotNull World world, int x, int y, int z);

    @Invoker("func_149807_p")
    boolean canNotFlowThrow(@NotNull World world, int x, int y, int z);

    @Invoker("func_149810_a")
    int continueFlowDecay(@NotNull World world, int x, int y, int z, int currentDecay);

    @Invoker("func_149809_q")
    boolean canFlowThrow(@NotNull World world, int x, int y, int z);
}
