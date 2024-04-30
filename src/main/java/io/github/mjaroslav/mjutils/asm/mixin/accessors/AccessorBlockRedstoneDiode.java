package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockRedstoneDiode.class)
public interface AccessorBlockRedstoneDiode {
    @Invoker("func_149897_b")
    void onRedstoneDiodeNeighborBlockChange(@NotNull World world, int x, int y, int z, @NotNull Block block);

    @Invoker("func_149910_g")
    boolean isPowerLocked(@NotNull IBlockAccess world, int x, int y, int z, int meta);

    @Invoker
    boolean callIsGettingInput(@NotNull World world, int x, int y, int z, int meta);

    @Invoker
    int callGetInputStrength(@NotNull World world, int x, int y, int z, int side);

    @Invoker("func_149902_h")
    int getInputStrengthFromSide(@NotNull IBlockAccess world, int x, int y, int z, int meta);

    @Invoker("func_149913_i")
    int getOutputStrengthFrom(@NotNull IBlockAccess world, int x, int y, int z, int direction);

    @Invoker("func_149911_e")
    void notifyNeighborPowerBlocks(@NotNull World world, int x, int y, int z);

    @Invoker("func_149904_f")
    int getOutputSignal(@NotNull IBlockAccess world, int x, int y, int z, int meta);

    @Invoker("func_149912_i")
    boolean isPreviousBlockDiode(@NotNull World world, int x, int y, int z, int meta);
}
