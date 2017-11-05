package mjaroslav.mcmods.mjutils.common.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.MJInfo;
import mjaroslav.mcmods.mjutils.common.objects.IModModule;
import mjaroslav.mcmods.mjutils.common.objects.ModInitModule;
import mjaroslav.mcmods.mjutils.common.tooltip.TooltipEventHandler;
import net.minecraftforge.common.MinecraftForge;

/**
 * MJUtils tooltip module.
 * 
 * @author MJaroslav
 *
 */
@ModInitModule(modid = MJInfo.MODID)
public class TooltipModule implements IModModule {
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
	
	@Override
	public int getPriority() {
		return 2;
	}
}
