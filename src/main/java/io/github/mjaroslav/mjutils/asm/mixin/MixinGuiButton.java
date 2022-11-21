package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.internal.lib.General.Client.GuiReplacements;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static io.github.mjaroslav.mjutils.config.ForgeAnnotationConfig.*;

@Mixin(GuiButton.class)
public abstract class MixinGuiButton extends Gui {
    @Shadow
    public boolean enabled;
    @Shadow
    public String displayString;
    @Shadow(remap = false)
    protected boolean field_146123_n;

    @ModifyArg(method = "drawButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiButton;" +
        "drawCenteredString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"),
        index = 4)
    public int inject(int original) {
        if (!GuiReplacements.colors || displayString == null || field_146123_n || !enabled) return original;
        if (HEX_COLOR_PATTERN.matcher(displayString).matches() || HEX_COLOR_ALPHA_PATTERN.matcher(displayString)
            .matches()) return Integer.parseInt(displayString.substring(1), 16);
        else return original;
    }
}
