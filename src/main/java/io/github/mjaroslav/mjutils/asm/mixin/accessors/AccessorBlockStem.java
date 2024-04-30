package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockStem;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockStem.class)
public interface AccessorBlockStem {
    @Invoker("func_149875_n")
    float getFruitProduceChance(@NotNull World world, int x, int y, int z);
}
