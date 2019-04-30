package mjaroslav.mcmods.mjutils.module;

import com.mojang.realmsclient.util.Pair;
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
import java.util.*;

import static mjaroslav.util.FieldType.get;

public class AnnotationBasedConfiguration extends FileBasedConfiguration {
    private final Set<Class> rawCategories = new HashSet<>();

    private final Map<String, Pair<ConfigurationCategory, Set<Pair<ConfigurationProperty, Field>>>> categories
            = new HashMap<>();

    private boolean hasCache = false;

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
    public void readFields() {
        if (!hasCache) {
            for (Class categoryClass : rawCategories)
                addCategoryClass(categoryClass, null);
            hasCache = false;
        }
        categories.forEach((name, category) -> category.second().forEach(property ->
                parseField(property.second(), property.first(), category.first(), name)));
    }

    private void addCategoryClass(Class categoryClass, String parentName) {
        ConfigurationCategory configInfo =
                (ConfigurationCategory) categoryClass.getAnnotation(ConfigurationCategory.class);
        String name = configInfo.name();
        boolean world = configInfo.requiresWorldRestart();
        boolean mc = configInfo.requiresMcRestart();
        if (UtilsJava.stringIsNotEmpty(parentName))
            name = String.format("%s.%s", parentName, name);
        getInstance().addCustomCategoryComment(name, configInfo.comment());
        getInstance().setCategoryRequiresMcRestart(name, mc);
        getInstance().setCategoryRequiresWorldRestart(name, world);
        categories.put(name, Pair.of(configInfo, findFields(categoryClass)));
        for (Class memberClass : categoryClass.getDeclaredClasses())
            if (memberClass.isAnnotationPresent(ConfigurationCategory.class))
                addCategoryClass(memberClass, name);
    }

    private Set<Pair<ConfigurationProperty, Field>> findFields(Class categoryClass) {
        Set<Pair<ConfigurationProperty, Field>> result = new HashSet<>();
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
                        result.add(Pair.of(field.getAnnotation(ConfigurationProperty.class), field));
                else logger.warn(String.format("Field \"%s\" in \"%s\" will be ignored. @ConfigurationProperty must " +
                        "be public, static and not final", field.getName(), categoryClass.getName()));
        }
        return result;
    }

    private void parseField(Field field, ConfigurationProperty info, ConfigurationCategory categoryInfo,
                            String categoryFullName) {
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
                    if (info.useFloat())
                        value = (float) ((double) value);
                    break;
                case DOUBLE_ARRAY:
                    value = instance.get(categoryFullName, name, info.defaultDoubleArray(), info.comment(),
                            info.minDouble(), info.maxDouble(), info.listLengthFixed(), info.maxListLength())
                            .setRequiresMcRestart(mcRestart).setRequiresWorldRestart(worldRestart).getDoubleList();
                    if (info.useFloat())
                        value = UtilsJava.toFloatArray((double[]) value);
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

    @SuppressWarnings("unchecked")
    public List<IConfigElement> categoryToElementList(String name) {
        return new ConfigElement(getInstance().getCategory(name)).getChildElements();
    }

    public List<IConfigElement> generalToElementList() {
        return categoryToElementList(Configuration.CATEGORY_GENERAL);
    }
}
