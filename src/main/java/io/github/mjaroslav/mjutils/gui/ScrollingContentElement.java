package io.github.mjaroslav.mjutils.gui;

import io.github.mjaroslav.sharedjava.tuple.Triplet;

import javax.annotation.Nullable;

public interface ScrollingContentElement {
    void drawScreen(int xBegin, int yOffset, boolean hovered, float floatTicks);

    void drawDebugLines(int xBegin, int yOffset, boolean hovered, float floatTicks);

    int getContentHeight();

    Triplet<Boolean, Integer, Integer> isHovered(GuiContentScrollingPane parent, int xBegin, int yOffset, int mouseX, int mouseY);

    @Nullable
    OnMouseClickListener getOnMouseClickListener();

    interface OnMouseClickListener {
        void listen(ScrollingContentElement element, int x, int y);
    }
}
