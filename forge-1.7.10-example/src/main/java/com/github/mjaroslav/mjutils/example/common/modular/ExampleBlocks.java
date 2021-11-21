package com.github.mjaroslav.mjutils.example.common.modular;

import com.github.mjaroslav.mjutils.modular.BlockModule;
import com.github.mjaroslav.mjutils.modular.SubscribeModule;
import net.minecraft.block.Block;

@SubscribeModule
public class ExampleBlocks extends BlockModule {
    public static Block exampleBlock;

    @Override
    public String getBlocksRootPackage() {
        return "com.github.mjaroslav.mjutils.example.block";
    }
}
