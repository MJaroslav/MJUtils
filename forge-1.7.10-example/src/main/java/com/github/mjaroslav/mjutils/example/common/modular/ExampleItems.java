package com.github.mjaroslav.mjutils.example.common.modular;

import com.github.mjaroslav.mjutils.modular.ItemModule;
import com.github.mjaroslav.mjutils.modular.SubscribeModule;
import net.minecraft.item.Item;

@SubscribeModule
public class ExampleItems extends ItemModule {
    public static Item exampleItem;

    @Override
    public String getItemsRootPackage() {
        return "com.github.mjaroslav.mjutils.example.common.item";
    }
}
