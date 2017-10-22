package mjaroslav.mcmods.mjutils.common.objects;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import pro.ddopson.ClassEnumerator;

public class InitHandler {
	private String pckgName;
	private String modid;
	private Logger logger;
	private boolean isLog;
	private URL jar;
	private ArrayList<IInitBase> modules = new ArrayList<IInitBase>();

	public InitHandler(URL jar, String modid, String packageName, Logger logger, boolean isLog) {
		this.pckgName = packageName;
		this.jar = jar;
		this.modid = modid;
		this.logger = logger;
		this.isLog = isLog;
	}

	public void findModules() {
		this.logger.log(Level.INFO, "Search modules for \"" + this.modid + "\"");
		List<Class<?>> classes = null;
		this.modules.clear();
		try {
			classes = ClassEnumerator.processDirectory(new File("../bin/" + this.pckgName.replace('.', '/')),
					this.pckgName);
		} catch (Exception e) {
			classes = ClassEnumerator.processJarfile(this.jar, this.pckgName);
		}
		if (classes != null) {
			try {
				for (Class clazz : classes)
					for (Class intf : clazz.getInterfaces())
						if (intf.isAssignableFrom(IInitBase.class)) {
							IInitBase module = (IInitBase) clazz.newInstance();
							modules.add(module);
							if (this.isLog)
								this.logger.info(
										"Found module \"" + this.modid + "\": \"" + module.getModuleName() + "\"");
						}

			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public void preInit(FMLPreInitializationEvent event) {
		if (this.isLog)
			this.logger.info("Pre initialization of \"" + this.modid + "\"");
		for (IInitBase module : modules)
			module.preInit(event);
	}

	public void init(FMLInitializationEvent event) {
		if (this.isLog)
			this.logger.info("Initialization of \"" + this.modid + "\"");
		for (IInitBase module : modules)
			module.init(event);
	}

	public void postInit(FMLPostInitializationEvent event) {
		if (this.isLog)
			this.logger.info("Post initialization of \"" + this.modid + "\"");
		for (IInitBase module : modules)
			module.postInit(event);
	}

	public String getModid() {
		return this.modid;
	}

	public ArrayList<IInitBase> getModules() {
		return this.modules;
	}

	public String getPackageName() {
		return this.pckgName;
	}

	public boolean isLog() {
		return this.isLog;
	}

	public URL getJar() {
		return jar;
	}

	public void setLog(boolean isLog) {
		this.isLog = isLog;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Logger getLogger() {
		return logger;
	}
}
