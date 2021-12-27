package com.github.mjaroslav.mjutils.gui;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GuiText implements ScrollingContentHeight {
    @Nonnull
    protected final FontRenderer renderer;

    protected final boolean splitText, centerString, enableShadows, packHeight;
    protected final int xOffset, extraYOffset, width, height, color;
    protected final double zLevel;
    protected final int textHeight;
    @Getter
    protected final String[] textCache;

    @Override
    public void drawScreen(int xBegin, int yOffset, float floatTicks) {
        for (int i = 0; i < textCache.length; i++) {
            String line = textCache[i];
            if (centerString && width > 0)
                renderer.drawString(line, xBegin + xOffset + ((width - renderer.getStringWidth(line)) / 2), yOffset + i * renderer.FONT_HEIGHT, color, enableShadows);
            else
                renderer.drawString(line, xBegin + xOffset, yOffset + i * renderer.FONT_HEIGHT, color, enableShadows);
        }
    }

    @Override
    public void drawDebugLines(int xBegin, int yOffset, float floatTicks) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glLineWidth(2f);
        GL11.glBegin(GL11.GL_LINE_LOOP);

        GL11.glColor4f(0f, 0f, 1f, 1f);
        GL11.glVertex3d(xBegin, yOffset, zLevel);
        GL11.glVertex3d(xBegin + width, yOffset, zLevel);
        GL11.glVertex3d(xBegin + width, yOffset + getContentHeight(), zLevel);
        GL11.glVertex3d(xBegin, yOffset + getContentHeight(), zLevel);
        GL11.glEnd();

        GL11.glLineWidth(1f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor4f(0f, 1f, 0f, 1f);
        GL11.glVertex3d(xBegin + xOffset, yOffset, zLevel);
        GL11.glVertex3d(xBegin + xOffset, yOffset + getContentHeight(), zLevel);
        GL11.glVertex3d(xBegin, yOffset + getContentHeight() - extraYOffset, zLevel);
        GL11.glVertex3d(xBegin + width, yOffset + getContentHeight() - extraYOffset, zLevel);
        GL11.glEnd();

        for (int i = 0; i < textCache.length; i++) {
            String line = textCache[i];
            GL11.glLineWidth(1f);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glColor4f(1f, 0f, 0f, 1f);
            int contentWidth = renderer.getStringWidth(line);
            int contentX;
            if (centerString) {
                contentX = ((width - renderer.getStringWidth(line)) / 2);
            } else {
                contentX = 0;
            }
            GL11.glVertex3d(xBegin + xOffset + contentX, yOffset + i * renderer.FONT_HEIGHT, zLevel);
            GL11.glVertex3d(xBegin + xOffset + contentX + contentWidth, yOffset + i * renderer.FONT_HEIGHT, zLevel);
            GL11.glVertex3d(xBegin + xOffset + contentX + contentWidth, yOffset + (i + 1) * renderer.FONT_HEIGHT, zLevel);
            GL11.glVertex3d(xBegin + xOffset + contentX, yOffset + (i + 1) * renderer.FONT_HEIGHT, zLevel);
            GL11.glEnd();
        }

        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public int getContentHeight() {
        int size = height > 0 ? packHeight ? textHeight : height : textHeight;
        return size > 0 ? size + extraYOffset : 0;
    }

    @Builder(setterPrefix = "set")
    protected static GuiText $build(StringBuilder builder, @Nullable FontRenderer renderer, boolean splitText, boolean centerString, boolean enableShadows,
                                    boolean packHeight, int xOffset, int extraYOffset, int width, int height, int color, int zLevel) {
        if (renderer == null)
            renderer = Minecraft.getMinecraft().fontRenderer;
        List<String> result = new ArrayList<>();
        String[] split = builder.toString().trim().split("\n");
        if (splitText && width > 0) {
            for (String line : split)
                for (Object splitLine : renderer.listFormattedStringToWidth(line, width - xOffset))
                    result.add((String) splitLine);
        } else
            result.addAll(Arrays.asList(split));
        String[] textCache = result.toArray(new String[0]);
        int textHeight = textCache.length * renderer.FONT_HEIGHT;
        return new GuiText(renderer, splitText, centerString, enableShadows, packHeight, xOffset, extraYOffset, width, height, color, zLevel, textHeight, textCache);
    }

    @SuppressWarnings({"unused", "UnusedReturnValue"})
    public static class GuiTextBuilder {
        protected final StringBuilder builder = new StringBuilder();

        protected GuiTextBuilder setBuilder(StringBuilder builder) {
            return this; // Disabled;
        }

        public GuiTextBuilder append(Object text) {
            builder.append(text);
            return this;
        }

        public GuiTextBuilder appendLine(Object text) {
            builder.append("\n").append(text);
            return this;
        }

        public GuiTextBuilder appendTranslated(String key, Object... args) {
            return append(I18n.format(key, args));
        }

        public GuiTextBuilder appendTranslatedLine(String key, Object... args) {
            return appendLine(I18n.format(key, args));
        }

        public GuiTextBuilder set(Object text) {
            clearLines();
            append(text);
            return this;
        }

        public GuiTextBuilder setTranslated(String key, Object... args) {
            clearLines();
            appendTranslated(key, args);
            return this;
        }

        public GuiTextBuilder clearLines() {
            builder.setLength(0);
            return this;
        }
    }
}
