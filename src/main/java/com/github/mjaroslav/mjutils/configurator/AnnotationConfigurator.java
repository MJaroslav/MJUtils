package com.github.mjaroslav.mjutils.configurator;

import cpw.mods.fml.client.config.IConfigElement;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.regex.Pattern;

public class AnnotationConfigurator extends ForgeConfigurator {
    public static final String[] COLOR_VALID_VALUES = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    public static final Pattern COLOR_VALID_PATTERN = Pattern.compile("^[0-9a-f]$");
    @Nonnull
    protected final Class<?> rootCategory;

    private boolean versionParsed;

    public AnnotationConfigurator(@Nonnull String fileName, @Nonnull String modId, @Nonnull Class<?> rootCategory) {
        super(fileName, modId);
        this.rootCategory = rootCategory;
    }

    @Nullable
    @Override
    public String getVersion() {
        if (!versionParsed) {
            try {
                int mods;
                for (Field field : rootCategory.getFields()) {
                    mods = field.getModifiers();
                    if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && Modifier.isFinal(mods)
                            && field.getName().equals("configVersion") && field.getType().equals(String.class)) {
                        version = (String) field.get(null);
                        break;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            versionParsed = true;
        }
        return super.getVersion();
    }

    @Override
    public void sync() {
        try {
            parseClass(getInstance(), rootCategory, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    public List<IConfigElement> categoryToElementList(String category) {
        return new ConfigElement<>(instance.getCategory(category)).getChildElements();
    }

    public static String formatName(String name) {
        if (Character.isUpperCase(name.charAt(0)))
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
        return name.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    public static void parseClass(@Nonnull Configuration instance, @Nonnull Class<?> categoryClass, @Nullable String parentCategoryName) throws Exception {
        String categoryName = !StringUtils.isNotBlank(parentCategoryName) ? "root"
                : String.format("%s.%s", parentCategoryName, formatName(categoryClass.getSimpleName()));
        Name name = categoryClass.getDeclaredAnnotation(Name.class);
        if (name != null)
            categoryName = !StringUtils.isNotBlank(parentCategoryName) ? "root"
                    : String.format("%s.%s", parentCategoryName, name.value());
        Comment comment = categoryClass.getDeclaredAnnotation(Comment.class);
        if (comment != null)
            instance.setCategoryComment(categoryName, comment.value());
        LangKey langKey = categoryClass.getDeclaredAnnotation(LangKey.class);
        if (langKey != null)
            instance.setCategoryLanguageKey(categoryName, langKey.value());
        if (categoryClass.isAnnotationPresent(RequiresMCRestart.class))
            instance.setCategoryRequiresMcRestart(categoryName, true);
        if (categoryClass.isAnnotationPresent(RequiresWorldRestart.class))
            instance.setCategoryRequiresWorldRestart(categoryName, true);
        int mods;
        Property.Type type;
        for (Field field : categoryClass.getDeclaredFields()) {
            mods = field.getModifiers();
            if (Modifier.isPublic(mods) && Modifier.isStatic(mods)) {
                type = parseType(field.getType());
                if (type != null)
                    parseField(instance, field, categoryName, type, field.getType().isArray());
            }
        }
        for (Class<?> clazz : categoryClass.getDeclaredClasses()) {
            mods = clazz.getModifiers();
            if (Modifier.isStatic(mods) && Modifier.isPublic(mods))
                parseClass(instance, clazz, categoryName);
        }
    }

    public static void parseField(Configuration instance, Field field, String categoryName, Property.Type parsedType,
                                  boolean isArray) throws Exception {
        String propertyName = formatName(field.getName());
        Name name = field.getDeclaredAnnotation(Name.class);
        if (name != null)
            propertyName = name.value();
        String propertyComment = "";
        Comment comment = field.getDeclaredAnnotation(Comment.class);
        if (comment != null)
            propertyComment = comment.value();
        ConfigCategory category = instance.getCategory(categoryName);
        if (parsedType == Property.Type.STRING) {
            ColorType colorType = field.getDeclaredAnnotation(ColorType.class);
            if (colorType != null)
                parsedType = Property.Type.COLOR;
            ModIdType modIdType = field.getDeclaredAnnotation(ModIdType.class);
            if (modIdType != null)
                parsedType = Property.Type.MOD_ID;
        }
        Property property;
        switch (parsedType) {
            case INTEGER: {
                if (!(field.isAnnotationPresent(DefaultInt.class) || field.isAnnotationPresent(DefaultIntArray.class)))
                    return;
                if (isArray) {
                    int[] defaultValue = field.getDeclaredAnnotation(DefaultIntArray.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue, propertyComment);
                    field.set(null, property.getIntList());
                } else {
                    int defaultValue = field.getDeclaredAnnotation(DefaultInt.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    field.set(null, property.getInt());
                }
                boolean hasRange = false;
                IntRange intRange = field.getDeclaredAnnotation(IntRange.class);
                if (intRange != null) {
                    property.setMaxValue(intRange.max());
                    property.setMinValue(intRange.min());
                    hasRange = true;
                }
                if (StringUtils.isNotBlank(property.comment))
                    if (hasRange)
                        property.comment += " [range: " + property.getMinValue() + " ~ " + property.getMaxValue()
                                + ", default: " + property.getDefault() + "]";
                    else
                        property.comment += " [default: " + property.getDefault() + "]";
            }
            break;
            case BOOLEAN: {
                if (!(field.isAnnotationPresent(DefaultBoolean.class) || field.isAnnotationPresent(DefaultBooleanArray.class)))
                    return;
                if (isArray) {
                    boolean[] defaultValue = field.getDeclaredAnnotation(DefaultBooleanArray.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    field.set(null, property.getBooleanList());
                } else {
                    boolean defaultValue = field.getDeclaredAnnotation(DefaultBoolean.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    field.set(null, property.getBoolean());
                }
                if (StringUtils.isNotBlank(property.comment))
                    property.comment += " [default: " + property.getDefault() + "]";
            }
            break;
            case DOUBLE: {
                if (!(field.isAnnotationPresent(DefaultDouble.class) || field.isAnnotationPresent(DefaultDoubleArray.class)))
                    return;
                if (isArray) {
                    double[] defaultValue = field.getDeclaredAnnotation(DefaultDoubleArray.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    field.set(null, property.getDoubleList());
                } else {
                    double defaultValue = field.getDeclaredAnnotation(DefaultDouble.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    field.set(null, defaultValue);
                }
                boolean hasRange = false;
                DoubleRange doubleRange = field.getDeclaredAnnotation(DoubleRange.class);
                if (doubleRange != null) {
                    property.setMaxValue(doubleRange.max());
                    property.setMinValue(doubleRange.min());
                    hasRange = true;
                }
                if (StringUtils.isNotBlank(property.comment))
                    if (hasRange)
                        property.comment += " [range: " + property.getMinValue() + " ~ " + property.getMaxValue()
                                + ", default: " + property.getDefault() + "]";
                    else
                        property.comment += " [default: " + property.getDefault() + "]";
            }
            break;
            default: {
                if (!(field.isAnnotationPresent(DefaultString.class) || field.isAnnotationPresent(DefaultStringArray.class)))
                    return;
                if (isArray) {
                    String[] defaultValue = field.getDeclaredAnnotation(DefaultStringArray.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment, parsedType);
                    field.set(null, property.getStringList());
                } else {
                    String defaultValue = field.getDeclaredAnnotation(DefaultString.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment, parsedType);
                    field.set(null, property.getString());
                }
                if (StringUtils.isNotBlank(property.comment))
                    property.comment += " [default: " + property.getDefault() + "]";
            }
            break;
        }
        RequiresWorldRestart requiresWorldRestart = field.getDeclaredAnnotation(RequiresWorldRestart.class);
        if (requiresWorldRestart != null)
            property.setRequiresWorldRestart(true);
        RequiresMCRestart requiresMCRestart = field.getDeclaredAnnotation(RequiresMCRestart.class);
        if (requiresMCRestart != null)
            property.setRequiresMcRestart(true);
        if (isArray) {
            if (field.isAnnotationPresent(FixArraySize.class))
                property.setIsListLengthFixed(true);
            MaxArraySize maxArraySize = field.getDeclaredAnnotation(MaxArraySize.class);
            if (maxArraySize != null)
                property.setMaxListLength(maxArraySize.value());
        }
        LangKey langKey = field.getDeclaredAnnotation(LangKey.class);
        if (langKey != null)
            property.setLanguageKey(langKey.value());
        if (property.getType() == Property.Type.COLOR) {
            property.setValidValues(COLOR_VALID_VALUES);
            property.setValidationPattern(COLOR_VALID_PATTERN);
        }
        ValidPattern pattern = field.getDeclaredAnnotation(ValidPattern.class);
        if (pattern != null)
            property.setValidationPattern(Pattern.compile(pattern.value()));
        ValidValues validValues = field.getDeclaredAnnotation(ValidValues.class);
        if (validValues != null)
            property.setValidValues(validValues.value());
        category.put(propertyName, property);
    }

    private static Property.Type parseType(Class<?> type) {
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

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ValidPattern {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ValidValues {
        String[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    public @interface Name {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    public @interface Comment {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    public @interface LangKey {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    public @interface RequiresMCRestart {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    public @interface RequiresWorldRestart {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ModIdType {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ColorType {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DefaultString {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DefaultBoolean {
        boolean value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DefaultDouble {
        double value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DefaultInt {
        int value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DefaultStringArray {
        String[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DefaultBooleanArray {
        boolean[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DefaultDoubleArray {
        double[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DefaultIntArray {
        int[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface IntRange {
        int min() default Integer.MIN_VALUE;

        int max() default Integer.MAX_VALUE;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DoubleRange {
        double min() default Double.MIN_VALUE;

        double max() default Double.MAX_VALUE;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface FixArraySize {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface MaxArraySize {
        int value();
    }
}
