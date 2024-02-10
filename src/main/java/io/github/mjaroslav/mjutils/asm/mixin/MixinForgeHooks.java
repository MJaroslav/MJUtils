package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.event.player.PlayerDestroyBlockInCreativeEvent;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeHooks.class)
public abstract class MixinForgeHooks {
    @Redirect(method = "onBlockBreakEvent", at = @At(value = "INVOKE", ordinal = 0,
        target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private static @NotNull Item overrideCreativeBlockBreakCancellingByEvent(@NotNull ItemStack stack) {
        return MinecraftForge.EVENT_BUS.post(new PlayerDestroyBlockInCreativeEvent(stack)) ?
            Items.diamond_sword : Items.stick;
    }
}
