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

/**
 * Used to connect the modular system to the modification. Installation. Create
 * an instance of the handler with modification id in the argument. Call in each
 * initialization event of modification using the same method from the handler.
 * Example: preInit({@link FMLPreInitializationEvent} e) {handler.preInit(e)}.
 * Add findModules({@link FMLConstructionEvent} e) method to the a similar event
 * in the main class. Register your modules through the {@link ModInitModule}
 * annotation and {@link IModModule} interface.
 * 
 * @author MJaroslav
 *
 */
public class ModInitHandler {
	/**
	 * Modification id.
	 */
	private String modid;
	/**
	 * Logger for module system
	 */
	public static Logger logger = LogManager.getLogger("MJUtils Module System");
	/**
	 * List of mod modules, use {@link ModInitHandler#getModules()}.
	 */
	private ArrayList<IModModule> modules = new ArrayList<IModModule>();

	/**
	 * New modification initialization handler.
	 * 
	 * @param modid
	 *            - ID of modification. Do not use someone else's ID.
	 */
	public ModInitHandler(String modid) {
		this.modid = modid;
	}

	/**
	 * Find all modules for this handler (modification). Called in
	 * {@link FMLConstructionEvent}.
	 * 
	 * @param event
	 *            - main class construction event.
	 */
	public void findModules(FMLConstructionEvent event) {
		this.modules.clear();
		logger.log(Level.INFO, "Looking for modules for \"" + this.modid + "\"");
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
						this.modules.add((IModModule) instance);
						logger.info(
								"Found module for \"" + this.modid + "\": \"" + ((IModModule) instance).getModuleName()
										+ "\" with priority " + ((IModModule) instance).getPriority());
						count++;
					}
				}
			}
		}
		sort();
		logger.info("Search finished, found " + count + " module" + (count == 1 ? "" : "s"));
	}

	/**
	 * Sort modules by priority.
	 */
	public void sort() {
		ArrayList<IModModule> copyList = (ArrayList<IModModule>) this.modules.clone();
		this.modules.clear();
		int max = 0;
		int n = 0;
		while (!copyList.isEmpty()) {
			for (int id = 0; id < copyList.size(); id++) {
				max = Math.max(max, copyList.get(id).getPriority());
				if (max == copyList.get(id).getPriority())
					n = id;
			}
			this.modules.add(0, copyList.get(n));
			copyList.remove(n);
			max = 0;
		}
	}

	/**
	 * Called in a similar event of the main class.
	 * 
	 * @param event
	 *            - mail class pre init event.
	 */
	public void preInit(FMLPreInitializationEvent event) {
		logger.info("Pre initialization of \"" + this.modid + "\"");
		for (IModModule module : modules)
			module.preInit(event);
	}

	/**
	 * Called in a similar event of the main class.
	 * 
	 * @param event
	 *            - mail class init event.
	 */
	public void init(FMLInitializationEvent event) {
		logger.info("Initialization of \"" + this.modid + "\"");
		for (IModModule module : modules)
			module.init(event);
	}

	/**
	 * Called in a similar event of the main class.
	 * 
	 * @param event
	 *            - mail class post init event.
	 */
	public void postInit(FMLPostInitializationEvent event) {
		logger.info("Post initialization of \"" + this.modid + "\"");
		for (IModModule module : modules)
			module.postInit(event);
	}

	/**
	 * Get handler mod id.
	 * 
	 * @return Modification id of this handler.
	 */
	public String getModid() {
		return this.modid;
	}

	/**
	 * Get list of modules. Use
	 * {@link ModInitHandler#findModules(FMLConstructionEvent)} for search.
	 * 
	 * @return List of found modules of this handler.
	 */
	public ArrayList<IModModule> getModules() {
		return this.modules;
	}
}
