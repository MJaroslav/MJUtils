package com.github.mjaroslav.mjutils.example.common.item;

import com.github.mjaroslav.mjutils.modular.ItemModule.SubscribeItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@SubscribeItem("exampleItem")
public class ExampleItem extends Item {
    public ExampleItem() {
        setTextureName("mjutilsexample:exampleitem");
        setUnlocalizedName("exampleitem");
        setCreativeTab(CreativeTabs.tabMisc);
    }
}
