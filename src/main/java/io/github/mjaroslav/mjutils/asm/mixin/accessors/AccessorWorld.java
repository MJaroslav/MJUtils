package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(World.class)
public interface AccessorWorld {
    @Invoker
    boolean callChunkExists(int x, int z);

    @Invoker
    int callComputeLightValue(int x, int y, int z, @NotNull EnumSkyBlock skyBlock);
}
