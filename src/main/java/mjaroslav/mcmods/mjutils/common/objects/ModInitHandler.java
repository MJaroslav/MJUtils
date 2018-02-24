package mjaroslav.mcmods.mjutils.common.objects;

import java.util.*;

import org.apache.logging.log4j.*;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.*;

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
 */
public class ModInitHandler {
    /**
     * Logger for module system
     */
    public static Logger logger = LogManager.getLogger("MJUtils Module System");
    private static Comparator<IModModule> ASCENDING_COMPARATOR = new Comparator<IModModule>() {

        @Override
        public int compare(IModModule o1, IModModule o2) {
            return o2.getPriority() - o1.getPriority();
        }
    };
    /**
     * Modification id.
     */
    private String modid;
    /**
     * Modification configuration handler.
     */
    private ConfigurationBase config;
    /**
     * Modification proxy handler.
     */
    private ProxyBase proxy;
    /**
     * List of modification modules, use {@link ModInitHandler#getModules()}.
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
     * New modification initialization handler.
     * 
     * @param modid
     *            - ID of modification. Do not use someone else's ID.
     * @param config
     *            - instance of configuration handler.
     * @param proxy
     *            - instance of proxy handler.
     */
    public ModInitHandler(String modid, ConfigurationBase config, ProxyBase proxy) {
        this.modid = modid;
        this.config = config;
        this.proxy = proxy;
    }

    /**
     * Find all modules for this handler (modification). Called in
     * {@link FMLConstructionEvent}.
     *
     * @param event
     *            - main class construction event.
     */
    public void findModules(FMLConstructionEvent event) {
        modules.clear();
        logger.log(Level.INFO, "Looking for modules for \"" + modid + "\"");
        iterator = event.getASMHarvestedData().getAll(ModInitModule.class.getName()).iterator();
        if (config instanceof ConfigurationHandler)
            ((ConfigurationHandler) config).findCategories(event);
    }

    private Iterator<ASMData> iterator;

    private void registerModules() {
        int count = 0;
        if (iterator != null) {
            while (iterator.hasNext()) {
                ASMData data = iterator.next();
                if (data.getAnnotationInfo().get("modid").equals(modid)) {
                    try {
                        Object instance = Class.forName(data.getClassName()).newInstance();
                        if (instance != null && instance instanceof IModModule
                                && !((IModModule) instance).getModuleName().equals("Config")
                                && !((IModModule) instance).getModuleName().equals("Proxy")) {
                            modules.add((IModModule) instance);
                            logger.info("Found module for \"" + this.modid + "\": \""
                                    + ((IModModule) instance).getModuleName() + "\" with priority "
                                    + ((IModModule) instance).getPriority());
                            count++;
                        }
                    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            Collections.sort(modules, ASCENDING_COMPARATOR);
        }
        logger.info("Search finished, found " + count + " module" + (count == 1 ? "" : "s"));
    }

    /**
     * Called in a similar event of the main class.
     *
     * @param event
     *            - mail class pre init event.
     */
    public void preInit(FMLPreInitializationEvent event) {
        registerModules();
        logger.info("Pre initialization of \"" + modid + "\"");
        if (config != null)
            config.preInit(event);
        for (IModModule module : modules)
            if (module.canLoad() && modsIsLoaded(module.modDependencies()))
                module.preInit(event);
        if (proxy != null)
            proxy.preInit(event);
    }

    /**
     * Called in a similar event of the main class.
     *
     * @param event
     *            - mail class init event.
     */
    public void init(FMLInitializationEvent event) {
        logger.info("Initialization of \"" + modid + "\"");
        if (config != null)
            config.init(event);
        for (IModModule module : modules)
            if (module.canLoad() && modsIsLoaded(module.modDependencies()))
                module.init(event);
        if (proxy != null)
            proxy.init(event);
    }

    /**
     * Called in a similar event of the main class.
     *
     * @param event
     *            - mail class post init event.
     */
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Post initialization of \"" + modid + "\"");
        if (config != null)
            config.postInit(event);
        for (IModModule module : modules)
            if (module.canLoad() && modsIsLoaded(module.modDependencies()))
                module.postInit(event);
        if (proxy != null)
            proxy.postInit(event);
    }

    /**
     * Get handler mod id.
     *
     * @return Modification id of this handler.
     */
    public String getModid() {
        return modid;
    }

    /**
     * Get list of modules. Use
     * {@link ModInitHandler#findModules(FMLConstructionEvent)} for search.
     *
     * @return List of found modules of this handler.
     */
    public ArrayList<IModModule> getModules() {
        return modules;
    }

    /**
     * Check some mods on existing.
     * 
     * @param modids
     *            - mod ids.
     * @return True if all is loaded.
     */
    public static boolean modsIsLoaded(String... modids) {
        if (modids != null && modids.length > 0)
            for (String modid : modids)
                if (!Loader.isModLoaded(modid))
                    return false;
        return true;
    }
}
