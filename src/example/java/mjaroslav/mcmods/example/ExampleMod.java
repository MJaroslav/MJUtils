package mjaroslav.mcmods.example;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import mjaroslav.mcmods.mjutils.common.json.JSONReader;
import mjaroslav.mcmods.mjutils.common.objects.ModInitHandler;

@Mod(name = ExampleInfo.NAME, modid = ExampleInfo.MODID, version = ExampleInfo.VERSION, guiFactory = ExampleInfo.GUIFACTORY, dependencies = ExampleInfo.DEPENDENCIES)
public class ExampleMod {
	public static Logger LOG = LogManager.getLogger(ExampleInfo.NAME);
	private static ModInitHandler initHandler = new ModInitHandler(ExampleInfo.MODID);
	public static ExampleConfig config = new ExampleConfig();
	@SidedProxy(clientSide = ExampleInfo.CLIENTPROXY, serverSide = ExampleInfo.COMMONPROXY)
	public static ExampleCommonProxy proxy;
	@Instance(ExampleInfo.MODID)
	public static ExampleMod instance;
	public static JSONReader<ExampleJSON> reader = new JSONReader<ExampleJSON>(new ExampleJSON(),
			new File("example.json"), true);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		this.config.preInit(event);
		if (ExampleConfig.useExampleMod) {
			this.reader.setFile(new File(event.getModConfigurationDirectory() + "/example.json"));
			this.reader.init();
			this.initHandler.preInit(event);
			this.proxy.preInit(event);
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		this.config.init(event);
		if (ExampleConfig.useExampleMod) {
			this.initHandler.init(event);
			this.proxy.init(event);
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		this.config.postInit(event);
		if (ExampleConfig.useExampleMod) {
			this.initHandler.postInit(event);
			this.proxy.postInit(event);
		}
	}

	@EventHandler
	public void constr(FMLConstructionEvent event) {
		this.initHandler.findModules(event);
	}

	@EventHandler
	public void serverStopped(FMLServerStoppingEvent event) {
		if (ExampleConfig.useExampleMod) {
			this.reader.json.iAmABooleanCopy = ExampleConfig.iAmABoolean;
			this.reader.json.iAmAnIntegerCopy = ExampleConfig.iAmAnInteger;
			this.reader.json.iAmTheStringCopy = ExampleConfig.iAmTheString;
			this.reader.write();
		}
	}
}
