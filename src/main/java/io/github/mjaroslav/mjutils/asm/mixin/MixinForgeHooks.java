package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.util.game.UtilsInteractions;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
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
        return UtilsInteractions.isBlockBreakingDisabledInCreative(stack) ? Items.diamond_sword : stack.getItem();
    }
}
