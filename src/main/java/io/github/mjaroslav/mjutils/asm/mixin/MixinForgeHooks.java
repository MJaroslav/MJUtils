package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.event.BlockDestroyedInCreativeEvent;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeHooks.class)
public abstract class MixinForgeHooks {
    // Disabling block breaking for configurable items in creative mode
    @Redirect(method = "onBlockBreakEvent",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;",
            ordinal = 0))
    private static Item injected(ItemStack stack) {
        return MinecraftForge.EVENT_BUS.post(new BlockDestroyedInCreativeEvent(stack)) ?
            Items.diamond_sword : Items.stick;
    }
}
