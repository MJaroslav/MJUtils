package com.github.mjaroslav.mjutils.example.server;

import com.github.mjaroslav.mjutils.example.common.CommonProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ServerProxy extends CommonProxy {
    public void listen(FMLInitializationEvent event) {
        log.info("Hi! I'm server sided proxy call!");
    }
}
