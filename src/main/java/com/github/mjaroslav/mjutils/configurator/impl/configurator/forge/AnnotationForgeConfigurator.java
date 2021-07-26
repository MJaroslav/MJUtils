package com.github.mjaroslav.mjutils.configurator.impl.configurator.forge;

import com.github.mjaroslav.mjutils.configurator.ConfiguratorEvents;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorEvents.ConfiguratorInstanceChangedEvent;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;
import net.minecraftforge.common.config.ConfigCategory;
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
import java.util.HashMap;
import java.util.Map;

public class AnnotationForgeConfigurator<T> extends ForgeConfiguratorBase<T> {
    public static final String VERSION_FIELD_NAME = "VERSION";

    @Nonnull
    protected Class<T> typeClass;
    protected SyncCallback<T> syncFunc;
    protected T genericInstance;
    protected String parsedVersion;
    protected boolean staticMode;

    protected final Map<Field, Object> DEFAULT_VALUES = new HashMap<>(); // Cringe code.

    public AnnotationForgeConfigurator(@Nonnull ConfiguratorsLoader loader, @Nonnull String fileName, @Nonnull Class<T> typeClass, boolean staticMode) {
        super(loader, fileName);
        this.typeClass = typeClass;
        this.staticMode = staticMode;
    }

    @Nullable
    @Override
    public T getInstance() {
        return genericInstance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setInstance(@Nonnull T value) {
        if (isUseEvents()) {
            ConfiguratorInstanceChangedEvent event = ConfiguratorEvents.configuratorInstanceChangedEventPost(genericInstance, value, staticMode);
            if (!staticMode && !event.isCanceled())
                genericInstance = (T) event.newInstance;
        } else
            genericInstance = value;
        hasChanges = true;
        loadProperties(configurationInstance);
    }

    @Nonnull
    @Override
    public State restoreDefault() {
        if (isReadOnly())
            return State.READONLY;
        try {
            T generatedDefault = staticMode ? genericInstance : typeClass.newInstance();
            for (Map.Entry<Field, Object> entry : DEFAULT_VALUES.entrySet())
                entry.getKey().set(staticMode ? null : generatedDefault, entry.getValue());
            setInstance(generatedDefault);
            return State.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return State.ERROR;
        }
    }

    @Nonnull
    @Override
    public String getActualVersion() {
        if (parsedVersion == null) {
            // Parse actual version
            try {
                int mods;
                for (Field field : typeClass.getFields()) {
                    mods = field.getModifiers();
                    if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && Modifier.isFinal(mods)
                            && field.getName().equals(VERSION_FIELD_NAME) && field.getType().equals(String.class)) {
                        parsedVersion = (String) field.get(null);
                        break;
                    }
                }
                if (parsedVersion == null)
                    parsedVersion = UNKNOWN_VERSION;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return parsedVersion;
    }

    @SuppressWarnings("unchecked")
    public T getGenericInstance() {
        if (genericInstance == null) {
            try {
                genericInstance = (T) getClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error on loading configuration, instance can't be created");
            }
        }
        return genericInstance;
    }

    public void markDirty() {
        hasChanges = true;
    }

    @Nonnull
    @Override
    public State sync() {
        return syncFunc != null ? syncFunc.sync(getGenericInstance()) : State.OK;
    }

    @Override
    protected State loadProperties(Configuration instance) {
        try {
            parseClass(getConfigurationInstance(), typeClass, null, staticMode ? null : getGenericInstance());
            return State.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return State.ERROR;
        }
    }

    protected String formatName(String name) {
        if (Character.isUpperCase(name.charAt(0)))
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
        return name.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    protected void parseClass(Configuration instance, Class<?> categoryClass, String parentCategoryName, Object classInstance) throws Exception {
        String categoryName = !StringUtils.isNotBlank(parentCategoryName) ? formatName(categoryClass.getSimpleName())
                : String.format("%s.%s", parentCategoryName, formatName(categoryClass.getSimpleName()));
        Name name = categoryClass.getDeclaredAnnotation(Name.class);
        if (name != null)
            categoryName = !StringUtils.isNotBlank(parentCategoryName) ? name.value()
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
            // Static field mode allowed only in readonly mode
            if (Modifier.isPublic(mods) && (!Modifier.isStatic(mods) || staticMode)) {
                type = parseType(field.getType());
                if (type != null)
                    parseField(instance, field, categoryName, type, field.getType().isArray(), staticMode ? null : field.get(classInstance));
                else if (!staticMode)
                    parseClass(instance, field.getType(), categoryName, field.get(classInstance));
            }
        }
        if (staticMode)
            for (Class<?> clazz : categoryClass.getDeclaredClasses()) {
                mods = clazz.getModifiers();
                if (Modifier.isStatic(mods) && Modifier.isPublic(mods))
                    parseClass(instance, clazz, categoryName, null);
            }
    }

    @SuppressWarnings("DuplicatedCode")
    protected void parseField(Configuration instance, Field field, String categoryName, Property.Type parsedType,
                              boolean isArray, Object classInstance) throws Exception {
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
                    DEFAULT_VALUES.putIfAbsent(field, defaultValue);
                    field.set(staticMode ? null : classInstance, property.getIntList());
                } else {
                    int defaultValue = field.getDeclaredAnnotation(DefaultInt.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    DEFAULT_VALUES.putIfAbsent(field, defaultValue);
                    field.set(staticMode ? null : classInstance, property.getInt());
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
                    DEFAULT_VALUES.putIfAbsent(field, defaultValue);
                    field.set(staticMode ? null : classInstance, property.getBooleanList());
                } else {
                    boolean defaultValue = field.getDeclaredAnnotation(DefaultBoolean.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    DEFAULT_VALUES.putIfAbsent(field, defaultValue);
                    field.set(staticMode ? null : classInstance, property.getBoolean());
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
                    DEFAULT_VALUES.putIfAbsent(field, defaultValue);
                    field.set(staticMode ? null : classInstance, property.getDoubleList());
                } else {
                    double defaultValue = field.getDeclaredAnnotation(DefaultDouble.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment);
                    DEFAULT_VALUES.putIfAbsent(field, defaultValue);
                    field.set(staticMode ? null : classInstance, defaultValue);
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
                    DEFAULT_VALUES.putIfAbsent(field, defaultValue);
                    field.set(staticMode ? null : classInstance, property.getStringList());
                } else {
                    String defaultValue = field.getDeclaredAnnotation(DefaultString.class).value();
                    property = instance.get(categoryName, propertyName, defaultValue,
                            propertyComment, parsedType);
                    DEFAULT_VALUES.putIfAbsent(field, defaultValue);
                    field.set(staticMode ? null : classInstance, property.getString());
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

    public void setSyncCallback(SyncCallback<T> syncFunc) {
        this.syncFunc = syncFunc;
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

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ArraySize {
        int value();
    }
}