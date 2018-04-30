package mjaroslav.mcmods.mjutils.lib.module;

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
 * in the main class. Register your modules through the {@link ModModule}
 * annotation and {@link IModule} interface.
 *
 * @author MJaroslav
 */
public class ModuleHandler {
    public static final Logger logger = LogManager.getLogger("MJUtils Module System");
    private final String modid;
    private final ConfigurationBase config;
    private final ProxyBase proxy;
    private final ArrayList<IModule> modules = new ArrayList<IModule>();

    private static final Comparator<IModule> ASCENDING_COMPARATOR = new Comparator<IModule>() {
        @Override
        public int compare(IModule o1, IModule o2) {
            return o2.getPriority() - o1.getPriority();
        }
    };

    public ModuleHandler(String modid) {
        this.modid = modid;
        config = null;
        proxy = null;
    }

    public ModuleHandler(String modid, ConfigurationBase config, ProxyBase proxy) {
        this.modid = modid;
        this.config = config;
        this.proxy = proxy;
    }

    public void findModules(FMLConstructionEvent event) {
        modules.clear();
        logger.log(Level.INFO, "Looking for modules for \"" + modid + "\"");
        iterator = event.getASMHarvestedData().getAll(ModModule.class.getName()).iterator();
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
                        if (instance != null && instance instanceof IModule) {
                            modules.add((IModule) instance);
                            logger.info("Found module for \"" + modid + "\": \"" + ((IModule) instance).getModuleName()
                                    + "\" with priority " + ((IModule) instance).getPriority());
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

    public void preInit(FMLPreInitializationEvent event) {
        registerModules();
        logger.info("Pre initialization of \"" + modid + "\"");
        if (config != null)
            config.preInit(event);
        for (IModule module : modules)
            if (module.canLoad() && modsIsLoaded(module.modDependencies()))
                module.preInit(event);
        if (proxy != null)
            proxy.preInit(event);
    }

    public void init(FMLInitializationEvent event) {
        logger.info("Initialization of \"" + modid + "\"");
        if (config != null)
            config.init(event);
        for (IModule module : modules)
            if (module.canLoad() && modsIsLoaded(module.modDependencies()))
                module.init(event);
        if (proxy != null)
            proxy.init(event);
    }

    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Post initialization of \"" + modid + "\"");
        if (config != null)
            config.postInit(event);
        for (IModule module : modules)
            if (module.canLoad() && modsIsLoaded(module.modDependencies()))
                module.postInit(event);
        if (proxy != null)
            proxy.postInit(event);
    }

    public String getModid() {
        return modid;
    }

    /**
     * Get list of modules. Use
     * {@link ModuleHandler#findModules(FMLConstructionEvent)} for search.
     *
     * @return List of found modules of this handler.
     */
    public ArrayList<IModule> getModules() {
        return modules;
    }

    public static boolean modsIsLoaded(String... modids) {
        if (modids != null && modids.length > 0)
            for (String modid : modids)
                if (!Loader.isModLoaded(modid))
                    return false;
        return true;
    }
}
