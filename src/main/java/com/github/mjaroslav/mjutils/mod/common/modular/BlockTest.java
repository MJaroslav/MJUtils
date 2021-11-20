package com.github.mjaroslav.mjutils.mod.common.modular;

import com.github.mjaroslav.mjutils.modular.BlockModule;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

@BlockModule.SubscribeBlock("testBlock")
public class BlockTest extends Block {
    public BlockTest() {
        super(Material.carpet);
        System.out.println(getClass().getResource("").getPath());
        setBlockTextureName("mjutils:icon");
        setBlockName("bedrock");
    }


    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        System.out.println(textureName);
        super.registerBlockIcons(p_149651_1_);
    }
}
