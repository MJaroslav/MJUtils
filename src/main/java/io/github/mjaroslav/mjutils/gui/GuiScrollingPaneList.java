package io.github.mjaroslav.mjutils.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public abstract class GuiScrollingPaneList extends GuiScrollingPane {
    protected final int entrySize;
    protected int selectedIndex = -1;

    protected final int boxLeft = left, boxRight = scrollBarXStart - 1;
    protected final int entrySizeWithOutBorders;

    public GuiScrollingPaneList(GuiScreen parent, int entrySize, int x, int y, int width, int height, float scrollStep) {
        super(parent, x, y, width, height, scrollStep);
        this.entrySize = entrySize;
        entrySizeWithOutBorders = entrySize - 4;
    }

    public abstract int getListSize();

    @Override
    public void onMouseClicked(int x, int y, boolean isDrugged) {
        int index = y / entrySize;
        if (index >= 0 && index < getListSize() && !isDrugged) {
            selectedIndex = index;
            onElementClicked(index);
        }
    }

    public void onElementClicked(int index) {
    }

    @Override
    public int getContentHeight() {
        return getListSize() * entrySize;
    }

    public boolean isSelected(int index) {
        return selectedIndex == index;
    }

    @Override
    public void drawContent(int beginX, int shiftY, float floatTicks) {
        Tessellator tess = Tessellator.instance;
        for (int index = 0; index < getListSize(); ++index) {
            int offsetY = shiftY + index * entrySize;
            if (offsetY <= bottom && offsetY + entrySizeWithOutBorders >= top) {
                if (isSelected(index)) {
                    GL11.glColor4f(1F, 1F, 1F, 1F);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tess.startDrawingQuads();
                    tess.setColorOpaque_I(8421504);
                    tess.addVertexWithUV(boxLeft, (offsetY + entrySizeWithOutBorders + 2), 0D, 0D, 1D);
                    tess.addVertexWithUV(boxRight, (offsetY + entrySizeWithOutBorders + 2), 0D, 1D, 1D);
                    tess.addVertexWithUV(boxRight, (offsetY - 2), 0D, 1D, 0D);
                    tess.addVertexWithUV(boxLeft, (offsetY - 2), 0D, 0D, 0D);
                    tess.setColorOpaque_I(0);
                    tess.addVertexWithUV((boxLeft + 1), (offsetY + entrySizeWithOutBorders + 1), 0D, 0D, 1D);
                    tess.addVertexWithUV((boxRight - 1), (offsetY + entrySizeWithOutBorders + 1), 0D, 1D, 1D);
                    tess.addVertexWithUV((boxRight - 1), (offsetY - 1), 0D, 1D, 0D);
                    tess.addVertexWithUV((boxLeft + 1), (offsetY - 1), 0D, 0D, 0D);
                    tess.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }
                drawEntry(index, x, right - 6 - 1, offsetY, entrySize - 4, floatTicks);
            }
        }
    }

    public abstract void drawEntry(int index, int beginX, int width, int offsetY, int maxY, float floatTicks);
}
