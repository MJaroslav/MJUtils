package com.github.mjaroslav.mjutils.util.lang.reflect;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class UtilsReflection {
    @Nonnull
    public static String getPackageFromClass(@Nonnull Object cls) {
        String className;
        if (cls instanceof String)
            className = (String) cls;
        else if (cls instanceof Class<?>)
            className = ((Class<?>) cls).getName();
        else
            throw new IllegalArgumentException("Argument must be String class name or Class object");
        return className.contains(".") ? className.substring(0, className.lastIndexOf(".")) : "";
    }

    public static Field findFieldUpTo(@Nonnull Class<?> clazz, @Nullable Class<?> exclusiveParent, String... fieldNames) {
        Exception failed = null;
        Map<String, Field> fields = getFieldsUpTo(clazz, exclusiveParent);
        for (String fieldName : fieldNames) {
            try {
                if (fields.containsKey(fieldName)) {
                    Field f = fields.get(fieldName); // Search in parents too
                    f.setAccessible(true);
                    return f;
                }
            } catch (Exception e) {
                failed = e;
            }
        }
        throw new ReflectionHelper.UnableToFindFieldException(fieldNames, failed);
    }

    public static Map<String, Field> getFieldsUpTo(@Nonnull Class<?> startClass, @Nullable Class<?> exclusiveParent) {
        Map<String, Field> currentClassFields = new HashMap<>();
        Lists.newArrayList(startClass.getDeclaredFields()).forEach(field -> currentClassFields.put(field.getName(), field));
        Class<?> parentClass = startClass.getSuperclass();
        if (parentClass != null && (!(parentClass.equals(exclusiveParent)))) {
            Map<String, Field> parentClassFields = getFieldsUpTo(parentClass, exclusiveParent);
            currentClassFields.putAll(parentClassFields);
        }
        return currentClassFields;
    }


    @SuppressWarnings("unchecked")
    public static <T, E> T getPrivateValueUpTo(@Nonnull Class<? super E> classToAccess, E instance, @Nullable Class<?> exclusiveParent, String... fieldNames) {
        try {
            return (T) findFieldUpTo(classToAccess, exclusiveParent, fieldNames).get(instance);
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, e);
        }
    }

    public static <T, E> void setPrivateValueUpTo(Class<? super T> classToAccess, T instance, E value, @Nullable Class<?> exclusiveParent, String... fieldNames) {
        try {
            findFieldUpTo(classToAccess, exclusiveParent, fieldNames).set(instance, value);
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, e);
        }
    }
}
