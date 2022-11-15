package io.github.mjaroslav.mjutils.gui;

import io.github.mjaroslav.mjutils.util.game.client.UtilsGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public abstract class GuiScrollingPane {
    protected final GuiScreen parent;
    protected final int x, y, width, height;
    protected final float scrollStep;

    protected final int top, bottom, left, right;

    protected final int scrollBarXStart;
    protected final int scrollBarXEnd;

    protected float scrollPosition;

    protected int scrollDownButtonId, scrollUpButtonId;

    protected float initialMouseClickY = -2f;
    protected float scrollFactor;

    protected int mouseX, mouseY;

    protected boolean mouseLocked;

    protected long lastClickTime = 0L;

    protected boolean visible = true;

    public GuiScrollingPane(GuiScreen parent, int x, int y, int width, int height, float scrollStep) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scrollStep = scrollStep;
        top = y;
        bottom = y + height;
        left = x;
        right = x + width;
        scrollBarXStart = x + width - 6;
        scrollBarXEnd = scrollBarXStart + 6;
    }

    protected void applyScrollLimits() {
        int showedPart = getContentHeight() - (height - 4);
        if (showedPart < 0)
            showedPart /= 2;
        if (scrollPosition < 0)
            scrollPosition = 0;
        if (scrollPosition > showedPart)
            scrollPosition = showedPart;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        onVisibilityChanged(visible);
    }

    public void init() {}

    public void onClose() {}

    public void onVisibilityChanged(boolean newValue) {
    }

    public boolean isVisible() {
        return visible;
    }

    public void resetScrolling() {
        scrollPosition = 0;
    }

    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == scrollUpButtonId) {
                scrollPosition -= scrollStep;
                initialMouseClickY = -2F;
                applyScrollLimits();
            } else if (button.id == scrollDownButtonId) {
                scrollPosition += scrollStep;
                initialMouseClickY = -2F;
                applyScrollLimits();
            }
        }
    }

    public void registerScrollButtons(@SuppressWarnings("rawtypes") List buttonList, int scrollUpButtonId, int scrollDownButtonId) {
        this.scrollUpButtonId = scrollUpButtonId;
        this.scrollDownButtonId = scrollDownButtonId;
    }

    public boolean isMouseOver() {
        return mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom;
    }

    public void onMouseClicked(int x, int y, boolean isDrugged) {
    }

    protected void handleInputs(float floatTicks) {
        if (Mouse.isButtonDown(0)) {
            if (isMouseOver() || mouseLocked) {
                mouseLocked = true;

                onMouseClicked(mouseX, mouseY - top + (int) scrollPosition - 4, System.currentTimeMillis() - lastClickTime < 200L);
                lastClickTime = System.currentTimeMillis();

                if (initialMouseClickY == -1.0F)
                    if (mouseY >= top && mouseY <= bottom) {
                        if (mouseX >= scrollBarXStart && mouseX <= scrollBarXEnd) {
                            scrollFactor = -1.0F;
                            int heightDiff = getContentHeight() - (height - 4);
                            if (heightDiff < 1)
                                heightDiff = 1;
                            int scrollBarHeight = (int) (height * height / (float) getContentHeight());
                            if (scrollBarHeight < 32)
                                scrollBarHeight = 32;
                            if (scrollBarHeight > height - 8)
                                scrollBarHeight = height - 8;
                            scrollFactor /= (float) (height - scrollBarHeight) / (float) heightDiff;
                        } else
                            scrollFactor = 1F;
                        initialMouseClickY = mouseY;
                    } else
                        initialMouseClickY = -2F;
                else if (this.initialMouseClickY >= 0F) {
                    scrollPosition -= (mouseY - initialMouseClickY) * scrollFactor;
                    initialMouseClickY = mouseY;
                }
            }
        } else {
            mouseLocked = false;
            if (isMouseOver()) {
                while (Mouse.next()) {
                    int direction = Mouse.getEventDWheel();
                    if (direction != 0) {
                        if (direction > 0)
                            direction = -1;
                        else
                            direction = 1;
                        scrollPosition += direction * scrollStep;
                    }
                }
                initialMouseClickY = -1F;
            }
        }
    }

    public void drawBackground(float floatTicks) {
        Tessellator tess = Tessellator.instance;
        if (Minecraft.getMinecraft().theWorld != null)
            UtilsGUI.drawGradientRect(left, top, right, bottom, 0xC0101010, 0xD0101010);
        else {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_FOG);
            Minecraft.getMinecraft().renderEngine.bindTexture(Gui.optionsBackground);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            float scale = 32F;
            tess.startDrawingQuads();
            tess.setColorOpaque_I(2105376);
            tess.addVertexWithUV(left, bottom, 0D, left / scale, (bottom + scrollPosition) / scale);
            tess.addVertexWithUV(right, bottom, 0.0D, (right / scale), (bottom + scrollPosition) / scale);
            tess.addVertexWithUV(right, top, 0D, right / scale, (top + scrollPosition) / scale);
            tess.addVertexWithUV(left, top, 0D, left / scale, (top + scrollPosition) / scale);
            tess.draw();
        }
    }

    public void drawExtra(float floatTicks) {
        Tessellator tess = Tessellator.instance;
        byte scrollBarWidth = 4;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tess.startDrawingQuads();
        tess.setColorRGBA_I(0x000000, 0);
        tess.addVertexWithUV(left, top + scrollBarWidth, 0D, 0D, 1D);
        tess.addVertexWithUV(right, top + scrollBarWidth, 0D, 1D, 1D);
        tess.setColorRGBA_I(0x000000, 255);
        tess.addVertexWithUV(right, top, 0D, 1D, 0D);
        tess.addVertexWithUV(left, top, 0D, 0D, 0D);
        tess.draw();
        tess.startDrawingQuads();
        tess.setColorRGBA_I(0x000000, 255);
        tess.addVertexWithUV(left, bottom, 0D, 0D, 1D);
        tess.addVertexWithUV(right, bottom, 0D, 1D, 1D);
        tess.setColorRGBA_I(0x000000, 0);
        tess.addVertexWithUV(right, bottom - scrollBarWidth, 0D, 1D, 0D);
        tess.addVertexWithUV(left, bottom - scrollBarWidth, 0D, 0D, 0D);
        tess.draw();

        int heightDiff = getContentHeight() - (height - 4);

        if (heightDiff > 0) {
            int scrollBarHeight = height * height / getContentHeight();

            if (scrollBarHeight < 32)
                scrollBarHeight = 32;

            if (scrollBarHeight > height - 8)
                scrollBarHeight = height - 8;

            int scrollBarYPos = (int) scrollPosition * (height - scrollBarHeight) / heightDiff + top;

            if (scrollBarYPos < top)
                scrollBarYPos = top;

            tess.startDrawingQuads();
            tess.setColorRGBA_I(0x000000, 255);
            tess.addVertexWithUV(scrollBarXStart, bottom, 0D, 0D, 1D);
            tess.addVertexWithUV(scrollBarXEnd, bottom, 0D, 1D, 1D);
            tess.addVertexWithUV(scrollBarXEnd, top, 0D, 1D, 0D);
            tess.addVertexWithUV(scrollBarXStart, top, 0D, 0D, 0D);
            tess.draw();
            tess.startDrawingQuads();
            tess.setColorRGBA_I(0x808080, 255);
            tess.addVertexWithUV(scrollBarXStart, scrollBarYPos + scrollBarHeight, 0D, 0D, 1D);
            tess.addVertexWithUV(scrollBarXEnd, scrollBarYPos + scrollBarHeight, 0D, 1D, 1D);
            tess.addVertexWithUV(scrollBarXEnd, scrollBarYPos, 0D, 1D, 0D);
            tess.addVertexWithUV(scrollBarXStart, scrollBarYPos, 0D, 0D, 0D);
            tess.draw();
            tess.startDrawingQuads();
            tess.setColorRGBA_I(0xC0C0C0, 255);
            tess.addVertexWithUV(scrollBarXStart, scrollBarYPos + scrollBarHeight - 1, 0D, 0D, 1D);
            tess.addVertexWithUV(scrollBarXEnd - 1, scrollBarYPos + scrollBarHeight - 1, 0D, 1D, 1D);
            tess.addVertexWithUV(scrollBarXEnd - 1, scrollBarYPos, 0D, 1D, 0D);
            tess.addVertexWithUV(scrollBarXStart, scrollBarYPos, 0D, 0D, 0D);
            tess.draw();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public abstract int getContentHeight();

    public void drawScreen(int mouseX, int mouseY, float floatTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if (!visible)
            return;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        UtilsGUI.glScissor(x, y, width, height);

        handleInputs(floatTicks);
        applyScrollLimits();

        drawBackground(floatTicks);

        drawContent(x, y + 4 - (int) scrollPosition, floatTicks);

        drawExtra(floatTicks);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public abstract void drawContent(int beginX, int shiftY, float floatTicks);
}
