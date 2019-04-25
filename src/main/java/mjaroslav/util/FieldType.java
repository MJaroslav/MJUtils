package mjaroslav.util;

import java.lang.reflect.Field;

public enum FieldType {
    INT(int.class), INT_ARRAY(int[].class), FLOAT(float.class), FLOAT_ARRAY(float[].class), STRING(String.class),
    STRING_ARRAY(String[].class), BOOLEAN(boolean.class), BOOLEAN_ARRAY(boolean[].class), UNKNOWN(Object.class);

    public final Class TYPE;

    FieldType(Class type) {
        this.TYPE = type;
    }

    public static FieldType get(Field field) {
        if(field != null) {
            Class type = field.getType();
            for(FieldType check : values())
                if(check.TYPE == type)
                    return check;
        }
        return UNKNOWN;
    }
}