package io.github.mjaroslav.mjutils.asm.mixin;

import cpw.mods.fml.client.config.GuiButtonExt;
import io.github.mjaroslav.mjutils.mod.lib.General.Client.GuiReplacements;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static io.github.mjaroslav.mjutils.config.ForgeAnnotationConfig.*;

@Mixin(GuiButtonExt.class)
public abstract class MixinGuiButtonExt extends GuiButton {
    public MixinGuiButtonExt() {
        super(0, 0, 0, null);
    }

    @ModifyArg(method = "drawButton", at = @At(value = "INVOKE", target = "Lcpw/mods/fml/client/config/GuiButtonExt;" +
        "drawCenteredString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"),
        index = 4)
    public int inject(int original) {
        if (!GuiReplacements.colors || displayString == null || !enabled) return original;
        if (HEX_COLOR_PATTERN.matcher(displayString).matches() || HEX_COLOR_ALPHA_PATTERN.matcher(displayString)
            .matches()) return Integer.parseInt(displayString.substring(1), 16);
        else return original;
    }
}
