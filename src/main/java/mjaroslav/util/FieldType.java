package mjaroslav.util;

import java.lang.reflect.Field;

public enum FieldType {
    INT, INTARRAY, FLOAT, FLOATARRAY, STRING, STRINGARRAY, BOOLEAN, BOOLEANARRAY, NONE;

    public static FieldType get(Field field) {
        if (field != null) {
            Class type = field.getType();
            if (type == String.class)
                return STRING;
            else if (type == String[].class)
                return STRINGARRAY;
            else if (type == int.class)
                return INT;
            else if (type == int[].class)
                return INTARRAY;
            else if (type == float.class)
                return FLOAT;
            else if (type == float[].class)
                return FLOATARRAY;
            else if (type == boolean.class)
                return BOOLEAN;
            else if (type == boolean[].class)
                return BOOLEANARRAY;
        }
        return NONE;
    }
}