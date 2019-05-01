package mjaroslav.mcmods.mjutils.module;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.module.Initializator.Configurable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * A powerful feature that allows you to automate and
 * split the initialization of proxies, configurations
 * and the rest of the game content, which can be divided
 * into modules (for example, blocks and items).
 *
 * @see Module
 * @see Modular
 * @see Proxy
 * @see Configurable
 * @see FileBasedConfiguration
 * @see AnnotationBasedConfiguration
 */
public class ModuleSystem {
    private static final Logger LOGGER = LogManager.getLogger("Module System");
    private static final Comparator<Modular> ASCENDING_COMPARATOR = (o1, o2) -> o2.priority() - o1.priority();

    private final String modID;
    private final FileBasedConfiguration config;
    private final Proxy proxy;
    private final List<Modular> modules = new ArrayList<>();
    private final Set<Configurable> configurations = new HashSet<>();


    /**
     * See class documentation.
     *
     * @param modID owner mod modID.
     */
    public ModuleSystem(String modID) {
        this(modID, null, null);
    }

    /**
     * See class documentation.
     *
     * @param modID                owner mod modID.
     * @param config               see {@link FileBasedConfiguration}. Can be null.
     * @param proxy                see {@link Proxy}. Can be null.
     * @param customConfigurations see {@link Configurable}. Can be empty or null.
     */
    public ModuleSystem(String modID, FileBasedConfiguration config, Proxy proxy,
                        Configurable... customConfigurations) {
        this.modID = modID;
        this.config = config;
        this.proxy = proxy;
        if (customConfigurations != null)
            configurations.addAll(Arrays.asList(customConfigurations));
    }

    /**
     * Should be called in mod construction event.
     *
     * @param event event object from owner mod.
     */
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
                        count++;
                    }
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        modules.sort(ASCENDING_COMPARATOR);
        List<String> names = new ArrayList<>();
        modules.forEach(module -> names.add(module.name()));
        LOGGER.info(String.format("Search finished, found %s module%s [%s]", count, count == 1 ? "" : "s",
                String.join(", ", names)));
        constuct(event);
    }

    private void constuct(FMLConstructionEvent event) {
        LOGGER.info(String.format("Construction of \"%s\"", modID));
        if (config != null)
            config.construct(event);
        for (Configurable config : configurations)
            config.construct(event);
        for (Modular module : modules)
            if (module.canLoad() && modsIsLoaded(module.dependencies()))
                module.construct(event);
        if (proxy != null)
            proxy.construct(event);
    }

    /**
     * Should be called in mod pre initialization event.
     *
     * @param event event object from owner mod.
     */
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info(String.format("Pre initialization of \"%s\"", modID));
        if (config != null)
            config.preInit(event);
        for (Configurable config : configurations)
            config.preInit(event);
        for (Modular module : modules)
            if (module.canLoad() && modsIsLoaded(module.dependencies()))
                module.preInit(event);
        if (proxy != null)
            proxy.preInit(event);
    }

    /**
     * Should be called in mod initialization event.
     *
     * @param event event object from owner mod.
     */
    public void init(FMLInitializationEvent event) {
        LOGGER.info(String.format("Initialization of \"%s\"", modID));
        if (config != null)
            config.init(event);
        for (Configurable config : configurations)
            config.init(event);
        for (Modular module : modules)
            if (module.canLoad() && modsIsLoaded(module.dependencies()))
                module.init(event);
        if (proxy != null)
            proxy.init(event);
    }

    /**
     * Should be called in mod post initialization event.
     *
     * @param event event object from owner mod.
     */
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info(String.format("Post initialization of \"%s\"", modID));
        if (config != null)
            config.postInit(event);
        for (Configurable config : configurations)
            config.postInit(event);
        for (Modular module : modules)
            if (module.canLoad() && modsIsLoaded(module.dependencies()))
                module.postInit(event);
        if (proxy != null)
            proxy.postInit(event);
    }

    private static boolean modsIsLoaded(String... modIDs) {
        if (modIDs != null && modIDs.length > 0)
            for (String modID : modIDs)
                if (!Loader.isModLoaded(modID))
                    return false;
        return true;
    }
}
