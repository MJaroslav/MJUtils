package mjaroslav.mcmods.mjutils.common.init;

import cpw.mods.fml.common.event.*;
import mjaroslav.mcmods.mjutils.MJInfo;
import mjaroslav.mcmods.mjutils.common.objects.IModModule;
import mjaroslav.mcmods.mjutils.common.objects.ModInitModule;
import mjaroslav.mcmods.mjutils.common.reaction.ReactionEventHandler;
import net.minecraftforge.common.MinecraftForge;

@ModInitModule(modid = MJInfo.MODID)
public class ReactionModule implements IModModule {
  @Override
  public String getModuleName() {
    return "Reaction";
  }

  @Override
  public int getPriority() {
    return 3;
  }

  @Override
  public void preInit(FMLPreInitializationEvent event) {
  }

  @Override
  public void init(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(new ReactionEventHandler());
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
  }
  
  @Override
  public String[] modDependencies() {
    return null;
  }

  @Override
  public boolean canLoad() {
    return true;
  }
}
