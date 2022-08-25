package com.github.mjaroslav.mjutils.test.asm.mixin;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Blocks.class)
public interface AccessorBlocks {
    @Accessor("air")
    @Mutable
    static void setAir(Block air) {
        throw new AssertionError();
    }
}
