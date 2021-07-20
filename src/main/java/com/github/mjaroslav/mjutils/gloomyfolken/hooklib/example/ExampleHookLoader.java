package com.github.mjaroslav.mjutils.gloomyfolken.hooklib.example;

import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.minecraft.HookLoader;

public class ExampleHookLoader extends HookLoader {

    @Override
    public void registerHooks() {
        registerHookContainer("mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.example.AnnotationHooks");
    }
}
