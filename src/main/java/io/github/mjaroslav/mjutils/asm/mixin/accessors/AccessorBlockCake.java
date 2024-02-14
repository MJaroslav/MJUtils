package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockCake;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockCake.class)
public interface AccessorBlockCake {
    @Invoker("func_150036_b")
    void eatCake(@NotNull World world, int x, int y, int z, @NotNull EntityPlayer eater);
}
