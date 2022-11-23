package io.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.asm.ModAnnotation.EnumHolder;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import io.github.mjaroslav.mjutils.internal.lib.ModInfo;
import io.github.mjaroslav.mjutils.util.UtilsReflection;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public final class ModuleLoader {
    private final @NotNull String modId;
    private final @NotNull Object modInstance;
    private @NotNull String moduleRootPackage;
    private final List<ModuleInfo> modules = new ArrayList<>();
    private int foundModulesCount;
    private int activatedModulesCount;

    public void checkForModule(@NotNull ASMData asmParsedAnnotation) {
        val annotationInfo = asmParsedAnnotation.getAnnotationInfo();
        if (asmParsedAnnotation.getClassName().startsWith(moduleRootPackage) ||
            StringUtils.equals(modId, (String) annotationInfo.get("value"))) {
            var modDependencies = new String[0];
            if (annotationInfo.containsKey("modDependencies"))
                //noinspection unchecked
                modDependencies = ((List<String>) annotationInfo.get("modDependencies")).toArray(modDependencies);
            val priority = (int) annotationInfo.getOrDefault("priority", 0);
            var loadOn = ModState.CONSTRUCTED;
            if (annotationInfo.containsKey("loadOn"))
                loadOn = ModState.valueOf(ReflectionHelper.getPrivateValue(EnumHolder.class,
                    (EnumHolder) annotationInfo.get("loadOn"), "value"));
            val info = new ModuleInfo(modDependencies, priority, loadOn, asmParsedAnnotation.getClassName());
            if (info.isAllRequiredModsLoaded()) {
                modules.add(info);
                ModInfo.loggerModules.debug("Found module \"%s\" for internal \"%s\"", UtilsReflection.getSimpleClassName(
                    info.getModuleClassName()), modId);
            } else
                ModInfo.loggerModules.debug("Module \"%s\" from \"%s\" internal not will be load because internal pack not " +
                        "have all required mods for module: [%s]",
                    UtilsReflection.getSimpleClassName(info.getModuleClassName()), modId, String.join(", ",
                        info.getModDependencies()));
            foundModulesCount++;
        }
    }

    public void tryFindAndAddProxy() {
        val proxy = UtilsMods.getProxyModuleFromMod(modInstance);
        if (proxy != null) {
            modules.add(new ModuleInfo(proxy));
            foundModulesCount++;
            ModInfo.loggerModules.debug("Found proxy module for \"%s\" internal", modId);
        }
    }

    public void sortModules() {
        ModInfo.loggerModules.debug("Sorting modules for \"%s\" internal: %s", modId, modules.stream().map(module ->
            UtilsReflection.getSimpleClassName(module.getModuleClassName())).collect(Collectors.toList()));
        modules.sort(Comparator.comparingInt(ModuleInfo::getPriority));
        activatedModulesCount = modules.size();
        ModInfo.loggerModules.info("Activated %s\\%s of found modules for \"%s\" internal: %s", activatedModulesCount,
            foundModulesCount, modId,
            modules.stream().map(module -> UtilsReflection.getSimpleClassName(module.getModuleClassName()))
                .collect(Collectors.toList()));
    }

    @SuppressWarnings("deprecation")
    public void listen(@NotNull FMLModContainer container, @NotNull FMLEvent event) {
        ModInfo.loggerModules.debug("Listen \"%s\" event for %s modules of \"%s\" internal", event, modules.size(), modId);
        val bar = ProgressManager.push("Modules", modules.size(), true);
        modules.forEach(module -> {
            bar.step(UtilsReflection.getSimpleClassName(module.getModuleClassName()));
            module.listen(container, event);
        });
        ProgressManager.pop(bar);
    }
}
