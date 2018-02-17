package mjaroslav.mcmods.mjutils.common.objects;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import mjaroslav.utils.UtilsJava;
import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler extends ConfigurationBase {
    private String modid;
    private String className;
    private Configuration instance;
    private Class fieldClass;
    private Logger logger;

    public ConfigurationHandler(String modid, Logger logger, String className) {
        super();
        this.modid = modid;
        this.className = className;
        this.logger = logger;
        try {
            fieldClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("Config class (" + className + ") for \"" + modid + "\" not found!");
            e.printStackTrace();
        }
    }

    public ConfigurationHandler(String modid, Logger logger, Class clazz) {
        super();
        this.modid = modid;
        this.className = clazz.getName();
        this.logger = logger;
        fieldClass = clazz;
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
    public void readFields() {
        ArrayList<Field> fields = new ArrayList<Field>();
        for (Field field : fieldClass.getFields()) {
            int mods = field.getModifiers();
            if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && !Modifier.isFinal(mods)
                    && field.getAnnotation(ConfigField.class) != null) {
                parseField(field, field.getAnnotation(ConfigField.class), getInstance());
            }
        }
    }

    // Parse (and set) field from info.
    public static void parseField(Field field, ConfigField info, Configuration instance) {
        try {
            String name = "";
            if (UtilsJava.stringIsNotEmpty(info.customName()))
                name = info.customName();
            else {
                name = field.getName();
                if (Character.isUpperCase(name.charAt(0)))
                    name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                name = name.replaceAll("[A-Z]", "_$0").toLowerCase();
            }
            boolean flag = true;
            Object value = null;
            switch (FieldType.get(field)) {
            case FLOAT:
                value = (float) instance
                        .get(info.category(), name, info.defaultFloat(), info.comment(), info.minFloat(),
                                info.maxFloat())
                        .setRequiresMcRestart(info.requiresMcRestart())
                        .setRequiresWorldRestart(info.requiresWorldRestart()).getDouble();
                break;
            case FLOATARRAY:
                value = UtilsJava.toFloatArray(instance
                        .get(info.category(), name, UtilsJava.toDoubleArray(info.defaultFloatArray()), info.comment(),
                                info.minFloat(), info.maxFloat(), info.listLengthFixed(), info.maxListLength())
                        .setRequiresMcRestart(info.requiresMcRestart())
                        .setRequiresWorldRestart(info.requiresWorldRestart()).getDoubleList());
                break;
            case STRING:
                value = instance.get(info.category(), name, info.defaultString(), info.comment(), info.validValues())
                        .setRequiresMcRestart(info.requiresMcRestart())
                        .setRequiresWorldRestart(info.requiresWorldRestart()).getString();
                break;
            case STRINGARRAY:
                value = instance.get(info.category(), name, info.defaultStringArray(), info.comment())
                        .setMaxListLength(info.maxListLength()).setIsListLengthFixed(info.listLengthFixed())
                        .setValidValues(info.validValues()).setRequiresMcRestart(info.requiresMcRestart())
                        .setRequiresWorldRestart(info.requiresWorldRestart()).getStringList();
                break;
            case INT:
                value = instance.get(info.category(), name, info.defaultInt(), info.comment())
                        .setRequiresMcRestart(info.requiresMcRestart())
                        .setRequiresWorldRestart(info.requiresWorldRestart()).getInt();
                break;
            case INTARRAY:
                value = instance.get(info.category(), name, info.defaultIntArray(), info.comment(), info.minInt(),
                        info.maxInt(), info.listLengthFixed(), info.maxListLength()).getIntList();
                break;
            case BOOLEAN:
                value = instance.get(info.category(), name, info.defaultBoolean(), info.comment())
                        .setRequiresMcRestart(info.requiresMcRestart())
                        .setRequiresWorldRestart(info.requiresWorldRestart()).getBoolean();
                break;
            case BOOLEANARRAY:
                value = instance.get(info.category(), name, info.defaultBooleanArray(), info.comment())
                        .setIsListLengthFixed(info.listLengthFixed()).setMaxListLength(info.maxListLength())
                        .setRequiresWorldRestart(info.requiresWorldRestart())
                        .setRequiresMcRestart(info.requiresMcRestart()).getBooleanList();
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

    public static enum FieldType {
        INT, INTARRAY, FLOAT, FLOATARRAY, STRING, STRINGARRAY, BOOLEAN, BOOLEANARRAY, NONE;

        public static FieldType get(Field field) {
            if (field != null) {
                Class type = field.getType();
                if (type == String.class)
                    return STRING;
                if (type == String[].class)
                    return STRINGARRAY;
                if (type == int.class)
                    return INT;
                if (type == int[].class)
                    return INTARRAY;
                if (type == float.class)
                    return FLOAT;
                if (type == float[].class)
                    return FLOATARRAY;
                if (type == boolean.class)
                    return BOOLEAN;
                if (type == boolean[].class)
                    return BOOLEANARRAY;
            }
            return NONE;
        }
    }
}
