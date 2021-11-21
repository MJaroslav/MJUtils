package com.github.mjaroslav.mjutils.example.common.block;

import com.github.mjaroslav.mjutils.modular.BlockModule.SubscribeBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

@SubscribeBlock("exampleBlock")
public class ExampleBlock extends Block {
    public ExampleBlock() {
        super(Material.rock);
        setBlockTextureName("mjutilsexample:exampleblock");
        setBlockName("exampleblock");
        setCreativeTab(CreativeTabs.tabBlock);
    }
}
