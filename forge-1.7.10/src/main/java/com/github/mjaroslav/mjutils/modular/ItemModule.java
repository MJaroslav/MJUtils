package com.github.mjaroslav.mjutils.modular;

import com.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

public class ItemModule {
    private static Set<ASMDataTable.ASMData> asmRawItems;

    protected String rootPackage;

    public String getItemsRootPackage() {
        if (rootPackage == null)
            rootPackage = UtilsReflection.getPackageFromClass(this);
        return rootPackage;
    }

    public void listen(FMLConstructionEvent event) {
        if (asmRawItems == null)
            asmRawItems = event.getASMHarvestedData().getAll(SubscribeItem.class.getName());
    }

    public void listen(FMLPreInitializationEvent event) {
        for (ASMDataTable.ASMData data : asmRawItems) {
            if (data.getClassName().startsWith(getItemsRootPackage())) {
                try {
                    String name = (String) data.getAnnotationInfo().get("value");
                    Item itemInstance = (Item) Class.forName(data.getClassName()).newInstance();
                    GameRegistry.registerItem(itemInstance, name);
                    Field itemField = getClass().getField(name);
                    int mods = itemField.getModifiers();
                    if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && !Modifier.isFinal(mods)) {
                        itemField.set(this, itemInstance);
                        ModuleLoader.log.debug("Auto registration of item \"%s\"",
                                Item.itemRegistry.getNameForObject(itemInstance));
                    } else ModuleLoader.log.error("Item \"%s\" not registered, field for it must be static and public",
                            data.getClassName());
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException e) {
                    ModuleLoader.log.error("Can't register item \"%s\"", e, data.getClassName());
                }
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface SubscribeItem {
        @Nonnull String value();
    }
}
