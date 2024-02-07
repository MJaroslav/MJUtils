package io.github.mjaroslav.mjutils.asm.mixin;

import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLEvent;
import io.github.mjaroslav.mjutils.modular.ModuleLoader;
import io.github.mjaroslav.mjutils.modular.SubscribeModule;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

import static io.github.mjaroslav.mjutils.lib.ModInfo.*;

@Mixin(value = FMLModContainer.class, remap = false)
public abstract class MixinFMLModContainer implements ModContainer {
    @Unique
    private static Set<ASMData> mjutils$asmRawModules;

    @Shadow(remap = false)
    private LoadController controller;

    @Shadow
    public abstract String getModId();

    @Shadow
    public abstract String getName();

    // Injecting module system loading
    @Inject(method = "handleModStateEvent", at = @At("HEAD"), remap = false)
    private void handleModStateEvent(@NotNull FMLEvent event, @NotNull CallbackInfo ci) {
        val modName = getName();
        val modId = getModId();
        try {
            val loader = ModuleLoader.getOrCreate(this);
            if (loader == null)
                return;
            if (event instanceof FMLConstructionEvent cons) {
                if (mjutils$asmRawModules == null) mjutils$asmRawModules = cons.getASMHarvestedData()
                    .getAll(SubscribeModule.class.getName());
                LOG_MODULES.debug(String.format("Parsing modules for %s (%s) mod", modName, modId));
                mjutils$asmRawModules.stream().filter(data -> data.getClassName().startsWith(loader.getRootPackage()) ||
                        StringUtils.equals(loader.getModId(), (String) data.getAnnotationInfo().get("value")))
                    .forEach(loader::parseModule);
                LOG_MODULES.debug(String.format("Trying find Proxy for %s (%s) mod", modName, modId));
                loader.tryFindAndAddProxy();
                loader.completeConstruction();
            }
            //noinspection DataFlowIssue
            loader.listen((FMLModContainer) (Object) this, event);
        } catch (Throwable t) {
            LOG_MODULES.fatal(String.format("Error while creating, loading or listening ModuleLoader for %s (%s) mod",
                modName, modId), t);
            controller.errorOccurred(this, t);
        }
    }
}
