package com.github.mjaroslav.mjutils.configurator;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

public class ConfiguratorEvents {
    public static Event.Result onConfiguratorChangedEventPost(@Nonnull Configurator configurator, boolean isWorldRunning, boolean requiresMcRestart) {
        ConfigChangedEvent event =
                new OnConfigChangedEvent(configurator.getModId(), configurator.getFile(), isWorldRunning, requiresMcRestart);
        FMLCommonHandler.instance().bus().post(event);
        return event.getResult();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static Event.Result postConfiguratorChangedEventPost(@Nonnull Configurator configurator, boolean isWorldRunning, boolean requiresMcRestart) {
        ConfigChangedEvent event =
                new PostConfigChangedEvent(configurator.getModId(), configurator.getFile(), isWorldRunning, requiresMcRestart);
        FMLCommonHandler.instance().bus().post(event);
        return event.getResult();
    }
}
