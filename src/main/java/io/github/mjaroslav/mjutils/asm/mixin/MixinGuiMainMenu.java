package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.internal.lib.General.Client;
import io.github.tox1cozz.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu {
    @ModifyExpressionValue(method = "drawScreen",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiMainMenu;updateCounter:F"))
    private float injected(float original) {
        return Client.overrideMinceraftChance < original ? 0 : 1;
    }
}
