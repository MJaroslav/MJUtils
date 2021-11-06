package com.github.mjaroslav.mjutils.modular;

import com.github.mjaroslav.mjutils.modular.Modular;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import lombok.Getter;
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

public class BlockModule implements Modular {
    @Getter
    protected String blocksPackage;

    private Set<ASMDataTable.ASMData> rawBlocks;

    @Override
    public void listen(FMLConstructionEvent event) {
        blocksPackage = getClass().getName();
        blocksPackage = blocksPackage.substring(0, blocksPackage.lastIndexOf("."));
        rawBlocks = event.getASMHarvestedData().getAll(SubscribeBlock.class.getName());
    }

    @Override
    public void listen(FMLPreInitializationEvent event) {
        for (ASMDataTable.ASMData data : rawBlocks)
            if (data.getClassName().startsWith(blocksPackage)) {
                try {
                    String name = (String) data.getAnnotationInfo().get("value");
                    Block blockInstance = (Block) Class.forName(data.getClassName()).newInstance();
                    if (data.getAnnotationInfo().containsKey("itemBlockClass")) {
                        @SuppressWarnings("unchecked")
                        Class<? extends ItemBlock> itemBlockClass = (Class<? extends ItemBlock>) Class.forName((String) data.getAnnotationInfo().get("itemBlockClass"));
                        GameRegistry.registerBlock(blockInstance, itemBlockClass, name);
                    } else GameRegistry.registerBlock(blockInstance, name);
                    Field blockField = getClass().getField(name);
                    int mods = blockField.getModifiers();
                    if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && !Modifier.isFinal(mods))
                        blockField.set(this, blockInstance);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException e) {
                    e.printStackTrace();
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
