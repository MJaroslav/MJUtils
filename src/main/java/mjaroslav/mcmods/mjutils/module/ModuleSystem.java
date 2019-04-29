package mjaroslav.mcmods.mjutils.module;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Used to connect the modular system to the modification. Installation. Create
 * an instance of the handler with modification id in the argument. Call in each
 * initialization event of modification using the same method from the handler.
 * Example: preInit({@link FMLPreInitializationEvent} e) {handler.preInit(e)}.
 * Add initSystem({@link FMLConstructionEvent} e) method to the a similar event
 * in the main class. Register your modules through the {@link Module}
 * annotation and {@link Modular} interface.
 *
 * @author MJaroslav
 */
public class ModuleSystem {
    private static final Logger LOGGER = LogManager.getLogger("Module System");
    private static final Comparator<Modular> ASCENDING_COMPARATOR = (o1, o2) -> o2.priority() - o1.priority();

    private final String modID;
    private final FileBasedConfiguration config;
    private final Proxy proxy;
    private final List<Modular> modules = new ArrayList<>();
    private final Set<CustomConfiguration> configurations = new HashSet<>();


    public ModuleSystem(String modID) {
        this(modID, null, null);
    }

    public ModuleSystem(String modID, FileBasedConfiguration config, Proxy proxy,
                        CustomConfiguration... customConfigurations) {
        this.modID = modID;
        this.config = config;
        this.proxy = proxy;
        configurations.addAll(Arrays.asList(customConfigurations));
    }

    public void initSystem(FMLConstructionEvent event) {
        LOGGER.log(Level.INFO, String.format("Looking for modules for \"%s\"", modID));
        Iterator<ASMData> iterator = event.getASMHarvestedData().getAll(Module.class.getName()).iterator();
        int count = 0;
        while (iterator.hasNext()) {
            ASMData data = iterator.next();
            if (data.getAnnotationInfo().get("value").equals(modID)) {
                try {
                    Object instance = Class.forName(data.getClassName()).newInstance();
                    if (instance instanceof Modular) {
                        modules.add((Modular) instance);
                        LOGGER.info("Found module for \"" + modID + "\": \"" + ((Modular) instance).name()
                                + "\" with priority " + ((Modular) instance).priority());
                        count++;
                    }
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        modules.sort(ASCENDING_COMPARATOR);
        LOGGER.info("Search finished, found " + count + " module" + (count == 1 ? "" : "s"));
        constuct(event);
    }

    private void constuct(FMLConstructionEvent event) {
        LOGGER.info(String.format("Construction of \"%s\"", modID));
        if (config != null)
            config.construct(event);
        for (CustomConfiguration config : configurations)
            config.construct(event);
        for (Modular module : modules)
            if (module.canLoad() && modsIsLoaded(module.dependencies()))
                module.construct(event);
        if (proxy != null)
            proxy.construct(event);
    }

    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info(String.format("Pre initialization of \"%s\"", modID));
        if (config != null)
            config.preInit(event);
        for (CustomConfiguration config : configurations)
            config.preInit(event);
        for (Modular module : modules)
            if (module.canLoad() && modsIsLoaded(module.dependencies()))
                module.preInit(event);
        if (proxy != null)
            proxy.preInit(event);
    }

    public void init(FMLInitializationEvent event) {
        LOGGER.info(String.format("Initialization of \"%s\"", modID));
        if (config != null)
            config.init(event);
        for (CustomConfiguration config : configurations)
            config.init(event);
        for (Modular module : modules)
            if (module.canLoad() && modsIsLoaded(module.dependencies()))
                module.init(event);
        if (proxy != null)
            proxy.init(event);
    }

    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info(String.format("Post initialization of \"%s\"", modID));
        if (config != null)
            config.postInit(event);
        for (CustomConfiguration config : configurations)
            config.postInit(event);
        for (Modular module : modules)
            if (module.canLoad() && modsIsLoaded(module.dependencies()))
                module.postInit(event);
        if (proxy != null)
            proxy.postInit(event);
    }

    public String getModID() {
        return modID;
    }

    /**
     * Get list of modules. Use
     * {@link ModuleSystem#initSystem(FMLConstructionEvent)} for search.
     *
     * @return List of found modules of this handler.
     */
    public List<Modular> getModules() {
        return modules;
    }

    public static boolean modsIsLoaded(String... modIDs) {
        if (modIDs != null && modIDs.length > 0)
            for (String modID : modIDs)
                if (!Loader.isModLoaded(modID))
                    return false;
        return true;
    }
}
