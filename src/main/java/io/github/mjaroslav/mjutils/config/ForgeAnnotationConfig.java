package io.github.mjaroslav.mjutils.config;

import io.github.mjaroslav.mjutils.config.annotations.*;
import io.github.mjaroslav.mjutils.config.annotations.Restart.Value;
import io.github.mjaroslav.mjutils.config.annotations.Values.Color;
import io.github.mjaroslav.mjutils.config.annotations.Values.Mod;
import lombok.val;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ForgeAnnotationConfig extends ForgeConfig {
    public static final String[] COLOR_VALID_VALUES = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    public static final java.util.regex.Pattern COLOR_VALID_PATTERN = java.util.regex.Pattern.compile("^[0-9a-f]$");

    protected final @NotNull Class<?> rootCategoryClass;
    protected final @NotNull Map<Field, Object> defaults = new HashMap<>();

    public ForgeAnnotationConfig(@NotNull Path file, @NotNull Class<?> rootCategoryClass) {
        this(null, file, null, rootCategoryClass);
    }

    public ForgeAnnotationConfig(@Nullable String modId, @NotNull Path file, @NotNull Class<?> rootCategoryClass) {
        this(modId, file, null, rootCategoryClass);
    }

    public ForgeAnnotationConfig(@NotNull Path file, @Nullable String version, @NotNull Class<?> rootCategoryClass) {
        this(null, file, version, rootCategoryClass);
    }

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

    public static @NotNull String formatName(@NotNull String name) {
        name = name.replace("Category", "").replaceAll("[A-Z]", "_$0").toLowerCase();
        if (name.startsWith("_")) name = name.substring(1);
        return name;
    }

    public static void parseClass(@NotNull ForgeAnnotationConfig config, @NotNull Class<?> categoryClass,
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

    public static void parseField(@NotNull ForgeAnnotationConfig config, @NotNull Field field,
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
            if (field.isAnnotationPresent(Color.class)) parsedType = Type.COLOR;
            else if (field.isAnnotationPresent(Mod.class)) parsedType = Type.MOD_ID;
        val defaultValue = config.defaults.getOrDefault(field, field.get(null));
        config.defaults.putIfAbsent(field, defaultValue);
        Property property;
        switch (parsedType) {
            case INTEGER -> {
                if (isArray)
                    property = config.properties.get(categoryName, propertyName, (int[]) defaultValue, propertyComment);
                else
                    property = config.properties.get(categoryName, propertyName, (int) defaultValue, propertyComment);
                field.set(null, isArray ? property.getIntList() : property.getInt());
                var hasRange = false;
                var unlimMin = true;
                var unlimMax = true;
                val range = field.getAnnotation(Range.class);
                if (range != null) {
                    if (range.max() != Double.MAX_VALUE && range.max() != Integer.MAX_VALUE) {
                        property.setMaxValue((int) range.max());
                        unlimMax = false;
                    }
                    if (range.min() != Double.MIN_VALUE && range.min() != Integer.MIN_VALUE) {
                        property.setMinValue((int) range.min());
                        unlimMin = false;
                    }
                    hasRange = !unlimMax || !unlimMin;
                }
                if (StringUtils.isNotBlank(property.comment))
                    if (hasRange) {
                        var rangeComment = new StringBuilder(" [range:");
                        if (!unlimMin)
                            rangeComment.append(" ").append(property.getMinValue()).append(" <= value");
                        if (!unlimMax)
                            rangeComment.append(" <= ").append(property.getMaxValue());
                        rangeComment.append(", default: ").append(property.getDefault()).append("]");
                        property.comment += rangeComment.toString();
                    } else
                        property.comment += " [default: " + property.getDefault() + "]";
            }
            case BOOLEAN -> {
                if (isArray)
                    property = config.properties.get(categoryName, propertyName, (boolean[]) defaultValue,
                        propertyComment);
                else
                    property = config.properties.get(categoryName, propertyName, (boolean) defaultValue,
                        propertyComment);
                field.set(null, isArray ? property.getBooleanList() : property.getBoolean());
                if (StringUtils.isNotBlank(property.comment))
                    property.comment += " [default: " + property.getDefault() + "]";
            }
            case DOUBLE -> {
                if (isArray)
                    property = config.properties.get(categoryName, propertyName, (double[]) defaultValue,
                        propertyComment);
                else
                    property = config.properties.get(categoryName, propertyName, (double) defaultValue,
                        propertyComment);
                field.set(null, isArray ? property.getDoubleList() : property.getDouble());
                var hasRange = false;
                var unlimMin = true;
                var unlimMax = true;
                val range = field.getAnnotation(Range.class);
                if (range != null) {
                    if (range.max() != Double.MAX_VALUE) {
                        property.setMaxValue(range.max());
                        unlimMax = false;
                    }
                    if (range.min() != Double.MIN_VALUE) {
                        property.setMinValue(range.min());
                        unlimMin = false;
                    }
                    hasRange = !unlimMax || !unlimMin;
                }
                if (StringUtils.isNotBlank(property.comment))
                    if (hasRange) {
                        var rangeComment = new StringBuilder(" [range:");
                        if (!unlimMin)
                            rangeComment.append(" ").append(property.getMinValue()).append(" <= value");
                        if (!unlimMax)
                            rangeComment.append(" <= ").append(property.getMaxValue());
                        rangeComment.append(", default: ").append(property.getDefault()).append("]");
                        property.comment += rangeComment.toString();
                    } else
                        property.comment += " [default: " + property.getDefault() + "]";
            }
            default -> {
                if (isArray)
                    property = config.properties.get(categoryName, propertyName, (String[]) defaultValue,
                        propertyComment);
                else
                    property = config.properties.get(categoryName, propertyName, (String) defaultValue,
                        propertyComment);
                field.set(null, isArray ? property.getStringList() : property.getString());
                if (StringUtils.isNotBlank(property.comment))
                    property.comment += " [default: " + property.getDefault() + "]";
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
        if (property.getType() == Type.COLOR) {
            property.setValidValues(COLOR_VALID_VALUES);
            property.setValidationPattern(COLOR_VALID_PATTERN);
        }
        val pattern = field.getAnnotation(Pattern.class);
        if (pattern != null) property.setValidationPattern(java.util.regex.Pattern.compile(pattern.value()));
        val values = field.getAnnotation(Values.class);
        if (values != null) property.setValidValues(values.value());
        category.put(propertyName, property);
    }

    public static @Nullable Property.Type parseType(@NotNull Class<?> type) {
        if (type.equals(int.class) || type.equals(int[].class)) return Property.Type.INTEGER;
        if (type.equals(boolean.class) || type.equals(boolean[].class)) return Property.Type.BOOLEAN;
        if (type.equals(double.class) || type.equals(double[].class)) return Property.Type.DOUBLE;
        if (type.equals(String.class) || type.equals(String[].class)) return Property.Type.STRING;
        return null; // Is a subcategory.
    }
}
