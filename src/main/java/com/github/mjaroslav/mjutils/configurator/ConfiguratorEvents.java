package com.github.mjaroslav.mjutils.configurator;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

public class ConfiguratorEvents {
    public static Event.Result onConfiguratorChangedEventPost(@Nonnull Configurator<?> configurator, boolean isWorldRunning, boolean requiresMcRestart) {
        ConfigChangedEvent event =
                new OnConfigChangedEvent(configurator.getLoader().getModId(), configurator.getFile(), isWorldRunning, requiresMcRestart);
        FMLCommonHandler.instance().bus().post(event);
        return event.getResult();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static Event.Result postConfiguratorChangedEventPost(@Nonnull Configurator<?> configurator, boolean isWorldRunning, boolean requiresMcRestart) {
        ConfigChangedEvent event =
                new PostConfigChangedEvent(configurator.getLoader().getModId(), configurator.getFile(), isWorldRunning, requiresMcRestart);
        FMLCommonHandler.instance().bus().post(event);
        return event.getResult();
    }

    public static ConfiguratorInstanceChangedEvent configuratorInstanceChangedEventPost(Object oldInstance, @Nonnull Object newInstance, boolean notCancelable) {
        ConfiguratorInstanceChangedEvent event = new ConfiguratorInstanceChangedEvent(oldInstance, newInstance, notCancelable);
        FMLCommonHandler.instance().bus().post(event);
        return event;
    }

    public static class ConfiguratorInstanceChangedEvent extends Event {
        public Object oldInstance;
        @Nonnull
        public Object newInstance;
        public final boolean NOT_CANCELABLE;
        // TODO: Add requires mc/world restart.

        public ConfiguratorInstanceChangedEvent(Object oldInstance, @Nonnull Object newInstance, boolean notCancelable) {
            this.oldInstance = oldInstance;
            this.newInstance = newInstance;
            NOT_CANCELABLE = notCancelable;
        }

        @Override
        public boolean isCancelable() {
            return !NOT_CANCELABLE;
        }
    }
}
