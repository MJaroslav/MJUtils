package com.github.mjaroslav.mjutils.asm.mixin;

import com.github.mjaroslav.mjutils.modular.SubscribeModule;
import com.github.mjaroslav.mjutils.util.game.UtilsMods;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLEvent;
import lombok.val;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = FMLModContainer.class, remap = false)
public abstract class MixinFMLModContainer implements ModContainer {
    private static Set<ASMData> asmRawModules;

    @Shadow(remap = false)
    private LoadController controller;

    // Injecting module system loading
    @Inject(method = "handleModStateEvent", at = @At("HEAD"), remap = false)
    private void handleModStateEvent(FMLEvent event, CallbackInfo ci) {
        try {
            val loader = UtilsMods.getOrTryCreateModuleLoader(this, true);
            if (loader != null) {
                if (event instanceof FMLConstructionEvent) {
                    if (asmRawModules == null)
                        asmRawModules = ((FMLConstructionEvent) event).getASMHarvestedData()
                                .getAll(SubscribeModule.class.getName());
                    for (var rawModule : asmRawModules)
                        loader.checkForModule(rawModule);
                    loader.tryFindAndAddProxy();
                    loader.sortModules();
                }
                loader.listen((FMLModContainer) (Object) this, event);
            }
        } catch (Throwable t) {
            controller.errorOccurred(this, t);
        }
    }
}
