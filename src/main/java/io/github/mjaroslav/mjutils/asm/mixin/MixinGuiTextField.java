package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.internal.lib.General.Client.GuiReplacements;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static io.github.mjaroslav.mjutils.config.ForgeAnnotationConfig.*;

@Mixin(GuiTextField.class)
public abstract class MixinGuiTextField extends Gui {
    @Shadow
    private boolean isEnabled;
    @Shadow
    private String text;

    @ModifyArg(method = "drawTextBox", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;III)I"),
        index = 3)
    public int changeColor(int original) {
        if (!GuiReplacements.colors || text == null || !isEnabled) return original;
        if (HEX_COLOR_PATTERN.matcher(text).matches() || HEX_COLOR_ALPHA_PATTERN.matcher(text).matches())
            return Integer.parseInt(text.substring(1), 16);
        else return original;
    }
}
