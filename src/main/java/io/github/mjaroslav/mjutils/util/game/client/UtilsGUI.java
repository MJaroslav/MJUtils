package io.github.mjaroslav.mjutils.util.game.client;

import io.github.mjaroslav.mjutils.util.UtilsFormat;
import io.github.mjaroslav.mjutils.util.UtilsFormat.ColorFormat;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import io.github.mjaroslav.mjutils.util.net.UtilsDesktop;
import com.google.common.collect.Sets;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.common.FMLLog;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;

import static io.github.mjaroslav.mjutils.util.UtilsFormat.ColorFormat.ARGB;
import static org.lwjgl.opengl.GL11.*;

@Log4j2
@UtilityClass
public class UtilsGUI {
    private final Set<String> SUPPORTED_PROTOCOLS = Sets.newHashSet("http", "https");

    public void openURL(@NotNull GuiScreen from, @NotNull String url) {
        try {
            openURL(from, new URI(url));
        } catch (URISyntaxException e) {
            log.error("Can't open url " + url, e);
        }
    }

    public void openURL(@NotNull GuiScreen from, @NotNull URL url) {
        try {
            openURL(from, url.toURI());
        } catch (URISyntaxException e) {
            log.error("Can't open url " + url, e);
        }
    }

    public void openURL(@NotNull GuiScreen from, @NotNull URI uri) {
        val mc = Minecraft.getMinecraft();
        if (!SUPPORTED_PROTOCOLS.contains(uri.getScheme().toLowerCase()))
            log.warn(uri + " not opened because have unsupported protocol: " + uri.getScheme().toLowerCase());
        if (mc.gameSettings.chatLinksPrompt) {
            mc.displayGuiScreen(new GuiConfirmOpenLink((confirmed, id) -> {
                if (confirmed && id == 0)
                    UtilsDesktop.openURL(uri);
                mc.displayGuiScreen(from);
            }, uri.toString(), 0, false));
        } else
            UtilsDesktop.openURL(uri);
    }

    public void glScissor(int x, int y, int width, int height) {
        val mc = Minecraft.getMinecraft();
        val resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        var scale = resolution.getScaleFactor();
        var scissorWidth = width * scale;
        var scissorHeight = height * scale;
        var scissorX = x * scale;
        var scissorY = mc.displayHeight - scissorHeight - (y * scale);
        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }

    public void drawGradientRect(int left, int top, int right, int bottom, int firstColorARGB,
                                 int secondColorARGB) {
        drawGradientRect(left, top, right, bottom, firstColorARGB, secondColorARGB, ARGB);
    }

    public void drawGradientRect(int left, int top, int right, int bottom, int firstColor, int secondColor,
                                 @NotNull ColorFormat colorFormat) {
        val first = UtilsFormat.unpackColorIntToFloatArray(firstColor, colorFormat, ARGB);
        val second = UtilsFormat.unpackColorIntToFloatArray(secondColor, colorFormat, ARGB);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glDisable(GL_ALPHA_TEST);
        // It's correct constants?
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glShadeModel(GL_SMOOTH);
        val t = Tessellator.instance;
        t.startDrawingQuads();
        t.setColorRGBA_F(first[1], first[2], first[2], first[0]);
        t.addVertex(right, top, 0D);
        t.addVertex(left, top, 0D);
        t.setColorRGBA_F(second[1], second[2], second[2], second[0]);
        t.addVertex(left, bottom, 0D);
        t.addVertex(right, bottom, 0D);
        t.draw();
        glShadeModel(GL_FLAT);
        glDisable(GL_BLEND);
        glEnable(GL_ALPHA_TEST);
        glEnable(GL_TEXTURE_2D);
    }

    public int getButtonHoverState(int mouseX, int mouseY, @NotNull GuiButton button, boolean checkForEnabling) {
        if (!button.visible)
            return -1;
        var hovered = mouseX >= button.xPosition && mouseX <= button.xPosition + button.width &&
                mouseY >= button.yPosition && mouseY <= button.yPosition + button.height;
        return hovered ? checkForEnabling ? button.enabled ? 2 : 0 : 2 : 1;
    }

    public @Nullable GuiConfig createModGUIConfig(@Nullable String modId, @NotNull GuiScreen from) {
        try {
            return (GuiConfig) FMLClientHandler.instance().getGuiFactoryFor(UtilsMods.getContainer(modId))
                    .mainConfigGuiClass().getConstructor(GuiScreen.class).newInstance(from);
        } catch (Exception e) {
            FMLLog.log(Level.ERROR, e, "There was a critical issue trying to build the config GUI for %s", modId);
            return null;
        }
    }

    public boolean isModHaveGUIConfig(@NotNull String modId) {
        return Optional.ofNullable(UtilsMods.getContainer(modId))
                .map(FMLClientHandler.instance()::getGuiFactoryFor)
                .map(IModGuiFactory::mainConfigGuiClass).isPresent();
    }
}
