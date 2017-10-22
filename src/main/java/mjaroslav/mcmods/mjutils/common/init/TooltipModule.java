package mjaroslav.mcmods.mjutils.common.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.common.objects.IInitBase;
import mjaroslav.mcmods.mjutils.common.tooltip.TooltipEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class TooltipModule implements IInitBase {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
	}

	@Override
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}

	@Override
	public String getModuleName() {
		return "Tooltip";
	}
}
