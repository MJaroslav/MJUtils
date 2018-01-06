package mjaroslav.mcmods.mjutils.common.objects;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.*;

/**
 * Add a {@link ModInitModule} annotation to connect a module to modification.
 *
 * @author MJaroslav
 */
public interface IModModule {
  /**
   * Module name, optional. Use 'Proxy' for proxy modules and 'Config' for
   * configuration module, they will be ignored when searching.
   *
   * @return Module name.
   */
  public String getModuleName();

  /**
   * Priority of module loading. If you use an existing level, then this module
   * will shift the rest. If you use an extra-high level, the module will be
   * installed at the end of the list.
   *
   * @return Priority: 0 - first, 1 - second...
   */
  public int getPriority();

  /**
   * Called in a similar method of the main modification class. Do not call this
   * method elsewhere!
   *
   * @param event - event from mail class.
   */
  public void preInit(FMLPreInitializationEvent event);

  /**
   * Called in a similar method of the main modification class. Do not call this
   * method elsewhere!
   *
   * @param event - event from mail class.
   */
  public void init(FMLInitializationEvent event);

  /**
   * Called in a similar method of the main modification class. Do not call this
   * method elsewhere!
   *
   * @param event - event from mail class.
   */
  public void postInit(FMLPostInitializationEvent event);

  /**
   * Will be checked in {@link Loader#isModLoaded(String)}.
   * 
   * @return Array of modid.
   */
  public String[] modDependencies();

  /**
   * Can load module.
   * 
   * @return Set true if you wont use it.
   */
  public boolean canLoad();
}
