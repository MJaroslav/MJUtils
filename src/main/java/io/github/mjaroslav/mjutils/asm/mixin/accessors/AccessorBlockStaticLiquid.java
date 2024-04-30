package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockStaticLiquid.class)
public interface AccessorBlockStaticLiquid {
    @Invoker
    void callSetNotStationary(@NotNull World world, int x, int y, int z);

    @Invoker
    boolean callIsFlammable(@NotNull World world, int x, int y, int z);
}
