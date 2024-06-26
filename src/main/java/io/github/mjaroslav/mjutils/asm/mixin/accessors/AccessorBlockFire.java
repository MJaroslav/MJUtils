package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockFire;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Random;

@Mixin(BlockFire.class)
public interface AccessorBlockFire {
    @Invoker
    void callTryCatchFire(@NotNull World world, int x, int y, int z, int chance, @NotNull Random rand, int meta);

    @Invoker(remap = false)
    void callTryCatchFire(@NotNull World world, int x, int y, int z, int chance, @NotNull Random rand, int meta,
                          @NotNull ForgeDirection side);

    @Invoker
    boolean callCanNeighborBurn(@NotNull World world, int x, int y, int z);

    @Invoker
    int callGetChanceOfNeighborsEncouragingFire(@NotNull World world, int x, int y, int z);
}
