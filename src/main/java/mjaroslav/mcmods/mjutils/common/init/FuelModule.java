package mjaroslav.mcmods.mjutils.common.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mjaroslav.mcmods.mjutils.common.fuel.FuelHandler;
import mjaroslav.mcmods.mjutils.common.objects.IInitBase;

public class FuelModule implements IInitBase {
	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		GameRegistry.registerFuelHandler(new FuelHandler());
	}

	@Override
	public String getModuleName() {
		return "Fuel";
	}
}
