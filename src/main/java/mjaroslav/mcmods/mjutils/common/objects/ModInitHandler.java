package mjaroslav.mcmods.mjutils.common.objects;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ModInitHandler {
	private String modid;
	private static Logger logger = LogManager.getLogger("MJUtils Module System");
	private ArrayList<IModModule> modules = new ArrayList<IModModule>();

	public ModInitHandler(String modid) {
		this.modid = modid;
	}

	public void findModules(FMLConstructionEvent event) {
		this.modules.clear();
		this.logger.log(Level.INFO, "Looking for modules for \"" + this.modid + "\"");
		Iterator<ASMData> iterator = event.getASMHarvestedData().getAll(ModInitModule.class.getName()).iterator();
		ASMData data = null;
		Object instance = null;
		int count = 0;
		if (iterator != null) {
			while (iterator.hasNext()) {
				data = iterator.next();
				if (((String) data.getAnnotationInfo().get("modid")).equals(this.modid)) {
					try {
						instance = Class.forName(data.getClassName()).newInstance();
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
					}
					if (instance != null && instance instanceof IModModule
							&& !((IModModule) instance).getModuleName().equals("Config")
							&& !((IModModule) instance).getModuleName().equals("Proxy")) {
						modules.add((IModModule) instance);
						this.logger.info("Found module for \"" + this.modid + "\": \""
								+ ((IModModule) instance).getModuleName() + "\"");
						count++;
					}
				}
			}
		}
		this.logger.info("Search finished, found " + count + " module" + (count == 1 ? "" : "s"));
	}

	public void preInit(FMLPreInitializationEvent event) {
		this.logger.info("Pre initialization of \"" + this.modid + "\"");
		for (IModModule module : modules)
			module.preInit(event);
	}

	public void init(FMLInitializationEvent event) {
		this.logger.info("Initialization of \"" + this.modid + "\"");
		for (IModModule module : modules)
			module.init(event);
	}

	public void postInit(FMLPostInitializationEvent event) {
		this.logger.info("Post initialization of \"" + this.modid + "\"");
		for (IModModule module : modules)
			module.postInit(event);
	}

	public String getModid() {
		return this.modid;
	}

	public ArrayList<IModModule> getModules() {
		return this.modules;
	}
}
