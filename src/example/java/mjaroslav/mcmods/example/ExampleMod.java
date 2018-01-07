package mjaroslav.mcmods.example;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import mjaroslav.mcmods.mjutils.common.json.JSONReader;
import mjaroslav.mcmods.mjutils.common.objects.ConfigurationHandler;
import mjaroslav.mcmods.mjutils.common.objects.ModInitHandler;

@Mod(name = ExampleInfo.NAME, modid = ExampleInfo.MODID, version = ExampleInfo.VERSION, guiFactory = ExampleInfo.GUIFACTORY, dependencies = ExampleInfo.DEPENDENCIES)
public class ExampleMod {
    public static Logger LOG = LogManager.getLogger(ExampleInfo.NAME);
    // The configuration wrapper.
    // public static ExampleConfig config = new ExampleConfig();
    public static ConfigurationHandler config = new ConfigurationHandler(ExampleInfo.MODID, LOG,
            "mjaroslav.mcmods.example.ExampleInfo");

    @SidedProxy(clientSide = ExampleInfo.CLIENTPROXY, serverSide = ExampleInfo.COMMONPROXY)
    public static ExampleCommonProxy proxy;
    @Instance(ExampleInfo.MODID)
    public static ExampleMod instance;
    // The object specified in the constructor will be the default.
    public static JSONReader<ExampleJSON> reader = new JSONReader<ExampleJSON>(new ExampleJSON(),
            new File("example.json"), true);
    private static ModInitHandler initHandler = new ModInitHandler(ExampleInfo.MODID, config, proxy);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        reader.setFile(new File(event.getModConfigurationDirectory() + "/example.json"));
        reader.init();
        // Modules pre initialization.
        initHandler.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Modules initialization.
        initHandler.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Modules post initialization.
        initHandler.postInit(event);
    }

    @EventHandler
    public void constr(FMLConstructionEvent event) {
        // Find modification modules, before pre initialization.
        initHandler.findModules(event);
    }

    @EventHandler
    public void serverStopped(FMLServerStoppingEvent event) {
        // Records part of the configuration fields in the JSON file, when
        // server stopping.
        if (ExampleInfo.useExampleMod) {
            reader.json.iAmABooleanCopy = ExampleInfo.iAmABoolean;
            reader.json.iAmAnIntegerCopy = ExampleInfo.iAmAnInteger;
            reader.json.iAmTheStringCopy = ExampleInfo.iAmTheString;
            reader.write();
        }
    }
}
