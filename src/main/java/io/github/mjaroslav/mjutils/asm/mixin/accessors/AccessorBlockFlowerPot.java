package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockFlowerPot;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockFlowerPot.class)
public interface AccessorBlockFlowerPot {
    @Invoker("func_149929_e")
    @Nullable
    TileEntityFlowerPot getFlowerPotTileEntity(@NotNull World world, int x, int y, int z);
}
