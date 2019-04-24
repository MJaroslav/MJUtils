package mjaroslav.mcmods.mjutils.hook;

import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.minecraft.HookLoader;
import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;

// Don't forget -Dfml.coreMods.load=mjaroslav.mcmods.mjutils.hook.MJUtilsHookLoader
@SuppressWarnings("unused")
public class MJUtilsHookLoader extends HookLoader {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    @Override
    protected void registerHooks() {
        registerHookContainer("mjaroslav.mcmods.mjutils.hook.HookContainer");
    }
}
