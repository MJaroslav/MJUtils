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
import mjaroslav.mcmods.mjutils.common.objects.ModInitHandler;

@Mod(name = ExampleInfo.NAME, modid = ExampleInfo.MODID, version = ExampleInfo.VERSION, guiFactory = ExampleInfo.GUIFACTORY, dependencies = ExampleInfo.DEPENDENCIES)
public class ExampleMod {
  public static Logger LOG = LogManager.getLogger(ExampleInfo.NAME);
  // The configuration wrapper.
  public static ExampleConfig config = new ExampleConfig();
  @SidedProxy(clientSide = ExampleInfo.CLIENTPROXY, serverSide = ExampleInfo.COMMONPROXY)
  public static ExampleCommonProxy proxy;
  @Instance(ExampleInfo.MODID)
  public static ExampleMod instance;
  // The object specified in the constructor will be the default.
  public static JSONReader<ExampleJSON> reader = new JSONReader<ExampleJSON>(new ExampleJSON(),
      new File("example.json"), true);
  private static ModInitHandler initHandler = new ModInitHandler(ExampleInfo.MODID);

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    // Config pre initialization.
    this.config.preInit(event);
    if (ExampleConfig.useExampleMod) {
      this.reader.setFile(new File(event.getModConfigurationDirectory() + "/example.json"));
      this.reader.init();
      // Modules pre initialization.
      this.initHandler.preInit(event);
      // Proxy pre initialization.
      this.proxy.preInit(event);
    }
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    // Config initialization.
    this.config.init(event);
    if (ExampleConfig.useExampleMod) {
      // Modules initialization.
      this.initHandler.init(event);
      // Proxy initialization.
      this.proxy.init(event);
    }
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    // Config post initialization.
    this.config.postInit(event);
    if (ExampleConfig.useExampleMod) {
      // Modules post initialization.
      this.initHandler.postInit(event);
      // Proxy post initialization.
      this.proxy.postInit(event);
    }
  }

  @EventHandler
  public void constr(FMLConstructionEvent event) {
    // Find modification modules, before pre initialization.
    this.initHandler.findModules(event);
  }

  @EventHandler
  public void serverStopped(FMLServerStoppingEvent event) {
    // Records part of the configuration fields in the JSON file, when
    // server stopping.
    if (ExampleConfig.useExampleMod) {
      this.reader.json.iAmABooleanCopy = ExampleConfig.iAmABoolean;
      this.reader.json.iAmAnIntegerCopy = ExampleConfig.iAmAnInteger;
      this.reader.json.iAmTheStringCopy = ExampleConfig.iAmTheString;
      this.reader.write();
    }
  }
}
