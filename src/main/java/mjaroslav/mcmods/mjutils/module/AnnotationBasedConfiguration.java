package mjaroslav.mcmods.mjutils.module;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static mjaroslav.util.FieldType.get;

public class AnnotationBasedConfiguration extends FileBasedConfiguration {
    private final Set<Class> mainCategories = new HashSet<>();

    public AnnotationBasedConfiguration(String modID, Logger logger) {
        super(modID, logger);
    }

    @Override
    public void construct(FMLConstructionEvent event) {
        logger.log(Level.INFO, String.format("Looking for configuration categories for \"%s\" mod.", modID));
        Iterator<ASMData> iterator = event.getASMHarvestedData()
                .getAll(ConfigurationCategory.class.getName()).iterator();
        int count = 0;
        ASMData data;
        while (iterator.hasNext()) {
            data = iterator.next();
            if (data.getAnnotationInfo().get("modID").equals(modID)) {
                try {
                    Class category = Class.forName(data.getClassName());
                    if (!category.isMemberClass())
                        mainCategories.add(category);
                    count++;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info(String.format("Search finished, found %s categor%s.", count, (count == 1 ? "y" : "ies")));
    }

    @Override
    public void readFields() {
        for (Class categoryClass : mainCategories)
            parseCategoryClass(categoryClass, null, false, false, getInstance());
    }

    private static void parseCategoryClass(Class categoryClass, String parentName, boolean parentWorld,
                                           boolean parentMC, Configuration instance) {
        ConfigurationCategory configInfo =
                (ConfigurationCategory) categoryClass.getAnnotation(ConfigurationCategory.class);
        String name = configInfo.name();
        boolean world = configInfo.requiresWorldRestart();
        boolean mc = configInfo.requiresMcRestart();
        if (UtilsJava.stringIsNotEmpty(parentName))
            name = String.format("%s.%s", parentName, name);
        if (parentWorld)
            world = true;
        if (parentMC)
            mc = true;
        instance.addCustomCategoryComment(name, configInfo.comment());
        instance.setCategoryRequiresMcRestart(name, mc);
        instance.setCategoryRequiresWorldRestart(name, world);
        int mods;
        for (Field field : categoryClass.getFields()) {
            mods = field.getModifiers();
            if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && !Modifier.isFinal(mods)
                    && field.getDeclaredAnnotation(ConfigurationProperty.class) != null)
                parseField(field, field.getAnnotation(ConfigurationProperty.class),
                        (ConfigurationCategory) categoryClass.getAnnotation(ConfigurationCategory.class), instance);
        }
        for (Class memberClass : categoryClass.getDeclaredClasses())
            if (memberClass.isAnnotationPresent(ConfigurationCategory.class))
                parseCategoryClass(memberClass, name, world, mc, instance);
    }

    private static void parseField(Field field, ConfigurationProperty info, ConfigurationCategory categoryInfo,
                                   Configuration instance) {
        try {
            String name;
            if (UtilsJava.stringIsNotEmpty(info.name()))
                name = info.name();
            else {
                name = field.getName();
                if (Character.isUpperCase(name.charAt(0)))
                    name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                name = name.replaceAll("[A-Z]", "_$0").toLowerCase();
            }
            String category = categoryInfo.name();
            boolean worldRestart = info.requiresWorldRestart();
            boolean mcRestart = info.requiresMcRestart();
            worldRestart = categoryInfo.requiresWorldRestart() || worldRestart;
            mcRestart = categoryInfo.requiresMcRestart() || mcRestart;
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
                    value = instance.get(category, name, info.defaultInt(), info.comment())
                            .setRequiresMcRestart(mcRestart).setRequiresWorldRestart(worldRestart).getInt();
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
    public List<IConfigElement> categoryToElementList(String name) {
        return new ConfigElement(getInstance().getCategory(name)).getChildElements();
    }

    public List<IConfigElement> generalToElementList() {
        return categoryToElementList(Configuration.CATEGORY_GENERAL);
    }
}
