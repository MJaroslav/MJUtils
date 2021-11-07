package com.github.mjaroslav.mjutils.hook;

import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm.Hook;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.modular.SubscribeModule;
import com.github.mjaroslav.mjutils.util.game.UtilsMods;
import com.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLEvent;

import java.util.Set;

public class HooksFMLModContainer {
    private static Set<ASMData> asmRawModules;

    @Hook
    public static void handleModStateEvent(FMLModContainer instance, FMLEvent event) {
        LoadController controller = UtilsReflection.getPrivateValueUpTo(FMLModContainer.class, instance, ModContainer.class, "controller");
        try {
            ModuleLoader loader = UtilsMods.getOrTryCreateModuleLoader(instance);
            if (loader != null) {
                if (event instanceof FMLConstructionEvent) {
                    if (asmRawModules == null)
                        asmRawModules = ((FMLConstructionEvent) event).getASMHarvestedData().getAll(SubscribeModule.class.getName());
                    for (ASMData rawModule : asmRawModules)
                        loader.checkForModule(rawModule);
                    loader.tryFindAndAddProxy();
                    loader.sortModules();
                }
                loader.listen(event);
            }
        } catch (Throwable t) {
            controller.errorOccurred(instance, t);
        }
    }
}
