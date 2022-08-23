package com.github.mjaroslav.mjutils.configurator;

import com.github.mjaroslav.mjutils.configurator.annotations.*;
import cpw.mods.fml.client.config.IConfigElement;
import lombok.val;
import lombok.var;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AnnotationConfigurator extends ForgeConfigurator {
    public static final String[] COLOR_VALID_VALUES = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    public static final Pattern COLOR_VALID_PATTERN = Pattern.compile("^[0-9a-f]$");

    protected final @NotNull Class<?> rootCategory;
    protected final @NotNull Map<Field, Object> defaults = new HashMap<>();

    public AnnotationConfigurator(@NotNull String fileName, @NotNull String modId,
                                  @NotNull Class<?> rootCategory) {
        super(fileName, modId);
        this.rootCategory = rootCategory;
    }

    @SuppressWarnings("rawtypes")
    public List<IConfigElement> getElementList() {
        return new ConfigElement<>(instance.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements();
    }

    @Override
    public void init(@NotNull Configuration instance) {
        if (rootCategory.isAnnotationPresent(Version.class))
            version = rootCategory.getAnnotation(Version.class).value();
        try {
            parseClass(instance, rootCategory, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigSaved() {
        try {
            parseClass(instance, rootCategory, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected @NotNull String formatName(@NotNull String name) {
        name = name.replace("Category", "").replaceAll("[A-Z]", "_$0").toLowerCase();
        if (name.startsWith("_"))
            name = name.substring(1);
        return name;
    }

    protected void parseClass(@NotNull Configuration instance, @NotNull Class<?> categoryClass,
                              @Nullable String parentCategoryName) throws Exception {
        if (categoryClass.isAnnotationPresent(Ignore.class))
            return;
        var categoryName = !StringUtils.isNotBlank(parentCategoryName) ? Configuration.CATEGORY_GENERAL
                : String.format("%s.%s", parentCategoryName, formatName(categoryClass.getSimpleName()));
        val name = categoryClass.getAnnotation(Name.class);
        if (name != null)
            categoryName = !StringUtils.isNotBlank(parentCategoryName) ? Configuration.CATEGORY_GENERAL
                    : String.format("%s.%s", parentCategoryName, name.value());
        val comment = categoryClass.getAnnotation(Comment.class);
        if (comment != null)
            instance.setCategoryComment(categoryName, comment.value());
        val langKey = categoryClass.getAnnotation(LangKey.class);
        if (langKey != null)
            instance.setCategoryLanguageKey(categoryName, langKey.value());
        else
            instance.setCategoryLanguageKey(categoryName, "config.category." + getModId() + ":" + categoryName + ".name");
        val restart = categoryClass.getAnnotation(Restart.class);
        if (restart != null) {
            if (restart.value() == Restart.Value.GAME || restart.value() == Restart.Value.BOTH)
                instance.setCategoryRequiresMcRestart(categoryName, true);
            if (restart.value() == Restart.Value.WORLD || restart.value() == Restart.Value.BOTH)
                instance.setCategoryRequiresWorldRestart(categoryName, true);
        }
        int mods;
        Property.Type type;
        for (var field : categoryClass.getDeclaredFields()) {
            mods = field.getModifiers();
            if (Modifier.isPublic(mods) && Modifier.isStatic(mods)) {
                type = parseType(field.getType());
                if (type != null)
                    parseField(instance, field, categoryName, type, field.getType().isArray());
            }
        }
        for (var clazz : categoryClass.getClasses()) {
            mods = clazz.getModifiers();
            if (Modifier.isStatic(mods) && Modifier.isPublic(mods))
                parseClass(instance, clazz, categoryName);
        }
    }

    protected void parseField(@NotNull Configuration instance, @NotNull Field field, @NotNull String categoryName,
                              @NotNull Property.Type parsedType,
                              boolean isArray) throws Exception {
        var propertyName = formatName(field.getName());
        val name = field.getAnnotation(Name.class);
        if (name != null)
            propertyName = name.value();
        var propertyComment = "Description not provided";
        val comment = field.getAnnotation(Comment.class);
        if (comment != null)
            propertyComment = comment.value();
        val category = instance.getCategory(categoryName);
        if (parsedType == Property.Type.STRING)
            if (field.isAnnotationPresent(Values.Color.class))
                parsedType = Property.Type.COLOR;
            else if (field.isAnnotationPresent(Values.Mod.class))
                parsedType = Property.Type.MOD_ID;
        //val defaults = field.getAnnotation(Default.class);
        Property property;
        switch (parsedType) {
            case INTEGER: {
                if (isArray) {
                    val defaultValue = (int[]) defaults.getOrDefault(field, field.get(null));
                    defaults.putIfAbsent(field, defaultValue);
                    //val defaultValue = defaults != null ? defaults.I() : new int[0];
                    property = instance.get(categoryName, propertyName, defaultValue, propertyComment);
                    field.set(null, property.getIntList());
                } else {
                    val defaultValue = (int) defaults.getOrDefault(field, field.get(null));
                    defaults.putIfAbsent(field, defaultValue);
                    //val defaultValue = defaults != null ? defaults.i() : 0;
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    field.set(null, property.getInt());
                }
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
            break;
            case BOOLEAN: {
                if (isArray) {
                    val defaultValue = (boolean[]) defaults.getOrDefault(field, field.get(null));
                    defaults.putIfAbsent(field, defaultValue);
                    //val defaultValue = defaults != null ? defaults.B() : new boolean[0];
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    field.set(null, property.getBooleanList());
                } else {
                    val defaultValue = (boolean) defaults.getOrDefault(field, field.get(null));
                    defaults.putIfAbsent(field, defaultValue);
                    //val defaultValue = defaults != null && defaults.b();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    field.set(null, property.getBoolean());
                }
                if (StringUtils.isNotBlank(property.comment))
                    property.comment += " [default: " + property.getDefault() + "]";
            }
            break;
            case DOUBLE: {
                if (isArray) {
                    val defaultValue = (double[]) defaults.getOrDefault(field, field.get(null));
                    defaults.putIfAbsent(field, defaultValue);
                    //val defaultValue = defaults != null ? defaults.D() : new double[0];
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    field.set(null, property.getDoubleList());
                } else {
                    val defaultValue = (double) defaults.getOrDefault(field, field.get(null));
                    defaults.putIfAbsent(field, defaultValue);
                    //val defaultValue = defaults != null ? defaults.d() : 0;
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    field.set(null, defaultValue);
                }
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
            break;
            default: {
                if (isArray) {
                    val defaultValue = (String[]) defaults.getOrDefault(field, field.get(null));
                    defaults.putIfAbsent(field, defaultValue);
                    //val defaultValue = defaults != null ? defaults.S() : new String[0];
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment, parsedType);
                    field.set(null, property.getStringList());
                } else {
                    val defaultValue = (String) defaults.getOrDefault(field, field.get(null));
                    defaults.putIfAbsent(field, defaultValue);
                    //val defaultValue = defaults != null ? defaults.s() : "";
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment, parsedType);
                    field.set(null, property.getString());
                }
                if (StringUtils.isNotBlank(property.comment))
                    property.comment += " [default: " + property.getDefault() + "]";
            }
            break;
        }
        val restart = field.getAnnotation(Restart.class);
        if (restart != null) {
            if (restart.value() == Restart.Value.GAME || restart.value() == Restart.Value.BOTH)
                property.setRequiresMcRestart(true);
            if (restart.value() == Restart.Value.WORLD || restart.value() == Restart.Value.BOTH)
                property.setRequiresWorldRestart(true);
        }
        if (isArray) {
            val size = field.getAnnotation(ArraySize.class);
            if (size != null) {
                if (size.fixed())
                    property.setIsListLengthFixed(true);
                if (size.value() > 0)
                    property.setMaxListLength(size.value());

            }
        }
        LangKey langKey = field.getDeclaredAnnotation(LangKey.class);
        if (langKey != null)
            property.setLanguageKey(langKey.value());
        else
            property.setLanguageKey("config.property." + getModId() + ":" + categoryName + "." + propertyName + ".name");
        if (property.getType() == Property.Type.COLOR) {
            property.setValidValues(COLOR_VALID_VALUES);
            property.setValidationPattern(COLOR_VALID_PATTERN);
        }
        val pattern = field.getAnnotation(com.github.mjaroslav.mjutils.configurator.annotations.Pattern.class);
        if (pattern != null)
            property.setValidationPattern(Pattern.compile(pattern.value()));
        val values = field.getAnnotation(Values.class);
        if (values != null)
            property.setValidValues(values.value());
        category.put(propertyName, property);
    }

    protected @Nullable Property.Type parseType(@NotNull Class<?> type) {
        if (type.equals(int.class) || type.equals(int[].class))
            return Property.Type.INTEGER;
        if (type.equals(boolean.class) || type.equals(boolean[].class))
            return Property.Type.BOOLEAN;
        if (type.equals(double.class) || type.equals(double[].class))
            return Property.Type.DOUBLE;
        if (type.equals(String.class) || type.equals(String[].class))
            return Property.Type.STRING;
        return null; // Is a subcategory.
    }

//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.TYPE)
//    public @interface ValidPattern {
//        String value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.TYPE)
//    public @interface ValidValues {
//        String[] value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target({ElementType.FIELD, ElementType.TYPE})
//    public @interface Name {
//        String value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target({ElementType.FIELD, ElementType.TYPE})
//    public @interface Comment {
//        String value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target({ElementType.FIELD, ElementType.TYPE})
//    public @interface LangKey {
//        String value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target({ElementType.FIELD, ElementType.TYPE})
//    public @interface RequiresMCRestart {
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target({ElementType.FIELD, ElementType.TYPE})
//    public @interface RequiresWorldRestart {
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface ModIdType {
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface ColorType {
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface DefaultString {
//        String value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface DefaultBoolean {
//        boolean value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface DefaultDouble {
//        double value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface DefaultInt {
//        int value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface DefaultStringArray {
//        String[] value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface DefaultBooleanArray {
//        boolean[] value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface DefaultDoubleArray {
//        double[] value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface DefaultIntArray {
//        int[] value();
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface IntRange {
//        int min() default Integer.MIN_VALUE;
//
//        int max() default Integer.MAX_VALUE;
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface DoubleRange {
//        double min() default Double.MIN_VALUE;
//
//        double max() default Double.MAX_VALUE;
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface FixArraySize {
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.FIELD)
//    public @interface MaxArraySize {
//        int value();
//    }
}
