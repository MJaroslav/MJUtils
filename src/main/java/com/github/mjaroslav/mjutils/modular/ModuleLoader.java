package com.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.asm.ModAnnotation.EnumHolder;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public final class ModuleLoader {
    @Nonnull
    public final String modId;
    @Nonnull
    public final Object modInstance;
    @Getter
    @Nonnull
    private String moduleRootPackage;

    private final List<ModuleInfo> modules = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public void findModules(@Nonnull ASMDataTable table) {
        for (ASMDataTable.ASMData data : table.getAll(SubscribeModule.class.getName())) {
            if (data.getClassName().startsWith(moduleRootPackage)) {
                //if (data.getAnnotationInfo().get("value").equals(modId)) {
                String[] modDependencies = new String[0];
                if (data.getAnnotationInfo().containsKey("modDependencies"))
                    modDependencies = ((List<String>) data.getAnnotationInfo().get("modDependencies")).toArray(modDependencies);
                int priority = (int) data.getAnnotationInfo().getOrDefault("priority", 0);
                ModState loadOn = ModState.CONSTRUCTED;
                if (data.getAnnotationInfo().containsKey("loadOn"))
                    loadOn = ModState.valueOf(ReflectionHelper.getPrivateValue(EnumHolder.class,
                            (EnumHolder) data.getAnnotationInfo().get("loadOn"), "value"));
                ModuleInfo info = new ModuleInfo(modDependencies, priority, loadOn, data.getClassName());
                if (info.isAllRequiredModsLoaded())
                    modules.add(info);
            }
        }
        modules.sort(Comparator.comparingInt(module -> module.priority));
        Proxy proxy = getProxyFromMod(modInstance);
        if (proxy != null)
            modules.add(new ModuleInfo(proxy));
    }

    public void listen(FMLEvent event) {
        modules.forEach(module -> module.listen(event));
    }

    @Nullable
    public static Proxy getProxyFromMod(@Nonnull Object modInstance) {
        int mods;
        for (Field field : modInstance.getClass().getFields()) {
            mods = field.getModifiers();
            if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && field.isAnnotationPresent(SidedProxy.class)) {
                try {
                    return (Proxy) field.get(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }
}
