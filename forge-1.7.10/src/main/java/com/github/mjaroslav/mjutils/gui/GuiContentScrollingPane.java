package com.github.mjaroslav.mjutils.gui;

import com.github.mjaroslav.mjutils.mod.lib.CategoryRoot;
import com.github.mjaroslav.mjutils.object.Pair;
import lombok.val;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiContentScrollingPane extends GuiScrollingPane {
    protected final List<ScrollingContentElement> contentList = new ArrayList<>();

    public GuiContentScrollingPane(GuiScreen parent, int x, int y, int width, int height, float scrollStep) {
        super(parent, x, y, width, height, scrollStep);
    }

    protected abstract void initContent();

    @Override
    public int getContentHeight() {
        return contentList.stream().mapToInt(ScrollingContentElement::getContentHeight).sum();
    }

    @Override
    public void onMouseClicked(int x, int y, boolean isDrugged) {
        super.onMouseClicked(x, y, isDrugged);
        if(isDrugged)
            return;
        ScrollingContentElement selected = null;
        Pair<Boolean, Pair<Integer, Integer>> hoveredElement = new Pair<>();
        for (ScrollingContentElement element : contentList) {
            hoveredElement = element.isHovered(this, this.x, this.y + 4 - (int) scrollPosition, x, y);
            if (hoveredElement.getA()) {
                selected = element;
                break;
            }
        }
        if (selected != null) {
            ScrollingContentElement.OnMouseClickListener listener = selected.getOnMouseClickListener();
            if (listener != null)
                listener.listen(selected, hoveredElement.getB().getA(),
                        hoveredElement.getB().getB());
        }

    }

    protected boolean isPaneHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void drawContent(int beginX, int shiftY, float floatTicks) {
        int y = shiftY;
        GL11.glEnable(GL11.GL_BLEND);
        for (ScrollingContentElement content : contentList) {
            val contentHovered = content.isHovered(this, beginX, shiftY, mouseX, mouseY);
            boolean hovered = isPaneHovered(mouseX, mouseY) && contentHovered.getA();
            content.drawScreen(beginX, y, hovered, floatTicks);
            if (CategoryRoot.CategoryDebug.renderDebugLinesInScrollingPane)
                content.drawDebugLines(beginX, y, hovered, floatTicks);
            y += content.getContentHeight();
        }

        GL11.glDisable(GL11.GL_BLEND);
    }
}
