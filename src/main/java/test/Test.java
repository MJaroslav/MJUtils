package test;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLModDisabledEvent;
import lombok.extern.log4j.Log4j2;

@Mod(modid = "test", name = "Test", version = "1.0.0", canBeDeactivated = true)
@Log4j2
public class Test {
    @Mod.EventHandler
    public void onDisabled(FMLModDisabledEvent event) {
        log.info("=====================================");
        log.info("=====================================");
        log.info("=====================================");
        log.info("=====================================");
        log.info("=====================================");
        log.info("Mod Disabled");
    }
}
