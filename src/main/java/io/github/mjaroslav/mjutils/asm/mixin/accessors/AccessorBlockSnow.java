package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockSnow;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockSnow.class)
public interface AccessorBlockSnow {
    @Invoker("func_150155_m")
    boolean tryRemoveBlockWhenCantStay(@NotNull World world, int x, int y, int z);
}
