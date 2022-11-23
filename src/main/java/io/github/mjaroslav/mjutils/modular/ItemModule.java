package io.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.mjaroslav.mjutils.internal.lib.ModInfo;
import io.github.mjaroslav.mjutils.util.UtilsReflection;
import lombok.val;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.util.Set;

public class ItemModule {
    private static Set<ASMDataTable.ASMData> asmRawItems;

    protected String rootPackage;

    public String getItemsRootPackage() {
        if (rootPackage == null) rootPackage = UtilsReflection.getPackageFromClass(this);
        return rootPackage;
    }

    public void listen(@NotNull FMLConstructionEvent event) {
        if (asmRawItems == null) asmRawItems = event.getASMHarvestedData().getAll(SubscribeItem.class.getName());
    }

    public void listen(@NotNull FMLPreInitializationEvent event) {
        for (var data : asmRawItems)
            if (data.getClassName().startsWith(getItemsRootPackage()))
                try {
                    val name = (String) data.getAnnotationInfo().get("value");
                    val itemInstance = (Item) Class.forName(data.getClassName()).getConstructor().newInstance();
                    GameRegistry.registerItem(itemInstance, name);
                    val itemField = getClass().getField(name);
                    val mods = itemField.getModifiers();
                    if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && !Modifier.isFinal(mods)) {
                        itemField.set(this, itemInstance);
                        ModInfo.loggerModules.debug("Auto registration of item \"%s\"",
                            Item.itemRegistry.getNameForObject(itemInstance));
                    } else
                        ModInfo.loggerModules.error("Item \"%s\" not registered, field for it must be static and public",
                            data.getClassName());
                } catch (Exception e) {
                    ModInfo.loggerModules.error("Can't register item \"%s\"", e, data.getClassName());
                }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface SubscribeItem {
        @Nonnull String value();
    }
}
