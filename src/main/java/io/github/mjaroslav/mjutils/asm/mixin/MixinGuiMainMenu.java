package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.lib.General.Client;
import io.github.mjaroslav.mjutils.util.object.game.ResourcePath;
import io.github.tox1cozz.mixinextras.injector.ModifyExpressionValue;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Random;
import java.util.stream.Collectors;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {
    @Unique // Random direction per session
    private static final int mjutils$START_TIMER_SPEED = System.currentTimeMillis() % 2 == 0 ? 1 : -1;
    @Shadow
    private static @Final ResourceLocation splashTexts;
    @Shadow
    private static @Final Random rand;
    @Shadow
    private String splashText;
    @Shadow
    private int panoramaTimer;
    @Unique
    private int mjtuils$panoramaTimerSpeed = mjutils$START_TIMER_SPEED;

    @Shadow
    protected abstract void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_);

    @ModifyExpressionValue(method = "drawScreen",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiMainMenu;updateCounter:F"))
    private float injected(float original) {
        // Override change of "Minceraft" logo
        return Client.overrideMinceraftChance > original ? 0 : 1;
    }

    @Inject(method = "mouseClicked",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;mouseClicked(III)V", shift = Shift.AFTER))
    private void injected(int x, int y, int event, @NotNull CallbackInfo ci) {
        if (event != 0) return;
        // Change new splash when clicked on MC logo
        val logoWidth = 274;
        val logoHeight = 44;
        val logoX = width / 2 - logoWidth / 2;
        val logoY = 30;
        if (x >= logoX && x <= logoX + logoWidth && y >= logoY && y <= logoY + logoHeight) {
            // TODO: May be no load file EVERY time?
            try (val reader = ResourcePath.of(splashTexts).bufferedReader()) {
                val texts = reader.lines().map(String::trim).filter(line -> !line.isEmpty()).collect(Collectors.toList());
                if (!texts.isEmpty()) {
                    do {
                        splashText = texts.get(rand.nextInt(texts.size()));
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord
                            .func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                    } while (splashText.hashCode() == 125780783);
                }
            } catch (IOException ignored) {
            }
        }
    }

    @Inject(method = "updateScreen", at = @At("HEAD"), cancellable = true)
    private void injected(@NotNull CallbackInfo ci) {
        // Overwrite timer behavior
        panoramaTimer += mjtuils$panoramaTimerSpeed;
        ci.cancel();
    }

    @Inject(method = "keyTyped", at = @At("HEAD"))
    private void injected(char key, int keyCode, @NotNull CallbackInfo ci) {
        // Handle timer speed control
        if (keyCode == Keyboard.KEY_UP) mjtuils$panoramaTimerSpeed++;
        if (keyCode == Keyboard.KEY_DOWN) mjtuils$panoramaTimerSpeed--;
        if (keyCode == Keyboard.KEY_SPACE) mjtuils$panoramaTimerSpeed = mjutils$START_TIMER_SPEED;
    }

    @Redirect(method = "renderSkybox",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawPanorama(IIF)V"))
    private void injected(@NotNull GuiMainMenu instance, int x, int y, float partialTicks) {
        // Prevent "tremor" glitch if no panorama rotation
        drawPanorama(x, y, mjtuils$panoramaTimerSpeed == 0 ? 0 : mjtuils$panoramaTimerSpeed < 0 ? -partialTicks : partialTicks);
    }
}
