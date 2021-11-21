package com.github.mjaroslav.mjutils.example.common;

import com.github.mjaroslav.mjutils.modular.Proxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommonProxy extends Proxy {
    public void listen(FMLPreInitializationEvent event) {
        log.info("Hi! I'm all sided proxy call!");
    }
}
