package io.github.mjaroslav.mjutils.config;

import io.github.mjaroslav.mjutils.config.annotations.*;
import io.github.mjaroslav.mjutils.config.annotations.Restart.Value;
import io.github.mjaroslav.mjutils.config.annotations.Values.Color;
import io.github.mjaroslav.mjutils.config.annotations.Values.Mod;
import io.github.mjaroslav.mjutils.util.UtilsReflection;
import lombok.val;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Extended implementation of {@link ForgeConfig} that can automatically parse config properties from Class.
 * You should create class with public static fields (properties) and inner classes (subcategories). Supported field types:
 * int, double, boolean, String and arrays of those types. You must initialize values of fields, its will be default
 * values of properties. You can mark field or class with {@link Ignore} annotation for disabling parsing of them.
 * All names will be converted with this pattern: <code>CategoryClassName -> class_name</code>,
 * <code>ClassName -> class_name</code>, <code>fieldName -> field_name</code>. You can set custom name by {@link Name}
 * annotation. Commentary can be added by using {@link Comment}. For double and int types you can set min and max
 * values by {@link Range}. Also, for int you can use {@link HEX} for use hexadecimal representation of a number in file.
 * You can add language key for translation by {@link LangKey}. String values can be
 * specified by one of {@link Pattern}, {@link Values}, {@link Values.Color} or {@link Values.Mod} annotations.
 * If changing of property (and category) applied only after world or game restarting, use {@link Restart}.
 * Finally, you can configure and fix array size with {@link ArraySize} annotation.
 *
 * @see ForgeConfig
 */
public class ForgeAnnotationConfig extends ForgeConfig {
    /**
     * Just copy of {@link net.minecraft.util.EnumChatFormatting EnumChatFormatting} with only colors for usage as valid pattern.
     */
    public static final String[] COLOR_VALID_VALUES = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    /**
     * Regexp pattern of {@link ForgeAnnotationConfig#COLOR_VALID_VALUES}.
     */
    public static final java.util.regex.Pattern COLOR_VALID_PATTERN = java.util.regex.Pattern.compile("^[0-9a-f]$");
    /**
     * Regexp pattern for {@link HEX} integers: # and 6 HEX digits.
     */
    public static final java.util.regex.Pattern HEX_COLOR_PATTERN = java.util.regex.Pattern.compile("^#[0-9A-Fa-f]{6}$");
    /**
     * Regexp pattern for {@link HEX} integers with alpha: # and 8 HEX digits.
     */
    public static final java.util.regex.Pattern HEX_COLOR_ALPHA_PATTERN = java.util.regex.Pattern.compile("^#[0-9A-Fa-f]{8}$");

    protected final @NotNull Class<?> rootCategoryClass;
    protected final @NotNull Map<Field, Object> defaults = new HashMap<>();

    /**
     * @see ForgeAnnotationConfig#ForgeAnnotationConfig(String, Path, String, Class) Full constructor.
     */
    public ForgeAnnotationConfig(@NotNull Path file, @NotNull Class<?> rootCategoryClass) {
        this(null, file, null, rootCategoryClass);
    }

    /**
     * @see ForgeAnnotationConfig#ForgeAnnotationConfig(String, Path, String, Class) Full constructor.
     */
    public ForgeAnnotationConfig(@Nullable String modId, @NotNull Path file, @NotNull Class<?> rootCategoryClass) {
        this(modId, file, null, rootCategoryClass);
    }

    /**
     * @see ForgeAnnotationConfig#ForgeAnnotationConfig(String, Path, String, Class) Full constructor.
     */
    public ForgeAnnotationConfig(@NotNull Path file, @Nullable String version, @NotNull Class<?> rootCategoryClass) {
        this(null, file, version, rootCategoryClass);
    }

