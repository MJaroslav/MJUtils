package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockBush;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockBush.class)
public interface AccessorBlockBush {
    @Invoker("checkAndDropBlock")
    void checkAndDropBlock(@NotNull World world, int x, int y, int z);
}
