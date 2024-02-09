package io.github.mjaroslav.mjutils.asm.mixin.compatibility.thaumcraft;

import io.github.mjaroslav.mjutils.util.object.game.ResearchItemShadow;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.client.gui.GuiResearchPopup;

@Mixin(value = GuiResearchPopup.class, remap = false)
public abstract class MixinGuiResearchPopup {
    @Inject(method = "queueResearchInformation", at = @At("HEAD"), cancellable = true
        , locals = LocalCapture.CAPTURE_FAILSOFT)
    private void injected(@NotNull ResearchItem research, @NotNull CallbackInfo ci) {
        if (research instanceof ResearchItemShadow) ci.cancel(); // Prevent popup spam with copies
    }
}
