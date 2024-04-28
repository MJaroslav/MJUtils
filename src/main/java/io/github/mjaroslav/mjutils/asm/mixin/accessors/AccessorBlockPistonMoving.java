package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockPistonMoving;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockPistonMoving.class)
public interface AccessorBlockPistonMoving {
    @Invoker("func_149963_e")
    @Nullable
    TileEntityPiston getPistonMovingTileEntity(@NotNull IBlockAccess world, int x, int y, int z);
}
