package io.github.mjaroslav.mjutils.util;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToFindFieldException;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraftforge.classloading.FMLForgePlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Extension for {@link ReflectionHelper} with extra methods such as enum values getter or deep get/set field.
 *
 * @see ReflectionHelper
 */
@UtilityClass
public class UtilsReflection {
    private final Map<Class<?>, Enum<?>[]> ENUM_VALUES_CACHE = new HashMap<>();

    /**
     * Returns values from enum class.
     *
     * @param enumClass enum class, just must contain enum-standardized field with values.
     * @return array with Enum values. All successful gets cached.
     * @throws UnableToAccessFieldException on any error while values getting.
     */
    public @NotNull Enum<?>[] getEnumValues(@NotNull Class<?> enumClass) throws UnableToAccessFieldException {
        try {
            if (ENUM_VALUES_CACHE.containsKey(enumClass)) return ENUM_VALUES_CACHE.get(enumClass);
            Field valuesField = null;
            for (var field : enumClass.getDeclaredFields())
                if (field.getName().equals("$VALUES") || field.getName().equals("ENUM$VALUES")) {
                    valuesField = field;
                    break;
                }
            val flags = (FMLForgePlugin.RUNTIME_DEOBF ? Modifier.PUBLIC : Modifier.PRIVATE) | Modifier.STATIC
                | Modifier.FINAL | 0x1000;
            if (valuesField == null) {
                val valueType = String.format("[L%s;", enumClass.getName().replace('.', '/'));
                for (var field : enumClass.getDeclaredFields())
                    if ((field.getModifiers() & flags) == flags && field.getType().getName().replace('.', '/')
                        .equals(valueType)) {
                        valuesField = field;
                        break;
                    }
            }
            if (valuesField == null)
                throw new NoSuchFieldException("Can not find enum values fiend from " + enumClass.getName());
            valuesField.setAccessible(true);
            val result = (Enum<?>[]) valuesField.get(enumClass);
            ENUM_VALUES_CACHE.put(enumClass, result);
            return result;
        } catch (Exception e) {
            throw new UnableToAccessFieldException(new String[]{"$VALUES", "ENUM$VALUES"}, e);
        }
    }

    /**
     * Get enum from enumClass by string name or default if not found.
     *
     * @param name         case sensitive enum name.
     * @param enumClass    enum class, just must contain enum-standardized field with values.
     * @param defaultValue nullable default same-type enum value.
     * @return Enum value from enumClass.
     * @throws UnableToAccessFieldException on error while values getting.
     */
    public @Nullable Enum<?> getEnumFromName(@NotNull String name, @NotNull Class<?> enumClass,
                                             @Nullable Enum<?> defaultValue) throws UnableToAccessFieldException {
        return Arrays.stream(getEnumValues(enumClass)).filter(v -> v.name().equals(name)).findFirst()
            .orElse(defaultValue);
    }

