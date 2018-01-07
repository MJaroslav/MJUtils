package mjaroslav.mcmods.mjutils.common.objects;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import mjaroslav.utils.JavaUtils;
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

    public static void parseField(Field field, ConfigField info, Configuration instance) {
        try {
            String name = JavaUtils.stringIsEmpty(info.customName()) ? field.getName() : info.customName();
            switch (FieldType.get(field)) {
            case FLOAT:
                field.setFloat(null, instance.getFloat(name, info.category(), info.defaultFloat(), info.minFloat(),
                        info.maxFloat(), info.comment()));
                break;
            case FLOATARRAY:
                field.set(null, JavaUtils.toFloatArray(instance
                        .get(info.category(), name, JavaUtils.toDoubleArray(info.defaultFloatArray()), info.comment())
                        .getDoubleList()));
                break;
            case STRING:
                field.set(null, instance.getString(name, info.category(), info.defaultString(), info.comment()));
                break;
            case STRINGARRAY:
                field.set(null,
                        instance.getStringList(name, info.category(), info.defaultStringArray(), info.comment()));
                break;
            case INT:
                field.setInt(null, instance.getInt(name, info.category(), info.defaultInt(), info.minInt(),
                        info.maxInt(), info.comment()));
                break;
            case INTARRAY:
                field.set(null,
                        instance.get(info.category(), name, info.defaultIntArray(), info.comment()).getIntList());
                break;
            default:
                break;
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static enum FieldType {
        INT, INTARRAY, FLOAT, FLOATARRAY, STRING, STRINGARRAY, NONE;

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
            }
            return NONE;
        }

        public static boolean isConfigType(Field field) {
            if (field != null) {
                Class type = field.getType();
                return type == String.class || type == String[].class || type == int.class || type == int[].class
                        || type == float.class || type == float[].class;
            }
            return false;
        }
    }
}
