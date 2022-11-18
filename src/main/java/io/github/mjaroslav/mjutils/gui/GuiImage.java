package io.github.mjaroslav.mjutils.gui;

import io.github.mjaroslav.mjutils.object.Pair;
import io.github.mjaroslav.mjutils.object.game.client.CachedImage;
import io.github.mjaroslav.mjutils.util.io.ResourcePath;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.awt.*;

@RequiredArgsConstructor
public class GuiImage implements ScrollingContentElement {
    protected final int width, height;
    protected final int extraYOffset;
    protected final double zLevel, contentWidth, contentHeight, contentX, contentY;
    @Nonnull
    protected final CachedImage image;
    protected final boolean packHeight;
    @Getter
    protected final OnMouseClickListener onMouseClickListener;

    @Override
    public Pair<Boolean, Pair<Integer, Integer>> isHovered(GuiContentScrollingPane parent, int xBegin, int yOffset, int mouseX, int mouseY) {
        val result = new Pair<Boolean, Pair<Integer, Integer>>();
        if (mouseX >= contentX + xBegin && mouseX <= contentX + xBegin + contentWidth &&
            mouseY >= contentY + yOffset && mouseY <= contentY + yOffset + contentHeight) {
            result.setX(true);
            result.setY(new Pair<>((int) (contentX + xBegin + contentWidth - mouseX),
                (int) (contentY + yOffset + contentHeight - mouseY)));
        } else result.setX(false);
        return result;
    }

    @Override
    public void drawScreen(int xBegin, int yOffset, boolean hovered, float floatTicks) {
        if (!image.isLoaded())
            return;
        Minecraft.getMinecraft().renderEngine.bindTexture(image.getLocation());
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(xBegin + contentX, yOffset + contentY + contentHeight, zLevel, 0, 1);
        tess.addVertexWithUV(xBegin + contentX + contentWidth, yOffset + contentY + contentHeight, zLevel, 1, 1);
        tess.addVertexWithUV(xBegin + contentX + contentWidth, yOffset + contentY, zLevel, 1, 0);
        tess.addVertexWithUV(xBegin + contentX, yOffset + contentY, zLevel, 0, 0);
        tess.draw();
    }

    @Override
    public int getContentHeight() {
        return (int) ((packHeight ? contentHeight : height) + extraYOffset);
    }

    public void delete() {
        if (image.isLoaded())
            image.delete();
    }

    public boolean isLoaded() {
        return image.isLoaded();
    }

    @Builder(setterPrefix = "set")
    protected static GuiImage $build(@Nonnull CachedImage image, int width, int height, int extraYOffset, int zLevel, boolean packHeight, OnMouseClickListener onMouseClickListener) {
        Dimension modLogoDims = image.getDimensions();
        if (!image.isLoaded())
            return new GuiImage(width, height, extraYOffset, zLevel, 0, 0, 0, 0, image, true, onMouseClickListener);
        if (width != 0 && height != 0) {
            double scaleX = modLogoDims.width / (double) width;
            double scaleY = modLogoDims.height / (double) height;
            double scale = 1D;
            if (scaleX > 1 || scaleY > 1)
                scale = 1D / Math.max(scaleX, scaleY);
            double contentWidth = modLogoDims.width * scale;
            double contentHeight = modLogoDims.height * scale;
            double contentX = contentWidth < width ? (width - contentWidth) / 2D : 0D;
            if (packHeight && height > contentHeight)
                height = (int) contentHeight;
            double contentY = contentHeight < height ? (height - contentHeight) / 2D : 0D;
            return new GuiImage(width, height, extraYOffset, zLevel, contentWidth, contentHeight, contentX, contentY, image, packHeight, onMouseClickListener);
        } else {
            return new GuiImage(width, height, extraYOffset, zLevel, modLogoDims.width, modLogoDims.height, 0, 0, image, true, onMouseClickListener);
        }
    }

    @Override
    public void drawDebugLines(int xBegin, int yOffset, boolean hovered, float floatTicks) {
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
        GL11.glVertex3d(xBegin, yOffset + getContentHeight() - extraYOffset, zLevel);
        GL11.glVertex3d(xBegin + width, yOffset + getContentHeight() - extraYOffset, zLevel);
        GL11.glEnd();


        GL11.glLineWidth(1f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        if (hovered)
            GL11.glColor4f(1f, 1f, 0f, 1f);
        else
            GL11.glColor4f(1f, 0f, 0f, 1f);
        GL11.glVertex3d(xBegin + contentX, yOffset + contentY, zLevel);
        GL11.glVertex3d(xBegin + contentX + contentWidth, yOffset + contentY, zLevel);
        GL11.glVertex3d(xBegin + contentX + contentWidth, yOffset + contentY + contentHeight, zLevel);
        GL11.glVertex3d(xBegin + contentX, yOffset + contentY + contentHeight, zLevel);
        GL11.glEnd();

        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    @SuppressWarnings("unused")
    public static class GuiImageBuilder {
        public GuiImageBuilder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public GuiImageBuilder setImage(@Nonnull CachedImage image) {
            this.image = image;
            if (image.isLoaded()) {
                width = image.getDimensions().width;
                height = image.getDimensions().height;
            }
            return this;
        }

        public GuiImageBuilder setImage(@Nonnull ResourcePath path) {
            return setImage(new CachedImage(path));
        }

        public GuiImageBuilder setImage(@Nonnull ResourceLocation location) {
            return setImage(new CachedImage(ResourcePath.of(location)));
        }
    }
}
