package com.github.mjaroslav.mjutils.configurator.impl.loader;

import com.github.mjaroslav.mjutils.configurator.Configurator;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorEvents;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;
import com.github.mjaroslav.mjutils.configurator.impl.configurator.forge.AnnotationForgeConfigurator;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class AnnotationForgeConfiguratorsLoader implements ConfiguratorsLoader {
    protected Map<String, AnnotationForgeConfigurator<?>> configurators = new HashMap<>();

    @Nonnull
    protected final String MOD_ID;
    protected boolean shouldCrashOnError;
    protected boolean enableEvents;

    public AnnotationForgeConfiguratorsLoader(@Nonnull String modId, boolean enableEvents, boolean shouldCrashOnError) {
        MOD_ID = modId;
        this.shouldCrashOnError = shouldCrashOnError;
        this.enableEvents = enableEvents;
    }

    @Nonnull
    @Override
    public String getModId() {
        return MOD_ID;
    }

    @SuppressWarnings("rawtypes")
    public List<IConfigElement> categoryToElementList(String from, String category) {
        if (configurators.containsKey(from)) {
            AnnotationForgeConfigurator<?> configurator = configurators.get(from);
            Configuration configuration = configurator.getConfigurationInstance();
            return new ConfigElement<>(configuration.getCategory(category)).getChildElements();
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("UnusedReturnValue")
    public int makeConfigurators(FMLConstructionEvent event) {
        Iterator<ASMDataTable.ASMData> iterator = event.getASMHarvestedData()
                .getAll(AnnotationConfiguratorsLoaderMarker.class.getName()).iterator();
        int count = 0;
        ASMDataTable.ASMData data;
        while (iterator.hasNext()) {
            data = iterator.next();
            if (MOD_ID.equals(data.getAnnotationInfo().get("modId")))
                try {
                    Class<?> clazz = Class.forName(data.getClassName());
                    AnnotationForgeConfigurator<?> configurator = new AnnotationForgeConfigurator<>(this,
                            (String) data.getAnnotationInfo().get("fileName"), clazz, true);
                    configurator.setName(clazz.getName());
                    if (shouldCrashOnError)
                        configurator.makeCrashOnError();
                    if (enableEvents)
                        configurator.shouldUseEvents();
                    ConfigurationEventHandler.instance.addConfig(configurator);
                    addConfigurators(configurator);
                    count++;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
        }
        return count;
    }

    @Override
    public void addConfigurators(Configurator<?>... configurators) {
        AnnotationForgeConfigurator<?> afc;
        for (Configurator<?> configurator : configurators)
            if (configurator instanceof AnnotationForgeConfigurator) {
                afc = (AnnotationForgeConfigurator<?>) configurator;
                if (afc.getName() == null)
                    throw new RuntimeException("This ConfiguratorsLoaded requires nonnull configurator names");
                if (this.configurators.containsKey(afc.getName()))
                    throw new RuntimeException("Trying add configurator with occupied name");
                this.configurators.put(afc.getName(), afc);
            }
    }

    @Override
    public void load() {
        boolean notCool = false;
        for (AnnotationForgeConfigurator<?> configurator : configurators.values())
            if (configurator.load().isNotCool()) {
                if (configurator.canCrashOnError())
                    throw new RuntimeException("Error on configuration loading! See console for more info");
                notCool = true;
                break;
            }
        if (!notCool)
            forceSync();
    }

    @Override
    public void save() {
        for (AnnotationForgeConfigurator<?> configurator : configurators.values())
            if (configurator.hasChanges() && configurator.save().isNotCool() && configurator.canCrashOnError())
                throw new RuntimeException("Error on configuration saving! See console for more info");
    }

    @Override
    public void restoreToDefault() {
        for (AnnotationForgeConfigurator<?> configurator : configurators.values())
            if (configurator.load().isNotCool() && configurator.canCrashOnError())
                throw new RuntimeException("Error on configuration restoring! See console for more info");
        save();
    }

    @Override
    public void forceSync() {
        Configurator.State state;
        for (AnnotationForgeConfigurator<?> configurator : configurators.values()) {
            state = configurator.sync();
            if (state.isNotCool() && configurator.canCrashOnError())
                throw new RuntimeException("Error on configuration syncing! See console for more info");
            // TODO: Write asking for world/game restart.
            if (enableEvents)
                if (ConfiguratorEvents.onConfiguratorChangedEventPost(configurator, false, false) != Event.Result.DENY)
                    ConfiguratorEvents.postConfiguratorChangedEventPost(configurator, false, false);
        }
    }

    @Nullable
    @Override
    public Configurator<?> getConfigurator(String name) {
        return configurators.get(name);
    }

    public @interface AnnotationConfiguratorsLoaderMarker {
        @Nonnull
        String modId();

        @Nonnull
        String fileName();
    }

    public static class ConfigurationEventHandler {
        public static final ConfigurationEventHandler instance = new ConfigurationEventHandler();

        private ConfigurationEventHandler() {
        }

        private final ArrayList<AnnotationForgeConfigurator<?>> list = new ArrayList<>();

        private void addConfig(AnnotationForgeConfigurator<?> newConfig) {
            list.add(newConfig);
            ModInfo.LOG.info(String.format("Added configuration file for \"%s\" mod. Configuration contained in \"%s\" file.", newConfig.getLoader().getModId(), newConfig.getFile()));
        }

        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            for (AnnotationForgeConfigurator<?> config : list)
                if (event.modID.equals(config.getLoader().getModId())) {
                    config.sync();
                    ModInfo.LOGGER_MODULES.info(String.format("Configuration synchronized for \"%s\" mod. Configuration contained in \"%s\" file.", config.getLoader().getModId(), config.getFile()));
                    break;
                }
        }
    }
}
