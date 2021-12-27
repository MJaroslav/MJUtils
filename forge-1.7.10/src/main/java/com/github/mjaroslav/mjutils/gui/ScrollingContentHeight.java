package com.github.mjaroslav.mjutils.gui;

public interface ScrollingContentHeight {
    void drawScreen(int xBegin, int yOffset, float floatTicks);

    void drawDebugLines(int xBegin, int yOffset, float floatTicks);

    int getContentHeight();
}
