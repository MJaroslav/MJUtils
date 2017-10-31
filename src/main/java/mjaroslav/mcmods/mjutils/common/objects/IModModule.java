package mjaroslav.mcmods.mjutils.common.objects;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Add a {@link ModInitModule} annotation to connect a module to modification.
 * 
 * @author MJaroslav
 *
 */
public interface IModModule {
	/**
	 * Module name, optional. Use 'Proxy' for proxy modules and 'Config' for
	 * configuration module, they will be ignored when searching.
	 */
	public String getModuleName();

	/**
	 * Priority of module loading. If you use an existing level, then this
	 * module will shift the rest. If you use an extra-high level, the module
	 * will be installed at the end of the list.
	 * 
	 * @return Priority: 0 - first, 1 - second...
	 */
	public int getPriority();

	/**
	 * Called in a similar method of the main modification class. Do not call
	 * this method elsewhere!
	 */
	public void preInit(FMLPreInitializationEvent event);

	/**
	 * Called in a similar method of the main modification class. Do not call
	 * this method elsewhere!
	 */
	public void init(FMLInitializationEvent event);

	/**
	 * Called in a similar method of the main modification class. Do not call
	 * this method elsewhere!
	 */
	public void postInit(FMLPostInitializationEvent event);
}