    /**
     * @param rootCategoryClass class of root category, all inner classes will be become subcategories,
     *                          all fields will be become config properties.
     * @see Config#Config(String, Path, String) Super constructor for another parameters.
     */
    public ForgeAnnotationConfig(@Nullable String modId, @NotNull Path file, @Nullable String version,
                                 @NotNull Class<?> rootCategoryClass) {
        super(modId, file, version);
        this.rootCategoryClass = rootCategoryClass;
        registerSyncCallback(() -> {
            try {
                parseClass(this, rootCategoryClass, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected static @NotNull String formatName(@NotNull String name) {
        name = name.replace("Category", "").replaceAll("[A-Z]", "_$0").toLowerCase();
        if (name.startsWith("_")) name = name.substring(1);
        return name;
    }

    protected static void parseClass(@NotNull ForgeAnnotationConfig config, @NotNull Class<?> categoryClass,
                                     @Nullable String parentCategoryName) throws Exception {
        if (categoryClass.isAnnotationPresent(Ignore.class)) return;
        var categoryName = !StringUtils.isNotBlank(parentCategoryName) ? Configuration.CATEGORY_GENERAL
            : String.format("%s.%s", parentCategoryName, formatName(categoryClass.getSimpleName()));
        val name = categoryClass.getAnnotation(Name.class);
        if (name != null) categoryName = !StringUtils.isNotBlank(parentCategoryName) ? Configuration.CATEGORY_GENERAL
            : String.format("%s.%s", parentCategoryName, name.value());
        val comment = categoryClass.getAnnotation(Comment.class);
        if (comment != null) config.properties.setCategoryComment(categoryName, comment.value());
        val langKey = categoryClass.getAnnotation(LangKey.class);
        if (langKey != null) config.properties.setCategoryLanguageKey(categoryName, langKey.value());
        else
            config.properties.setCategoryLanguageKey(categoryName, "config.category." + config.modId + ":"
                + categoryName + ".name");
        val restart = categoryClass.getAnnotation(Restart.class);
        if (restart != null) {
            if (restart.value() == Value.GAME || restart.value() == Value.BOTH)
                config.properties.setCategoryRequiresMcRestart(categoryName, true);
            if (restart.value() == Value.WORLD || restart.value() == Value.BOTH)
                config.properties.setCategoryRequiresWorldRestart(categoryName, true);
        }
        int mods;
        Type type;
        for (var field : categoryClass.getDeclaredFields()) {
            mods = field.getModifiers();
            if (Modifier.isPublic(mods) && Modifier.isStatic(mods)) {
                type = parseType(field.getType());
                if (type != null) parseField(config, field, categoryName, type, field.getType().isArray());
            }
        }
        for (var clazz : categoryClass.getClasses()) {
            mods = clazz.getModifiers();
            if (Modifier.isStatic(mods) && Modifier.isPublic(mods)) parseClass(config, clazz, categoryName);
        }
    }

    protected static void parseField(@NotNull ForgeAnnotationConfig config, @NotNull Field field,
                                     @NotNull String categoryName, @NotNull Property.Type parsedType,
                                     boolean isArray) throws Exception {
        var propertyName = formatName(field.getName());
        val name = field.getAnnotation(Name.class);
        if (name != null) propertyName = name.value();
        var propertyComment = "Description not provided";
        val comment = field.getAnnotation(Comment.class);
        if (comment != null) propertyComment = comment.value();
        val category = config.properties.getCategory(categoryName);
        if (parsedType == Type.STRING)
            if (field.isAnnotationPresent(Color.class) || field.getType().equals(EnumChatFormatting.class))
                parsedType = Type.COLOR;
            else if (field.isAnnotationPresent(Mod.class)) parsedType = Type.MOD_ID;
        val defaultValue = config.defaults.getOrDefault(field, field.get(null));
        config.defaults.putIfAbsent(field, defaultValue);
        Property property;
        switch (parsedType) {
            case INTEGER -> {
                val hex = field.getAnnotation(HEX.class);
                if (hex != null) { // Special case for HEX colors
                    val format = "#%0" + (hex.alpha() ? 8 : 6) + "X";
                    if (isArray) {
                        val strings = Arrays.stream(((int[]) defaultValue)).mapToObj(i -> String.format(format, i))
                            .toArray(String[]::new);
                        property = config.properties.get(categoryName, propertyName, strings, propertyComment);
                    } else
                        property = config.properties.get(categoryName, propertyName, String.format(format, defaultValue),
                            propertyComment);
                    property.setValidationPattern(hex.alpha() ? HEX_COLOR_ALPHA_PATTERN : HEX_COLOR_PATTERN);
                    field.set(null, isArray ? Arrays.stream(property.getStringList()).mapToInt(s ->
                        Integer.parseInt(s.substring(1), 16)).toArray() : Integer.parseInt(property.getString().substring(1), 16));
                } else { // Others cases of int
                    if (isArray)
                        property = config.properties.get(categoryName, propertyName, (int[]) defaultValue, propertyComment);
                    else
                        property = config.properties.get(categoryName, propertyName, (int) defaultValue, propertyComment);
                    field.set(null, isArray ? property.getIntList() : property.getInt());
                    val range = field.getAnnotation(Range.class);
                    if (range != null) {
                        if (range.max() != Double.MAX_VALUE && range.max() != Integer.MAX_VALUE)
                            property.setMaxValue((int) range.max());
                        if (range.min() != Double.MIN_VALUE && range.min() != Integer.MIN_VALUE)
                            property.setMinValue((int) range.min());
                    }
                }
            }
            case BOOLEAN -> {
                if (isArray)
                    property = config.properties.get(categoryName, propertyName, (boolean[]) defaultValue,
                        propertyComment);
                else
                    property = config.properties.get(categoryName, propertyName, (boolean) defaultValue,
                        propertyComment);
                field.set(null, isArray ? property.getBooleanList() : property.getBoolean());
            }
            case DOUBLE -> {
                if (isArray)
                    property = config.properties.get(categoryName, propertyName, (double[]) defaultValue,
                        propertyComment);
                else
                    property = config.properties.get(categoryName, propertyName, (double) defaultValue,
                        propertyComment);
                field.set(null, isArray ? property.getDoubleList() : property.getDouble());
                val range = field.getAnnotation(Range.class);
                if (range != null) {
                    if (range.max() != Double.MAX_VALUE) property.setMaxValue(range.max());
                    if (range.min() != Double.MIN_VALUE) property.setMinValue(range.min());
                }
            }
            default -> {
                val type = field.getType();
                if (Enum.class.isAssignableFrom(type) || Enum[].class.isAssignableFrom(type)) {
                    if (isArray)
                        property = config.properties.get(categoryName, propertyName, Arrays
                                .stream(((Enum<?>[]) defaultValue)).map(Enum::name).toArray(String[]::new),
                            propertyComment, parsedType);
                    else
                        property = config.properties.get(categoryName, propertyName, defaultValue.toString(),
                            propertyComment, parsedType);
                    if (isArray)
                        field.set(null, Arrays.stream(property.getStringList()).map(l -> UtilsReflection
                            .getEnumFromName(l, field.getType())).toArray(Enum<?>[]::new));
                    else
                        field.set(null, UtilsReflection.getEnumFromName(property.getString(), field.getType()));
                    property.setValidValues(Arrays.stream(UtilsReflection.getEnumValues(type)).map(Enum::name)
                        .toArray(String[]::new));
                } else {
                    property = isArray ? config.properties.get(categoryName, propertyName, (String[]) defaultValue,
                        propertyComment, parsedType) : config.properties.get(categoryName, propertyName,
                        (String) defaultValue, propertyComment, parsedType);
                    field.set(null, isArray ? property.getStringList() : property.getString());
                }
            }
        }
        val restart = field.getAnnotation(Restart.class);
        if (restart != null) {
            if (restart.value() == Value.GAME || restart.value() == Value.BOTH) property.setRequiresMcRestart(true);
            if (restart.value() == Value.WORLD || restart.value() == Value.BOTH) property.setRequiresWorldRestart(true);
        }
        if (isArray) {
            val size = field.getAnnotation(ArraySize.class);
            if (size != null) {
                if (size.fixed()) property.setIsListLengthFixed(true);
                if (size.value() > 0) property.setMaxListLength(size.value());
            }
        }
        val langKey = field.getDeclaredAnnotation(LangKey.class);
        if (langKey != null) property.setLanguageKey(langKey.value());
        else property.setLanguageKey("config.property." + config.getModId() + ":" + categoryName + "."
            + propertyName + ".name");
        if (property.getType() == Type.COLOR) property.setValidValues(COLOR_VALID_VALUES);
        val pattern = field.getAnnotation(Pattern.class);
        if (pattern != null) property.setValidationPattern(java.util.regex.Pattern.compile(pattern.value()));
        val values = field.getAnnotation(Values.class);
        if (values != null) property.setValidValues(values.value());
        category.put(propertyName, property);
    }

    protected static @Nullable Property.Type parseType(@NotNull Class<?> type) {
        if (type.equals(int.class) || type.equals(int[].class)) return Type.INTEGER;
        if (type.equals(boolean.class) || type.equals(boolean[].class)) return Type.BOOLEAN;
        if (type.equals(double.class) || type.equals(double[].class)) return Type.DOUBLE;
        if (type.equals(String.class) || type.equals(String[].class) || Enum.class.isAssignableFrom(type)
            || Enum[].class.isAssignableFrom(type)) return Type.STRING;
        return null; // Is a subcategory.
    }
}
