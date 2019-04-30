package mjaroslav.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum FieldType {
    INT(int.class),
    INT_ARRAY(int[].class),
    DOUBLE(double.class, float.class),
    DOUBLE_ARRAY(double[].class, float[].class),
    STRING(String.class),
    STRING_ARRAY(String[].class),
    BOOLEAN(boolean.class),
    BOOLEAN_ARRAY(boolean[].class),
    UNKNOWN();

    public final Set TYPES;

    FieldType(Class... types) {
        this.TYPES = new HashSet<>(Arrays.asList(types));
    }

    public static FieldType get(Field field) {
        if (field != null) {
            Class type = field.getType();
            for (FieldType check : values())
                if (check.TYPES.contains(type))
                    return check;
        }
        return UNKNOWN;
    }
}