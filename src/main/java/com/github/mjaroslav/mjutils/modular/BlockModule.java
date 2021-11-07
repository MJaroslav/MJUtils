package com.github.mjaroslav.mjutils.modular;

import com.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

public class BlockModule {
    private static Set<ASMDataTable.ASMData> asmRawBlocks;

    protected String rootDirectory;

    public String getBlocksRootDirectory() {
        if (rootDirectory == null)
            rootDirectory = UtilsReflection.getPackageFromClass(this);
        return rootDirectory;
    }

    public void listen(FMLConstructionEvent event) {
        if (asmRawBlocks == null)
            asmRawBlocks = event.getASMHarvestedData().getAll(SubscribeBlock.class.getName());
    }

    public void listen(FMLPreInitializationEvent event) {
        for (ASMDataTable.ASMData data : asmRawBlocks) {
            if (data.getClassName().startsWith(getBlocksRootDirectory())) {
                try {
                    String name = (String) data.getAnnotationInfo().get("value");
                    Block blockInstance = (Block) Class.forName(data.getClassName()).newInstance();
                    if (data.getAnnotationInfo().containsKey("itemBlockClass")) {
                        @SuppressWarnings("unchecked")
                        Class<? extends ItemBlock> itemBlockClass = (Class<? extends ItemBlock>) Class.forName((String) data.getAnnotationInfo().get("itemBlockClass"));
                        GameRegistry.registerBlock(blockInstance, itemBlockClass, name);
                    } else
                        GameRegistry.registerBlock(blockInstance, name);
                    Field blockField = getClass().getField(name);
                    int mods = blockField.getModifiers();
                    if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && !Modifier.isFinal(mods)) {
                        blockField.set(this, blockInstance);
                        ModuleLoader.log.debug("Auto registration of block \"%s\"",
                                Block.blockRegistry.getNameForObject(blockInstance));
                    } else ModuleLoader.log.error("Block \"%s\" not registered, field for it must be static and public",
                            data.getClassName());
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException e) {
                    ModuleLoader.log.error("Can't register block \"%s\"", e, data.getClassName());
                }
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface SubscribeBlock {
        @Nonnull String value();

        @Nonnull String itemBlockClass() default "";
    }
}