    /**
     * Get enum from enumClass by string name.
     *
     * @param name      case sensitive enum name.
     * @param enumClass enum class, just must contain enum-standardized field with values.
     * @return Enum value from enumClass.
     * @throws NoSuchElementException       if enum with specified name not found.
     * @throws UnableToAccessFieldException on error while values getting.
     */
    public @NotNull Enum<?> getEnumFromName(@NotNull String name, @NotNull Class<?> enumClass)
        throws NoSuchElementException, UnableToAccessFieldException {
        return Arrays.stream(getEnumValues(enumClass)).filter(v -> v.name().equals(name)).findFirst()
            .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Returns simple class name from parameter. Can take String class name, class as is or an object from whose
     * class the simple name will be received. String class name must be with dots as package separator.
     *
     * @param source source for class name getting.
     * @return simple class name.
     */
    public @NotNull String getSimpleClassName(@NotNull Object source) {
        val className = source instanceof String string ? string :
            source instanceof Class<?> clazz ? clazz.getName() : source.getClass().getName();
        return className.contains(".") ? className.substring(className.lastIndexOf(".") + 1) : "";
    }

    /**
     * Returns package string from parameter. Can take String class name, class as is or an object from whose
     * class the package will be received. String class name must be with dots as package separator.
     *
     * @param source source for package getting.
     * @return formatted package string.
     */
    public @NotNull String getPackageFromClass(@NotNull Object source) {
        val className = source instanceof String string ? string :
            source instanceof Class<?> clazz ? clazz.getName() : source.getClass().getName();
        return className.contains(".") ? className.substring(0, className.lastIndexOf(".")) : "";
    }

    /**
     * Just {@link ReflectionHelper#findField(Class, String...)} with deep search in super classes.
     *
     * @param target     target class.
     * @param deepLimit  deepest class (not including) to which to search among super classes.
     * @param fieldNames field names for search.
     * @return first found field.
     * @throws UnableToFindFieldException when any error or if field not found.
     */
    public @NotNull Field findDeepField(@NotNull Class<?> target, @Nullable Class<?> deepLimit,
                                        @NotNull String @NotNull ... fieldNames) throws UnableToFindFieldException {
        Exception failed = null;
        val fields = getDeepDeclaredFields(target, deepLimit);
        for (var fieldName : fieldNames)
            try {
                if (fields.containsKey(fieldName)) {
                    val f = fields.get(fieldName); // Search in parents too
                    f.setAccessible(true);
                    return f;
                }
            } catch (Exception e) {
                failed = e;
            }
        throw new UnableToFindFieldException(fieldNames, failed);
    }

    /**
     * Get all declared fields from target class including private from super class.
     *
     * @param target    target class.
     * @param deepLimit deepest class (not including) to which to search among super classes.
     * @return all declared fields (including private from super classes) mapped to their names as keys.
     */
    public @NotNull Map<String, Field> getDeepDeclaredFields(@NotNull Class<?> target, @Nullable Class<?> deepLimit) {
        val result = new HashMap<String, Field>();
        var currentClass = target;
        do {
            for (var field : currentClass.getDeclaredFields()) result.put(field.getName(), field);
            currentClass = target.getSuperclass();
        } while (currentClass != null && !(currentClass.equals(deepLimit)));
        return result;
    }

    /**
     * Get field value from target class including private from super class.
     *
     * @param target     target class.
     * @param instance   object of target class for non-static fields.
     * @param deepLimit  deepest class (not including) to which to search among super classes.
     * @param fieldNames field names for search.
     * @param <T>        field type.
     * @param <E>        field owner class type.
     * @return value of first found field.
     * @throws UnableToAccessFieldException on any error while value getting.
     */
    @SuppressWarnings("unchecked")
    public @Nullable <T, E> T getDeepPrivateValue(@NotNull Class<? super E> target, @Nullable E instance,
                                                  @Nullable Class<?> deepLimit,
                                                  @NotNull String @NotNull ... fieldNames)
        throws UnableToAccessFieldException {
        try {
            return (T) findDeepField(target, deepLimit, fieldNames).get(instance);
        } catch (Exception e) {
            throw new UnableToAccessFieldException(fieldNames, e);
        }
    }

    /**
     * Set value to field from target class including private from super class.
     *
     * @param target     target class.
     * @param instance   object of target class for non-static fields.
     * @param value      new value for field.
     * @param deepLimit  deepest class (not including) to which to search among super classes.
     * @param fieldNames field names for search.
     * @param <E>        field owner class type.
     * @param <T>        field type.
     * @throws UnableToAccessFieldException on any error while value getting.
     */
    public <T, E> void setDeepPrivateValue(@NotNull Class<? super T> target, @Nullable T instance,
                                           @Nullable E value, @Nullable Class<?> deepLimit,
                                           @NotNull String @NotNull ... fieldNames)
        throws UnableToAccessFieldException {
        try {
            findDeepField(target, deepLimit, fieldNames).set(instance, value);
        } catch (Exception e) {
            throw new UnableToAccessFieldException(fieldNames, e);
        }
    }

    /**
     * Get generic type from class by index.
     *
     * @param target target class.
     * @param index  index of generic parameter.
     * @return Class of generic type with specified index.
     * @throws UnableToFindGenericTypeException on any error while getting generic type.
     */
    public @NotNull Class<?> getGenericType(@NotNull Class<?> target, int index)
        throws UnableToFindGenericTypeException {
        try {
            return (Class<?>) ((ParameterizedType) target.getGenericSuperclass()).getActualTypeArguments()[index];
        } catch (Exception e) {
            throw new UnableToFindGenericTypeException(e);
        }
    }

    public class UnableToFindGenericTypeException extends RuntimeException {
        public UnableToFindGenericTypeException(@Nullable Exception cause) {
            super(cause);
        }
    }
}
