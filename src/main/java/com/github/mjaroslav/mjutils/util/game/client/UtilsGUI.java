package com.github.mjaroslav.mjutils.util.game.client;

import com.github.mjaroslav.mjutils.util.UtilsFormat;
import com.github.mjaroslav.mjutils.util.game.UtilsMods;
import com.github.mjaroslav.mjutils.util.net.UtilsNet;
import com.google.common.collect.Sets;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ModContainer;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

@Log4j2
public class UtilsGUI {
    private static final Set<String> SUPPORTED_PROTOCOLS = Sets.newHashSet("http", "https");

    public static void openURL(GuiScreen from, String url) {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            URI uri = new URI(url);

            if (!SUPPORTED_PROTOCOLS.contains(uri.getScheme().toLowerCase()))
                throw new URISyntaxException(url, "Unsupported protocol: " + uri.getScheme().toLowerCase());
            if (mc.gameSettings.chatLinksPrompt) {
                mc.displayGuiScreen(new GuiConfirmOpenLink((confirmed, id) -> {
                    if (confirmed && id == 0)
                        UtilsNet.openURL(uri);
                    mc.displayGuiScreen(from);
                }, url, 0, false));
            } else
                UtilsNet.openURL(uri);
        } catch (URISyntaxException urisyntaxexception) {
            log.error("Can't open url " + url, urisyntaxexception);
        }
    }

    public static void glScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scale = resolution.getScaleFactor();
        int scissorWidth = width * scale;
        int scissorHeight = height * scale;
        int scissorX = x * scale;
        int scissorY = mc.displayHeight - scissorHeight - (y * scale);
        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int firstARGB, int secondARGB) {
        float[] first = UtilsFormat.unpackARGBInt(firstARGB);
        float[] second = UtilsFormat.unpackARGBInt(secondARGB);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        // It's correct constants?
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(first[1], first[2], first[2], first[0]);
        tessellator.addVertex(right, top, 0D);
        tessellator.addVertex(left, top, 0D);
        tessellator.setColorRGBA_F(second[1], second[2], second[2], second[0]);
        tessellator.addVertex(left, bottom, 0D);
        tessellator.addVertex(right, bottom, 0D);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static int getButtonHoverState(int mouseX, int mouseY, GuiButton button, boolean checkForEnabling) {
        if (!button.visible)
            return -1;
        boolean hovered = mouseX >= button.xPosition && mouseX <= button.xPosition + button.width &&
                mouseY >= button.yPosition && mouseY <= button.yPosition + button.height;
        return hovered ? checkForEnabling ? button.enabled ? 2 : 0 : 2 : 1;
    }

    @Nullable
    public static GuiConfig createModGUIConfig(String modId, GuiScreen from) {
        try {
            ModContainer mod = UtilsMods.getContainer(modId);
            IModGuiFactory guiFactory = FMLClientHandler.instance().getGuiFactoryFor(mod);
            return (GuiConfig) guiFactory.mainConfigGuiClass().getConstructor(GuiScreen.class).newInstance(from);
        } catch (Exception e) {
            FMLLog.log(Level.ERROR, e, "There was a critical issue trying to build the config GUI for %s", modId);
            return null;
        }
    }

    public static boolean isModHaveGUIConfig(String modId) {
        ModContainer mod = UtilsMods.getContainer(modId);
        if (mod == null)
            return false;
        IModGuiFactory guiFactory = FMLClientHandler.instance().getGuiFactoryFor(mod);
        return guiFactory != null && guiFactory.mainConfigGuiClass() != null;
    }
}
