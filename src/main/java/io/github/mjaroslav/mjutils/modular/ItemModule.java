package io.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.mjaroslav.sharedjava.reflect.ReflectionHelper;
import lombok.Getter;
import lombok.val;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.Set;

import static io.github.mjaroslav.mjutils.internal.lib.ModInfo.*;

@Getter
public class ItemModule {
    private static Set<ASMDataTable.ASMData> asmRawItems;

    protected final @NotNull String rootPackage;

    public ItemModule(@Nullable String rootPackage) {
        this.rootPackage = rootPackage != null ? rootPackage : ReflectionHelper.getPackage(this);
    }

    public void listen(@NotNull FMLConstructionEvent event) {
        if (asmRawItems == null) {
            LOG_MODULES.info("Collecting classes annotated with @SubscribeItem for handling Item registration");
            asmRawItems = event.getASMHarvestedData().getAll(SubscribeItem.class.getName());
        }
    }

    public void listen(@NotNull FMLPreInitializationEvent event) {
        asmRawItems.stream().filter(data -> data.getClassName().startsWith(getRootPackage())).forEach(data -> {
            val className = data.getClassName();
            try {
                LOG_MODULES.debug(String.format("Found annotated with @SubscribeItem %s class", className));
                val name = (String) data.getAnnotationInfo().get("value");
                val itemInstance = (Item) Class.forName(className).getConstructor().newInstance();
                GameRegistry.registerItem(itemInstance, name);
                val registryName = Item.itemRegistry.getNameForObject(itemInstance);
                LOG_MODULES.trace(String.format("Annotated %s Item registered with %s name", className,
                    registryName));
                val itemField = getClass().getField(name);
                val mods = itemField.getModifiers();
                if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && !Modifier.isFinal(mods)
                    && Item.class.isAssignableFrom(itemField.getType())) {
                    itemField.set(null, itemInstance);
                    LOG_MODULES.trace(String.format("Item %s placed to field %s#%s",
                        registryName, getClass().getName(), itemField.getName()));
                } else
                    LOG_MODULES.error(String.format("Can't place Item %s to field %s#%s, holder field must be public" +
                            " and static and be or extends Item class", registryName, getClass().getName(),
                        itemField.getName()));
            } catch (NoSuchMethodException | ClassCastException e) {
                LOG_MODULES.error(String.format("Can't handle annotated with @SubscribeItem %s class. " +
                    "Annotated classes must extends Item class and have default public constructor", className), e);
            } catch (Exception other) {
                LOG_MODULES.error(String.format("Can't handle annotated with @SubscribeItem %s class", className), other);
            }
        });
    }
}
