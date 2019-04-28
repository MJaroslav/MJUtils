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
 * Add initHandler({@link FMLConstructionEvent} e) method to the a similar event
 * in the main class. Register your modules through the {@link Module}
 * annotation and {@link Modular} interface.
 *
 * @author MJaroslav
 */
public class ModuleHandler {
    private static final Logger logger = LogManager.getLogger("Module System");
    private final String modid;
    private final ConfigurationBase config;
    private final ProxyBase proxy;
    private final ArrayList<Modular> modules = new ArrayList<>();

    private static final Comparator<Modular> ASCENDING_COMPARATOR = (o1, o2) -> o2.getPriority() - o1.getPriority();

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

    public void initHandler(FMLConstructionEvent event) {
        modules.clear();
        logger.log(Level.INFO, "Looking for modules for \"" + modid + "\"");
        iterator = event.getASMHarvestedData().getAll(Module.class.getName()).iterator();
        registerModules();
        constuct(event);
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
                        if (instance instanceof Modular) {
                            modules.add((Modular) instance);
                            logger.info("Found module for \"" + modid + "\": \"" + ((Modular) instance).getModuleName()
                                    + "\" with priority " + ((Modular) instance).getPriority());
                            count++;
                        }
                    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            modules.sort(ASCENDING_COMPARATOR);
        }
        logger.info("Search finished, found " + count + " module" + (count == 1 ? "" : "s"));
    }

    public void constuct(FMLConstructionEvent event) {
        logger.info("Construction of \"" + modid + "\"");
        if (config != null)
            config.construct(event);
        for (Modular module : modules)
            if (module.canLoad() && modsIsLoaded(module.modDependencies()))
                module.construct(event);
        if (proxy != null)
            proxy.construct(event);
    }

    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Pre initialization of \"" + modid + "\"");
        if (config != null)
            config.preInit(event);
        for (Modular module : modules)
            if (module.canLoad() && modsIsLoaded(module.modDependencies()))
                module.preInit(event);
        if (proxy != null)
            proxy.preInit(event);
    }

    public void init(FMLInitializationEvent event) {
        logger.info("Initialization of \"" + modid + "\"");
        if (config != null)
            config.init(event);
        for (Modular module : modules)
            if (module.canLoad() && modsIsLoaded(module.modDependencies()))
                module.init(event);
        if (proxy != null)
            proxy.init(event);
    }

    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Post initialization of \"" + modid + "\"");
        if (config != null)
            config.postInit(event);
        for (Modular module : modules)
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
     * {@link ModuleHandler#initHandler(FMLConstructionEvent)} for search.
     *
     * @return List of found modules of this handler.
     */
    public ArrayList<Modular> getModules() {
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
