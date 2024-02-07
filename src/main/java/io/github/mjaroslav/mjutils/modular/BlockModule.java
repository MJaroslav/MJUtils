package io.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.mjaroslav.sharedjava.reflect.ReflectionHelper;
import lombok.Getter;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.Set;

import static io.github.mjaroslav.mjutils.internal.lib.ModInfo.*;

@Getter
public class BlockModule {
    private static Set<ASMDataTable.ASMData> asmRawBlocks;

    protected final @NotNull String rootPackage;

    public BlockModule(@Nullable String rootPackage) {
        this.rootPackage = rootPackage != null ? rootPackage : ReflectionHelper.getPackage(this);
    }

    public void listen(@NotNull FMLConstructionEvent event) {
        if (asmRawBlocks == null) {
            LOG_MODULES.info("Collecting classes annotated with @SubscribeBlock for handling Block registration");
            asmRawBlocks = event.getASMHarvestedData().getAll(SubscribeBlock.class.getName());
        }
    }

    public void listen(@NotNull FMLPreInitializationEvent event) {
        asmRawBlocks.stream().filter(data -> data.getClassName().startsWith(getRootPackage())).forEach(data -> {
            val className = data.getClassName();
            try {
                LOG_MODULES.debug(String.format("Found annotated with @SubscribeBlock %s class", className));
                val name = (String) data.getAnnotationInfo().get("value");
                val blockInstance = (Block) Class.forName(className).getConstructor().newInstance();
                if (data.getAnnotationInfo().containsKey("itemBlockClass")) {
                    val itemBlockClassName = (String) data.getAnnotationInfo().get("itemBlockClass");
                    if (StringUtils.isEmpty(itemBlockClassName)) { // Fallback
                        LOG_MODULES.warn(String.format("Found itemBlockClass parameter in @SubscribeBlock annotation " +
                            "for %s Block class, but its value is empty or null. Block will registered anyway, but " +
                            "you should check this parameter for correct", className));
                        GameRegistry.registerBlock(blockInstance, name);
                    } else {
                        LOG_MODULES.trace(String.format("Found %s ItemBlock class for %s Block class", itemBlockClassName,
                            className));
                        @SuppressWarnings("unchecked")
                        val itemBlockClass = (Class<? extends ItemBlock>) Class.forName(itemBlockClassName);
                        GameRegistry.registerBlock(blockInstance, itemBlockClass, name);
                    }
                } else GameRegistry.registerBlock(blockInstance, name);
                val registryName = Block.blockRegistry.getNameForObject(blockInstance);
                LOG_MODULES.trace(String.format("Annotated %s Block registered with %s name", className,
                    registryName));
                val blockField = getClass().getField(name);
                val mods = blockField.getModifiers();
                if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && !Modifier.isFinal(mods) &&
                    Block.class.isAssignableFrom(blockField.getType())) {
                    blockField.set(null, blockInstance);
                    LOG_MODULES.trace(String.format("Block %s placed to field %s#%s",
                        registryName, getClass().getName(), blockField.getName()));
                } else
                    LOG_MODULES.error(String.format("Can't place Block %s to field %s#%s, holder field must be public" +
                            " and static and be or extends Block class", registryName, getClass().getName(),
                        blockField.getName()));
            } catch (NoSuchMethodException | ClassCastException e) {
                LOG_MODULES.error(String.format("Can't handle annotated with @SubscribeBlock %s class. " +
                    "Annotated classes must extends Block class and have default public constructor. If " +
                    "itemBlockClass specified, that class must extends ItemBlock and have public constructor " +
                    "with Block as parameter", className), e);
            } catch (Exception other) {
                LOG_MODULES.error(String.format("Can't handle annotated with @SubscribeBlock %s class", className), other);
            }
        });
    }
}
