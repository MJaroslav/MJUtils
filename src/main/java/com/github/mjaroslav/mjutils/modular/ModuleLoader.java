package com.github.mjaroslav.mjutils.modular;

import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import com.github.mjaroslav.mjutils.util.game.UtilsMods;
import com.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import com.github.mjaroslav.mjutils.util.logging.ModLogger;
import com.github.mjaroslav.mjutils.util.logging.UtilsLogger;
import com.github.mjaroslav.mjutils.util.logging.impl.Log4j2ModLogger;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.asm.ModAnnotation.EnumHolder;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class ModuleLoader {
    public static final ModLogger log = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, ModInfo.name + "/modules");

    @Nonnull
    public final String modId;
    @Nonnull
    public final Object modInstance;
    @Getter
    @Nonnull
    private String moduleRootPackage;

    private final List<ModuleInfo> modules = new ArrayList<>();

    private int foundModulesCount;

    public void checkForModule(@Nonnull ASMData asmParsedAnnotation) {
        Map<String, Object> annotationInfo = asmParsedAnnotation.getAnnotationInfo();
        if (asmParsedAnnotation.getClassName().startsWith(moduleRootPackage) ||
                StringUtils.equals(modId, (String) annotationInfo.get("value"))) {
            String[] modDependencies = new String[0];
            if (annotationInfo.containsKey("modDependencies"))
                //noinspection unchecked
                modDependencies = ((List<String>) annotationInfo.get("modDependencies")).toArray(modDependencies);
            int priority = (int) annotationInfo.getOrDefault("priority", 0);
            ModState loadOn = ModState.CONSTRUCTED;
            if (annotationInfo.containsKey("loadOn"))
                loadOn = ModState.valueOf(ReflectionHelper.getPrivateValue(EnumHolder.class,
                        (EnumHolder) annotationInfo.get("loadOn"), "value"));
            ModuleInfo info = new ModuleInfo(modDependencies, priority, loadOn, asmParsedAnnotation.getClassName());
            if (info.isAllRequiredModsLoaded()) {
                modules.add(info);
                log.debug("Found module \"%s\" for mod \"%s\"", UtilsReflection.getSimpleClassName(info.moduleClassName), modId);
            } else
                log.debug("Module \"%s\" from \"%s\" mod not will be load because mod pack not have all required mods for module: [%s]",
                        UtilsReflection.getSimpleClassName(info.moduleClassName), modId, String.join(", ", info.modDependencies));
            foundModulesCount++;
        }
    }

    public void tryFindAndAddProxy() {
        Proxy proxy = UtilsMods.getProxyModuleFromMod(modInstance);
        if (proxy != null) {
            modules.add(new ModuleInfo(proxy));
            foundModulesCount++;
            log.debug("Found proxy module for \"%s\" mod", modId);
        }
    }

    public void sortModules() {
        log.debug("Sorting modules for \"%s\" mod: %s", modId, modules.stream().map(module ->
                UtilsReflection.getSimpleClassName(module.moduleClassName)).collect(Collectors.toList()));
        modules.sort(Comparator.comparingInt(module -> module.priority));
        log.info("Activated %s\\%s of found modules for \"%s\" mod: %s", modules.size(), foundModulesCount, modId,
                modules.stream().map(module -> UtilsReflection.getSimpleClassName(module.moduleClassName))
                        .collect(Collectors.toList()));
    }

    public void listen(FMLEvent event) {
        log.debug("Listen \"%s\" event for %s modules of \"%s\" mod", event, modules.size(), modId);
        ProgressManager.ProgressBar bar = ProgressManager.push("Modules", modules.size(), true);
        modules.forEach(module -> {
            bar.step(UtilsReflection.getSimpleClassName(module.moduleClassName));
            module.listen(event);
        });
        ProgressManager.pop(bar);
    }
}
