package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockTripWireHook;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockTripWireHook.class)
public interface AccessorBlockTripWireHook {
    @Invoker("func_150136_a")
    void updateTripWireHookBlockState(@NotNull World world, int x, int y, int z, boolean connected, int meta,
                                      boolean notify, int wireLength, int side);

    @Invoker("func_150135_a")
    void playerSoundOnBlockStateChange(@NotNull World world, int x, int y, int z, boolean connected, boolean powered,
                                       boolean newConnected, boolean newPowered);

    @Invoker("func_150134_a")
    void notifyBlocksOfNeighborChangeBySide(@NotNull World world, int x, int y, int z, int side);

    @Invoker("func_150137_e")
    boolean tryDropBlockWhenCantStay(@NotNull World world, int x, int y, int z);
}
