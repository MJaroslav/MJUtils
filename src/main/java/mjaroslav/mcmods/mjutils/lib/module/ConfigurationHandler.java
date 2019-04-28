package mjaroslav.mcmods.mjutils.lib.module;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import mjaroslav.util.UtilsJava;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static mjaroslav.util.FieldType.get;

public class ConfigurationHandler extends ConfigurationBase {
    private final String modid;
    private Configuration instance;
    private final ArrayList<Class> fieldClasses = new ArrayList<>();
    private final Logger logger;

    public ConfigurationHandler(String modid, Logger logger) {
        super();
        this.modid = modid;
        this.logger = logger;
    }

    @Override
    public Configuration getInstance() {
        return instance;
    }

    @Override
    public void setInstance(Configuration newConfig) {
        instance = newConfig;
    }

    @Override
    public String getModId() {
        return modid;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public void construct(FMLConstructionEvent event) {
        fieldClasses.clear();
        logger.log(Level.INFO, "Looking for config categories for \"" + modid + "\"");
        Iterator<ASMData> iterator = event.getASMHarvestedData().getAll(ConfigCategory.class.getName()).iterator();
        int count = 0;
        while (iterator.hasNext()) {
            ASMData data = iterator.next();
            if (data.getAnnotationInfo().get("modid").equals(modid)) {
                try {
                    fieldClasses.add(Class.forName(data.getClassName()));
                    count++;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("Search finished, found " + count + " categor" + (count == 1 ? "y" : "ies"));
    }

    @Override
    public void readFields() {
        for (Class fieldClass : fieldClasses) {
            for (Field field : fieldClass.getFields()) {
                int mods = field.getModifiers();
                if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && !Modifier.isFinal(mods)
                        && field.getAnnotation(ConfigField.class) != null)
                    parseField(field, field.getAnnotation(ConfigField.class),
                            (ConfigCategory) fieldClass.getAnnotation(ConfigCategory.class), getInstance());
            }
            if (fieldClass.isAnnotationPresent(ConfigCategory.class)) {
                ConfigCategory configInfo = (ConfigCategory) fieldClass.getAnnotation(ConfigCategory.class);
                getInstance().addCustomCategoryComment(configInfo.name(), configInfo.comment());
                getInstance().setCategoryRequiresMcRestart(configInfo.name(), configInfo.requiresMcRestart());
                getInstance().setCategoryRequiresWorldRestart(configInfo.name(), configInfo.requiresWorldRestart());
            }
        }
    }

    // Parse (and set) field from info.
    private static void parseField(Field field, ConfigField info, ConfigCategory categoryInfo, Configuration instance) {
        try {
            String name;
            if (UtilsJava.stringIsNotEmpty(info.customName()))
                name = info.customName();
            else {
                name = field.getName();
                if (Character.isUpperCase(name.charAt(0)))
                    name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                name = name.replaceAll("[A-Z]", "_$0").toLowerCase();
            }
            String category = info.category();
            boolean worldRestart = info.requiresWorldRestart();
            boolean mcRestart = info.requiresMcRestart();
            if (categoryInfo != null) {
                category = categoryInfo.name();
                worldRestart = info.requiresWorldRestart() || worldRestart;
                mcRestart = info.requiresMcRestart() || mcRestart;
            }
            boolean flag = true;
            Object value = null;
            switch (get(field)) {
                case FLOAT:
                    value = (float) instance
                            .get(category, name, info.defaultFloat(), info.comment(), info.minFloat(), info.maxFloat())
                            .setRequiresMcRestart(mcRestart).setRequiresWorldRestart(worldRestart).getDouble();
                    break;
                case FLOAT_ARRAY:
                    value = UtilsJava.toFloatArray(instance
                            .get(category, name, UtilsJava.toDoubleArray(info.defaultFloatArray()), info.comment(),
                                    info.minFloat(), info.maxFloat(), info.listLengthFixed(), info.maxListLength())
                            .setRequiresMcRestart(mcRestart).setRequiresWorldRestart(worldRestart).getDoubleList());
                    break;
                case STRING:
                    value = instance.get(category, name, info.defaultString(), info.comment(), info.validValues())
                            .setRequiresMcRestart(mcRestart).setRequiresWorldRestart(worldRestart).getString();
                    break;
                case STRING_ARRAY:
                    value = instance.get(category, name, info.defaultStringArray(), info.comment())
                            .setMaxListLength(info.maxListLength()).setIsListLengthFixed(info.listLengthFixed())
                            .setValidValues(info.validValues()).setRequiresMcRestart(mcRestart)
                            .setRequiresWorldRestart(worldRestart).getStringList();
                    break;
                case INT:
                    value = instance.get(category, name, info.defaultInt(), info.comment()).setRequiresMcRestart(mcRestart)
                            .setRequiresWorldRestart(worldRestart).getInt();
                    break;
                case INT_ARRAY:
                    value = instance.get(category, name, info.defaultIntArray(), info.comment(), info.minInt(),
                            info.maxInt(), info.listLengthFixed(), info.maxListLength()).getIntList();
                    break;
                case BOOLEAN:
                    value = instance.get(category, name, info.defaultBoolean(), info.comment())
                            .setRequiresMcRestart(mcRestart).setRequiresWorldRestart(worldRestart).getBoolean();
                    break;
                case BOOLEAN_ARRAY:
                    value = instance.get(category, name, info.defaultBooleanArray(), info.comment())
                            .setIsListLengthFixed(info.listLengthFixed()).setMaxListLength(info.maxListLength())
                            .setRequiresWorldRestart(worldRestart).setRequiresMcRestart(mcRestart).getBooleanList();
                    break;
                default:
                    flag = false;
                    break;
            }
            if (flag)
                field.set(null, value);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<IConfigElement> toElementList(String name) {
        return new ConfigElement(getInstance().getCategory(name)).getChildElements();
    }

    public List<IConfigElement> toElementList() {
        return toElementList(Configuration.CATEGORY_GENERAL);
    }
}
