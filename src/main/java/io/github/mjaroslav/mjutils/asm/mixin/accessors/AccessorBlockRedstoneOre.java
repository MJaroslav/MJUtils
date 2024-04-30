package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockRedstoneOre.class)
public interface AccessorBlockRedstoneOre {
    @Invoker("func_150185_e")
    void activateOre(@NotNull World world, int x, int y, int z);

    @Invoker("func_150186_m")
    void spawnRedstoneParticles(@NotNull World world, int x, int y, int z);
}
