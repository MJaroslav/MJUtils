package com.github.mjaroslav.mjutils.example.common.modular;

import com.github.mjaroslav.mjutils.configurator.AnnotationConfigurator;
import com.github.mjaroslav.mjutils.example.lib.CategoryRoot;
import com.github.mjaroslav.mjutils.example.lib.ModInfo;
import com.github.mjaroslav.mjutils.modular.SubscribeModule;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import lombok.extern.log4j.Log4j2;

@SubscribeModule
@Log4j2
public class ExampleModule {
    public static final AnnotationConfigurator config = new AnnotationConfigurator(ModInfo.modId, ModInfo.modId, CategoryRoot.class);

    public void listen(FMLPreInitializationEvent event) {
        config.load();
        log.info("Hello world from ExampleModule!");
    }
}
