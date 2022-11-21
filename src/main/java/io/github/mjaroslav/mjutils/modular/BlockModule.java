package io.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.mjaroslav.mjutils.internal.lib.ModInfo;
import io.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.util.Set;

public class BlockModule {
    private static Set<ASMDataTable.ASMData> asmRawBlocks;

    protected String rootPackage;

    public String getBlocksRootPackage() {
        if (rootPackage == null) rootPackage = UtilsReflection.getPackageFromClass(this);
        return rootPackage;
    }

    public void listen(@NotNull FMLConstructionEvent event) {
        if (asmRawBlocks == null) asmRawBlocks = event.getASMHarvestedData().getAll(SubscribeBlock.class.getName());
    }

    public void listen(FMLPreInitializationEvent event) {
        for (var data : asmRawBlocks)
            if (data.getClassName().startsWith(getBlocksRootPackage()))
                try {
                    val name = (String) data.getAnnotationInfo().get("value");
                    val blockInstance = (Block) Class.forName(data.getClassName()).getConstructor().newInstance();
                    if (data.getAnnotationInfo().containsKey("itemBlockClass")) {
                        @SuppressWarnings("unchecked")
                        val itemBlockClass = (Class<? extends ItemBlock>) Class.forName((String) data.getAnnotationInfo()
                            .get("itemBlockClass"));
                        GameRegistry.registerBlock(blockInstance, itemBlockClass, name);
                    } else GameRegistry.registerBlock(blockInstance, name);
                    val blockField = getClass().getField(name);
                    val mods = blockField.getModifiers();
                    if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && !Modifier.isFinal(mods)) {
                        blockField.set(this, blockInstance);
                        ModInfo.loggerModules.debug("Auto registration of block \"%s\"",
                            Block.blockRegistry.getNameForObject(blockInstance));
                    } else
                        ModInfo.loggerModules.error("Block \"%s\" not registered, field for it must be static and public",
                            data.getClassName());
                } catch (Exception e) {
                    ModInfo.loggerModules.error("Can't register block \"%s\"", e, data.getClassName());
                }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface SubscribeBlock {
        @Nonnull String value();

        @Nonnull String itemBlockClass() default "";
    }
}
