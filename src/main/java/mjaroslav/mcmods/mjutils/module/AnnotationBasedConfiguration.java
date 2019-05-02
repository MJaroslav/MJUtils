package mjaroslav.mcmods.mjutils.module;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import mjaroslav.util.FieldType;
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

/**
 * Use this object instead of FileBasedConfiguration
 * to create a configuration via annotations.
 *
 * @see ConfigurationCategory
 * @see mjaroslav.mcmods.mjutils.module.FileBasedConfiguration
 */
// TODO: Оптимизировать поиск опций и, возможно, разбить основную аннотацию на составные.
public class AnnotationBasedConfiguration extends FileBasedConfiguration {
    private final Set<Class> rawCategories = new HashSet<>();

    private final Set<Property> properties = new HashSet<>();

    private boolean hasCache = false;

    /**
     * See class documentation.
     *
     * @param modID  see {@link FileBasedConfiguration#modID}
     * @param logger see {@link FileBasedConfiguration#logger}
     */
    public AnnotationBasedConfiguration(String modID, Logger logger) {
        super(modID, logger);
    }

    @Override
    public void construct(FMLConstructionEvent event) {
        logger.log(Level.INFO, String.format("Looking for configuration categories for \"%s\".", modID));
        Iterator<ASMData> iterator = event.getASMHarvestedData()
                .getAll(ConfigurationCategory.class.getName()).iterator();
        int count = 0;
        ASMData data;
        while (iterator.hasNext()) {
            data = iterator.next();
            if (UtilsJava.stringsNotNullAndEquals((String) data.getAnnotationInfo().get("modID"), modID))
                try {
                    Class category = Class.forName(data.getClassName());
                    if (!category.isMemberClass())
                        rawCategories.add(category);
                    count++;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
        }
        logger.info(String.format("Search finished, found %s categor%s.", count, (count == 1 ? "y" : "ies")));
    }

    @Override
    public void readFields(Configuration instance) {
        if (!hasCache) {
            for (Class categoryClass : rawCategories)
                cacheProperties(categoryClass, null, instance);
            hasCache = false;
        }
        properties.forEach(property -> parseField(property.field, property.info, property.categoryInfo,
                property.categoryFullName, instance));
    }

    private void cacheProperties(Class categoryClass, String parentName, Configuration instance) {
        ConfigurationCategory configInfo =
                (ConfigurationCategory) categoryClass.getAnnotation(ConfigurationCategory.class);
        String name = configInfo.name();
        boolean world = configInfo.requiresWorldRestart();
        boolean mc = configInfo.requiresMcRestart();
        if (UtilsJava.stringIsNotEmpty(parentName))
            name = String.format("%s.%s", parentName, name);
        instance.addCustomCategoryComment(name, configInfo.comment());
        instance.setCategoryRequiresMcRestart(name, mc);
        instance.setCategoryRequiresWorldRestart(name, world);
        int mods;
        for (Field field : categoryClass.getFields()) {
            mods = field.getModifiers();
            if (field.getDeclaredAnnotation(ConfigurationProperty.class) != null)
                if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && !Modifier.isFinal(mods))
                    if (FieldType.get(field) == FieldType.UNKNOWN)
                        logger.warn(String.format("Field \"%s\" in \"%s\" will be ignored. @ConfigurationProperty " +
                                        "must be float or double or boolean or int and their array type.",
                                field.getName(), categoryClass.getName()));
                    else
                        properties.add(new Property(field, field.getAnnotation(ConfigurationProperty.class), configInfo,
                                name));
                else logger.warn(String.format("Field \"%s\" in \"%s\" will be ignored. @ConfigurationProperty must " +
                        "be public, static and not final", field.getName(), categoryClass.getName()));
        }
        for (Class memberClass : categoryClass.getDeclaredClasses())
            if (memberClass.isAnnotationPresent(ConfigurationCategory.class))
                cacheProperties(memberClass, name, instance);
    }

