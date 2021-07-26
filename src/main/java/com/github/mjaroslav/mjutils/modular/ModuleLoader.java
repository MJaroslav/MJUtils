package com.github.mjaroslav.mjutils.modular;

import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.*;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ModuleLoader {
    protected final String MOD_ID;
    protected final List<Modular> MODULES;
    protected final Map<String, Modular> NAME_MODULE_MAP;
    protected final Object MOD_INSTANCE;

    public ModuleLoader(@Nonnull String modId, @Nonnull Object modInstance) {
        ModInfo.LOGGER_MODULES.info("Found ModuleLoader for \"%s\" mod!", modId);
        MOD_ID = modId;
        MODULES = new ArrayList<>();
        NAME_MODULE_MAP = new HashMap<>();
        MOD_INSTANCE = modInstance;
    }

    public String getModId() {
        return MOD_ID;
    }

    public void preInitialization(FMLPreInitializationEvent event) {
        ModInfo.LOGGER_MODULES.info("PreInitialization state of \"%s\" mod ModuleLoader", MOD_ID);
        MODULES.forEach(module -> module.preInitialization(event));
    }

    public void initialization(FMLInitializationEvent event) {
        ModInfo.LOGGER_MODULES.info("Initialization state of \"%s\" mod ModuleLoader", MOD_ID);
        MODULES.forEach(module -> module.initialization(event));
    }

    public void postInitialization(FMLPostInitializationEvent event) {
        ModInfo.LOGGER_MODULES.info("PostInitialization state of \"%s\" mod ModuleLoader", MOD_ID);
        MODULES.forEach(module -> module.postInitialization(event));
    }

    public void construction(FMLConstructionEvent event) {
        Iterator<ASMDataTable.ASMData> iterator = event.getASMHarvestedData().getAll(Module.class.getName()).iterator();
        int count = 0;
        Modular temp;
        while (iterator.hasNext()) {
            ASMDataTable.ASMData data = iterator.next();
            if (data.getAnnotationInfo().get("value").equals(MOD_ID)) {
                try {
                    Object instance = Class.forName(data.getClassName()).getConstructor(ModuleLoader.class).newInstance(this);
                    if (instance instanceof Modular) {
                        temp = (Modular) instance;
                        if (NAME_MODULE_MAP.containsKey(temp.getName()))
                            throw new RuntimeException(String.format("Error on \"%s\" mod module loading: same module name \"%s\" found (%s, %s)",
                                    temp.getName(), MOD_ID, NAME_MODULE_MAP.get(temp.getName()).getClass().getName(), temp.getClass().getName()));
                        MODULES.add(temp);
                        NAME_MODULE_MAP.put(temp.getName(), temp);
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        MODULES.removeIf(module -> {
            for (String mod : module.getModDependencies())
                if (!Loader.isModLoaded(mod)) {
                    NAME_MODULE_MAP.remove(module.getName());
                    return true;
                }
            for (String m : module.getModuleDependencies())
                if (!NAME_MODULE_MAP.containsKey(m)) {
                    NAME_MODULE_MAP.remove(module.getName());
                    return true;
                }
            return false;
        });
        Proxy proxy = getProxyFromMod(MOD_INSTANCE);
        if (proxy != null) {
            MODULES.add(proxy);
            NAME_MODULE_MAP.put(proxy.getName(), proxy);
        }
        MODULES.sort(Comparator.comparingInt(Modular::getPriority));
        List<String> names = new ArrayList<>();
        MODULES.forEach(module -> names.add(module.getName()));
        ModInfo.LOGGER_MODULES.info(String.format("Search finished, found %s module%s [%s]", count, count == 1 ? "" : "s",
                String.join(", ", names)));
        ModInfo.LOGGER_MODULES.info("Construction state of \"%s\" mod ModuleLoader", MOD_ID);
        MODULES.forEach(module -> module.construction(event));
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        ModInfo.LOGGER_MODULES.info("ServerStopping state of \"%s\" mod ModuleLoader", MOD_ID);
        MODULES.forEach(module -> module.serverStopping(event));
    }

    public void serverStopped(FMLServerStoppedEvent event) {
        ModInfo.LOGGER_MODULES.info("ServerStopped state of \"%s\" mod ModuleLoader", MOD_ID);
        MODULES.forEach(module -> module.serverStopped(event));
    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        ModInfo.LOGGER_MODULES.info("ServerAboutToStart state of \"%s\" mod ModuleLoader", MOD_ID);
        MODULES.forEach(module -> module.serverAboutToStart(event));
    }

    public void serverStarting(FMLServerStartingEvent event) {
        ModInfo.LOGGER_MODULES.info("ServerStarting state of \"%s\" mod ModuleLoader", MOD_ID);
        MODULES.forEach(module -> module.serverStarting(event));
    }

    public void serverStarted(FMLServerStartedEvent event) {
        ModInfo.LOGGER_MODULES.info("ServerStarted state of \"%s\" mod ModuleLoader", MOD_ID);
        MODULES.forEach(module -> module.serverStarted(event));
    }

    public void loadComplete(FMLLoadCompleteEvent event) {
        ModInfo.LOGGER_MODULES.info("LoadComplete state of \"%s\" mod ModuleLoader", MOD_ID);
        MODULES.forEach(module -> module.loadComplete(event));
    }

    public void communications(FMLInterModComms.IMCEvent event) {
        ModInfo.LOGGER_MODULES.info("InterModComms state of \"%s\" mod ModuleLoader", MOD_ID);
        MODULES.forEach(module -> module.communications(event));
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance() {
        return (T) MOD_INSTANCE;
    }

    public static Proxy getProxyFromMod(Object modInstance) {
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
