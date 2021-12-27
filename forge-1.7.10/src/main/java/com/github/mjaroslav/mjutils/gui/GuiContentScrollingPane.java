package com.github.mjaroslav.mjutils.gui;

import com.github.mjaroslav.mjutils.mod.lib.CategoryRoot;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiContentScrollingPane extends GuiScrollingPane {
    protected final List<ScrollingContentHeight> contentList = new ArrayList<>();

    public GuiContentScrollingPane(GuiScreen parent, int x, int y, int width, int height, float scrollStep) {
        super(parent, x, y, width, height, scrollStep);
    }

    protected abstract void initContent();

    @Override
    public int getContentHeight() {
        return contentList.stream().mapToInt(ScrollingContentHeight::getContentHeight).sum();
    }

    @Override
    public void drawContent(int beginX, int shiftY, float floatTicks) {
        int y = shiftY;
        GL11.glEnable(GL11.GL_BLEND);
        for (ScrollingContentHeight content : contentList) {
            content.drawScreen(beginX, y, floatTicks);
            if (CategoryRoot.CategoryDebug.renderDebugLinesInScrollingPane)
                content.drawDebugLines(beginX, y, floatTicks);
            y += content.getContentHeight();
        }

        GL11.glDisable(GL11.GL_BLEND);
    }
}
