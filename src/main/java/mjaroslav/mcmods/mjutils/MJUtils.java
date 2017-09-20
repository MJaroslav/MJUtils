package mjaroslav.mcmods.mjutils;

import java.util.ArrayList;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.common.anvil.AnvilModule;
import mjaroslav.mcmods.mjutils.common.fuel.FuelModule;
import mjaroslav.mcmods.mjutils.common.init.IInitBase;
import mjaroslav.mcmods.mjutils.common.tooltip.TooltipModule;

@Mod(modid = MJInfo.MODID, name = MJInfo.NAME, version = MJInfo.VERSION)
public class MJUtils {
	public ArrayList<IInitBase> modules = new ArrayList<IInitBase>();

	public void initModules() {
		this.modules.add(new AnvilModule());
		this.modules.add(new FuelModule());
		this.modules.add(new TooltipModule());
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		initModules();
		for (IInitBase module : this.modules)
			module.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		for (IInitBase module : this.modules)
			module.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		for (IInitBase module : this.modules)
			module.postInit(event);
	}
}
