package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockRedstoneWire.class)
public interface AccessorBlockRedstoneWire {
    @Invoker("func_150177_e")
    void updateRedstoneWireBlockStateAndNotifyOfNeighborChange(@NotNull World world, int x, int y, int z);

    @Invoker("func_150178_a")
    int getMaxPower(@NotNull World world, int x, int y, int z, int power);
}