    private void parseField(Field field, ConfigurationProperty info, ConfigurationCategory categoryInfo,
                            String categoryFullName, Configuration instance) {
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
            boolean worldRestart = info.requiresWorldRestart();
            boolean mcRestart = info.requiresMcRestart();
            worldRestart = categoryInfo.requiresWorldRestart() || worldRestart;
            mcRestart = categoryInfo.requiresMcRestart() || mcRestart;
            boolean flag = true;
            Object value = null;
            switch (get(field)) {
                case DOUBLE:
                    value = instance.get(categoryFullName, name, info.defaultDouble(), info.comment(), info.minDouble(),
                            info.maxDouble()).setRequiresMcRestart(mcRestart).
                            setRequiresWorldRestart(worldRestart).getDouble();
                    break;
                case DOUBLE_ARRAY:
                    value = instance.get(categoryFullName, name, info.defaultDoubleArray(), info.comment(),
                            info.minDouble(), info.maxDouble(), info.listLengthFixed(), info.maxListLength())
                            .setRequiresMcRestart(mcRestart).setRequiresWorldRestart(worldRestart).getDoubleList();
                    break;
                case STRING:
                    value = instance.get(categoryFullName, name, info.defaultString(), info.comment(),
                            info.validValues()).setRequiresMcRestart(mcRestart)
                            .setRequiresWorldRestart(worldRestart).getString();
                    break;
                case STRING_ARRAY:
                    value = instance.get(categoryFullName, name, info.defaultStringArray(), info.comment())
                            .setMaxListLength(info.maxListLength()).setIsListLengthFixed(info.listLengthFixed())
                            .setValidValues(info.validValues()).setRequiresMcRestart(mcRestart)
                            .setRequiresWorldRestart(worldRestart).getStringList();
                    break;
                case INT:
                    value = instance.get(categoryFullName, name, info.defaultInt(), info.comment())
                            .setRequiresMcRestart(mcRestart).setRequiresWorldRestart(worldRestart).getInt();
                    break;
                case INT_ARRAY:
                    value = instance.get(categoryFullName, name, info.defaultIntArray(), info.comment(), info.minInt(),
                            info.maxInt(), info.listLengthFixed(), info.maxListLength()).getIntList();
                    break;
                case BOOLEAN:
                    value = instance.get(categoryFullName, name, info.defaultBoolean(), info.comment())
                            .setRequiresMcRestart(mcRestart).setRequiresWorldRestart(worldRestart).getBoolean();
                    break;
                case BOOLEAN_ARRAY:
                    value = instance.get(categoryFullName, name, info.defaultBooleanArray(), info.comment())
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
            logger.error(e);
        }
    }

    /**
     * Use to get a list of all IConfigElements for the
     * specified category. Used
     * for {@link cpw.mods.fml.client.config.GuiConfig}.
     *
     * @param name full category name. Must be in lower case.
     * @return List of IConfigElement for specified category.
     * @throws NullPointerException if the specified category
     *                              does not exist.
     */
    @SuppressWarnings("unchecked")
    public List<IConfigElement> categoryToElementList(String name) {
        return new ConfigElement(getInstance().getCategory(name)).getChildElements();
    }

    /**
     * Typically, the configuration consists of a general
     * category and its subcategories. You can use this to
     * get a list of all IConfigElement for this category.
     *
     * @return List of IConfigElement for
     * {@link Configuration#CATEGORY_GENERAL}
     * @throws NullPointerException if general category does
     *                              not exist.
     * @see AnnotationBasedConfiguration#categoryToElementList(String)
     */
    public List<IConfigElement> generalToElementList() {
        return categoryToElementList(Configuration.CATEGORY_GENERAL);
    }

    private static class Property {
        private final ConfigurationCategory categoryInfo;
        private final ConfigurationProperty info;
        private final Field field;
        private final String categoryFullName;

        private Property(Field field, ConfigurationProperty info, ConfigurationCategory category, String
                categoryFullName) {
            this.info = info;
            this.field = field;
            this.categoryInfo = category;
            this.categoryFullName = categoryFullName;
        }

    }
}
