package com.github.mjaroslav.mjutils.mod.client.gui.replace.modlist;

import com.github.mjaroslav.mjutils.gui.GuiScrollingPane;
import com.github.mjaroslav.mjutils.gui.GuiScrollingPaneList;
import com.github.mjaroslav.mjutils.object.game.client.CachedImage;
import com.github.mjaroslav.mjutils.util.game.UtilsMods;
import com.github.mjaroslav.mjutils.util.game.client.UtilsGUI;
import com.github.mjaroslav.mjutils.util.game.client.UtilsTextures;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiModListReplacer extends GuiScreen {
    protected GuiSlotModListReplacer modList;
    protected GuiModInfoPane modInfo;

    protected int selected = -1;
    protected ModContainer selectedMod;

    protected int modListWidth;
    protected final ArrayList<ModContainer> mods;

    protected GuiButton configModButton;
    protected GuiButton disableModButton;
    protected GuiButton urlModButton;

    public GuiModListReplacer() {
        mods = new ArrayList<>();
        FMLClientHandler.instance().addSpecialModEntries(mods);
        for (ModContainer mod : Loader.instance().getModList()) {
            if (mod.getMetadata() != null && mod.getMetadata().parentMod == null && !Strings.isNullOrEmpty(mod.getMetadata().parent)) {
                String parentMod = mod.getMetadata().parent;
                ModContainer parentContainer = Loader.instance().getIndexedModList().get(parentMod);
                if (parentContainer != null) {
                    mod.getMetadata().parentMod = parentContainer;
                    parentContainer.getMetadata().childMods.add(mod);
                    continue;
                }
            } else if (mod.getMetadata() != null && mod.getMetadata().parentMod != null)
                continue;
            mods.add(mod);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        modListWidth = (int) (width * 0.3f);

        buttonList.add(new GuiButton(6, width - modListWidth - 10, height - 38, modListWidth, 20, I18n.format("gui.done")));

        configModButton = new GuiButton(20, 10, height - 60, modListWidth, 20, I18n.format("gui.modlist.button.config"));
        disableModButton = new GuiButton(21, 10, height - 38, modListWidth, 20, "");
        urlModButton = new GuiButton(22, width - modListWidth - 10, height - 60, modListWidth, 20, I18n.format("gui.modlist.button.openurl"));

        modList = new GuiSlotModListReplacer(this, mods, modListWidth);
        modInfo = new GuiModInfoPane(this, modListWidth + 15, 32, width - modListWidth - 25, height - 94, 30);

        buttonList.add(configModButton);
        buttonList.add(disableModButton);
        buttonList.add(urlModButton);

        updateElementsAttributes();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        modInfo.actionPerformed(button);
        modList.actionPerformed(button);
        if (button.enabled) {
            switch (button.id) {
                case 6: // Done button
                    mc.displayGuiScreen(new GuiMainMenu());
                    return;
                case 20: // Configure button
                    mc.displayGuiScreen(UtilsGUI.createModGUIConfig(selectedMod.getModId(), this));
                    return;
                case 21: // Disable/Enable button
                    if (UtilsMods.toggleSavedModState(selectedMod.getModId()))
                        disableModButton.displayString = I18n.format("gui.modlist.button.disable");
                    else
                        disableModButton.displayString = I18n.format("gui.modlist.button.enable");
                    return;
                case 22: // Open URL
                    UtilsGUI.openURL(this, selectedMod.getMetadata().url);
                    return;
            }
        }
        super.actionPerformed(button);
    }

    public int drawLine(String line, int offset, int shifty) {
        fontRendererObj.drawString(line, offset, shifty, 0xd7edea);
        return shifty + 10;
    }

    protected void updateElementsAttributes() {
        if (selectedMod != null) {
            if (UtilsMods.getSavedModState(selectedMod.getModId()))
                disableModButton.displayString = I18n.format("gui.modlist.button.disable");
            else
                disableModButton.displayString = I18n.format("gui.modlist.button.enable");
            disableModButton.visible = true;
            urlModButton.enabled = !StringUtils.isEmpty(selectedMod.getMetadata().url);
            urlModButton.visible = true;
            configModButton.visible = true;
            configModButton.enabled = UtilsGUI.isModHaveGUIConfig(selectedMod.getModId());
            disableModButton.enabled = UtilsMods.canDisableMod(selectedMod.getModId());
            modInfo.setVisible(true);
        } else {
            disableModButton.visible = false;
            urlModButton.visible = false;
            configModButton.visible = false;
            modInfo.setVisible(false);
            modInfo.clearCaches();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float floatTicks) {
        drawDefaultBackground();
        modList.drawScreen(mouseX, mouseY, floatTicks);
        modInfo.drawScreen(mouseX, mouseY, floatTicks);
        drawCenteredString(fontRendererObj, I18n.format("gui.modlist.text.modlist"), width / 2, 16, 0xFFFFFF);

        updateElementsAttributes();

        super.drawScreen(mouseX, mouseY, floatTicks);

        if (selectedMod != null) {
            int hoverState = UtilsGUI.getButtonHoverState(mouseX, mouseY, disableModButton, false);
            if (hoverState == 2 && !disableModButton.enabled)
                drawHoveringText(Lists.newArrayList(I18n.format("gui.modlist.text.iscoremod")), mouseX, mouseY, fontRendererObj);

            hoverState = UtilsGUI.getButtonHoverState(mouseX, mouseY, configModButton, false);
            if (hoverState == 2 && !configModButton.enabled)
                drawHoveringText(Lists.newArrayList(I18n.format("gui.modlist.text.noguiconfig")), mouseX, mouseY, fontRendererObj);

            hoverState = UtilsGUI.getButtonHoverState(mouseX, mouseY, urlModButton, false);
            if (hoverState == 2 && !urlModButton.enabled)
                drawHoveringText(Lists.newArrayList(I18n.format("gui.modlist.text.nourl")), mouseX, mouseY, fontRendererObj);
        }
    }

    public void selectModIndex(int index) {
        selected = index;
        if (index >= 0 && index <= mods.size())
            selectedMod = mods.get(selected);
        else
            selectedMod = null;
        modInfo.clearCaches();
        modInfo.resetScrolling();
        updateElementsAttributes();
    }

    public boolean modIndexSelected(int index) {
        return index == selected;
    }

    public static class GuiModInfoPane extends GuiScrollingPane {
        protected GuiModListReplacer parent;

        protected CachedImage modLogo;

        protected final List<CachedImage> modScreenshots = new ArrayList<>();

        public GuiModInfoPane(GuiModListReplacer parent, int x, int y, int width, int height, float scrollStep) {
            super(parent, x, y, width, height, scrollStep);
            this.parent = parent;
        }

        public void clearCaches() {
            if (modLogo != null) {
                modLogo.delete();
                modLogo = null;
            }
            modScreenshots.forEach(CachedImage::delete);
            modScreenshots.clear();
        }

        @Override
        public int getContentHeight() {
            ModContainer mod = parent.selectedMod;
            String desc = mod.getMetadata().description;
            int offset = parent.modListWidth + 20;
            int rightSide = parent.width - offset - 20;
            return (modLogo != null ? 70 : 0) + 12 + 60 +
                    (StringUtils.isEmpty(mod.getMetadata().credits) ? 0 : 10) +
                    10 * (StringUtils.isEmpty(desc) ? 0 :
                            parent.fontRendererObj.listFormattedStringToWidth(desc, rightSide).size())
                    + 12 + 10 + 140 * modScreenshots.size();
        }

        protected void cacheImages(ModContainer mod) {
            ModMetadata meta = mod.getMetadata();
            if (modLogo == null) {
                if (!StringUtils.isEmpty(meta.logoFile)) {
                    modLogo = new CachedImage("modlogo", meta.logoFile, mod.getModId());
                    if (!modLogo.isLoaded())
                        modLogo = null;
                }
            }
            if (modScreenshots.size() == 0 && meta.screenshots != null)
                for (int i = 0; i < meta.screenshots.length; i++) {
                    if (!StringUtils.isEmpty(meta.screenshots[i])) {
                        CachedImage screenshot = new CachedImage("modscreenshot" + i, meta.screenshots[i], mod.getModId());
                        if (screenshot.isLoaded())
                            modScreenshots.add(screenshot);
                    }
                }
        }

        @Override
        public void drawContent(int shiftY, float floatTicks) {
            int shifty = shiftY;
            ModContainer selectedMod = parent.selectedMod;
            if (selectedMod != null) {
                int offset = parent.modListWidth + 20;

                GL11.glEnable(GL11.GL_BLEND);
                if (!selectedMod.getMetadata().autogenerated) {
                    cacheImages(selectedMod);
                    GL11.glColor4f(1F, 1F, 1F, 1F);
                    if (modLogo != null) {
                        parent.mc.renderEngine.bindTexture(modLogo.getLocation());
                        Dimension modLogoDims = modLogo.getDimensions();
                        double scaleX = modLogoDims.width / 200.0;
                        double scaleY = modLogoDims.height / 65.0;
                        double scale = 1.0;
                        if (scaleX > 1 || scaleY > 1)
                            scale = 1.0 / Math.max(scaleX, scaleY);
                        modLogoDims.width *= scale;
                        modLogoDims.height *= scale;
                        Tessellator tess = Tessellator.instance;
                        tess.startDrawingQuads();
                        tess.addVertexWithUV(offset, modLogoDims.height + shifty, parent.zLevel, 0, 1);
                        tess.addVertexWithUV(offset + modLogoDims.width, modLogoDims.height + shifty, parent.zLevel, 1, 1);
                        tess.addVertexWithUV(offset + modLogoDims.width, shifty, parent.zLevel, 1, 0);
                        tess.addVertexWithUV(offset, shifty, parent.zLevel, 0, 0);

                        tess.draw();

                        shifty += 70;
                    }
                    parent.fontRendererObj.drawStringWithShadow(selectedMod.getMetadata().name, offset, shifty, 0xFFFFFF);
                    shifty += 12;

                    shifty = parent.drawLine(I18n.format("gui.modlist.text.version", selectedMod.getDisplayVersion(), selectedMod.getVersion()), offset, shifty);
                    shifty = parent.drawLine(I18n.format("gui.modlist.text.state", selectedMod.getModId(), Loader.instance().getModState(selectedMod)), offset, shifty);
                    if (!selectedMod.getMetadata().credits.isEmpty())
                        shifty = parent.drawLine(I18n.format("gui.modlist.text.credits", selectedMod.getMetadata().credits), offset, shifty);
                    shifty = parent.drawLine(I18n.format("gui.modlist.text.authors", selectedMod.getMetadata().getAuthorList()), offset, shifty);
                    shifty = parent.drawLine(I18n.format("gui.modlist.text.url", selectedMod.getMetadata().url), offset, shifty);
                    shifty = parent.drawLine(selectedMod.getMetadata().childMods.isEmpty() ? I18n.format("gui.modlist.text.nochild") : I18n.format("gui.modlist.text.child", selectedMod.getMetadata().getChildModList()), offset, shifty);
                    int rightSide = parent.width - offset - 20;
                    if (rightSide > 20) {
                        parent.fontRendererObj.drawSplitString(selectedMod.getMetadata().description, offset, shifty + 10, rightSide, 0xDDDDDD);
                        shifty += parent.fontRendererObj.listFormattedStringToWidth(selectedMod.getMetadata().description, rightSide).size() * 10;
                        shifty += 10;
                    }
                    shifty += 12;
                    parent.fontRendererObj.drawStringWithShadow(I18n.format("gui.modlist.text.screens"), offset, shifty, 0xFFFFFF);
                    shifty += 10;
                    for (CachedImage screenshot : modScreenshots) {
                        parent.mc.renderEngine.bindTexture(screenshot.getLocation());
                        Dimension modLogoDims = screenshot.getDimensions();
                        double scaleX = modLogoDims.width / 500.0;
                        double scaleY = modLogoDims.height / 135.0;
                        double scale = 1.0;
                        if (scaleX > 1 || scaleY > 1)
                            scale = 1.0 / Math.max(scaleX, scaleY);
                        modLogoDims.width *= scale;
                        modLogoDims.height *= scale;
                        Tessellator tess = Tessellator.instance;
                        tess.startDrawingQuads();
                        tess.addVertexWithUV(offset, modLogoDims.height + shifty, parent.zLevel, 0, 1);
                        tess.addVertexWithUV(offset + modLogoDims.width, modLogoDims.height + shifty, parent.zLevel, 1, 1);
                        tess.addVertexWithUV(offset + modLogoDims.width, shifty, parent.zLevel, 1, 0);
                        tess.addVertexWithUV(offset, shifty, parent.zLevel, 0, 0);

                        tess.draw();

                        shifty += 140;
                    }
                } else {
                    offset = (parent.modListWidth + parent.width) / 2;
                    parent.drawCenteredString(parent.fontRendererObj, selectedMod.getName(), offset, shifty, 0xFFFFFF);
                    shifty += 10;
                    parent.drawCenteredString(parent.fontRendererObj, I18n.format("gui.modlist.text.versionmin", selectedMod.getVersion()), offset, shifty, 0xFFFFFF);
                    shifty += 10;
                    parent.drawCenteredString(parent.fontRendererObj, I18n.format("gui.modlist.text.statemin", Loader.instance().getModState(selectedMod)), offset, shifty, 0xFFFFFF);
                    shifty += 10;
                    parent.drawCenteredString(parent.fontRendererObj, I18n.format("gui.modlist.text.noinfo"), offset, shifty, 0xDDDDDD);
                    shifty += 10;
                    parent.drawCenteredString(parent.fontRendererObj, I18n.format("gui.modlist.text.askinfo"), offset, shifty, 0xDDDDDD);
                }
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
    }

    public static class GuiSlotModListReplacer extends GuiScrollingPaneList {
        protected GuiModListReplacer parent;
        protected final List<ModContainer> mods;
        private final Map<String, ResourceLocation> cachedIcons = new HashMap<>();

        public GuiSlotModListReplacer(GuiModListReplacer parent, List<ModContainer> mods, int listWidth) {
            super(parent, 34, 10, 32, listWidth, parent.height - 94, 32);
            this.parent = parent;
            this.mods = mods;
        }

        @Override
        public int getListSize() {
            return mods.size();
        }

        @Override
        public void onElementClicked(int index) {
            parent.selectModIndex(index);
        }

        @Override
        public boolean isSelected(int index) {
            return parent.modIndexSelected(index);
        }

        @Override
        public void drawEntry(int listIndex, int var2, int offsetY, int var4, float floatTicks) {
            ModContainer mc = mods.get(listIndex);
            Tessellator var5 = Tessellator.instance;
            boolean actual = UtilsMods.getActualModState(mc);
            boolean saved = UtilsMods.getSavedModState(mc.getModId());
            if (actual == saved) {
                if (!actual) {
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getName(), width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 2, 0xFF2222);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getDisplayVersion(), width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 12, 0xFF2222);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth("DISABLED", width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 22, 0xFF2222);
                } else {
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getName(), width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 2, 0xFFFFFF);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getDisplayVersion(), width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 12, 0xCCCCCC);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getMetadata() != null ? mc.getMetadata().getChildModCountString() : "Metadata not found", width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 22, 0xCCCCCC);
                }
            } else {
                if (!actual) {
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getName(), width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 2, 0xFFFF55);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getDisplayVersion(), width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 12, 0xFFFF55);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth("DISABLED", width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 22, 0xFFFF55);
                } else {
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getName(), width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 2, 0xFFFF55);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getDisplayVersion(), width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 12, 0xDDD605);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getMetadata() != null ? mc.getMetadata().getChildModCountString() : "Metadata not found", width - 10 - 30 - 3), x + 3 + 30 + 3, offsetY + 22, 0xDDD605);
                }
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            TextureManager tm = parent.mc.getTextureManager();
            try {
                if (cachedIcons.get(mc.getModId()) == null) {
                    BufferedImage logo = null;
                    InputStream logoResource = UtilsMods.getResourceFromModAsStream(mc.getModId(), "icon.png");
                    if (logoResource != null)
                        logo = ImageIO.read(logoResource);
                    if (logo != null) {
                        cachedIcons.put(mc.getModId(), tm.getDynamicTextureLocation("icon_" + mc.getModId(), new DynamicTexture(logo)));
                    } else cachedIcons.put(mc.getModId(), UtilsTextures.UNKNOWN_PACK);
                }
                if (cachedIcons.get(mc.getModId()) != null) {
                    parent.mc.renderEngine.bindTexture(cachedIcons.get(mc.getModId()));
                    var5.startDrawingQuads();
                    var5.addVertexWithUV(x + 2, offsetY + 30, parent.zLevel, 0, 1);
                    var5.addVertexWithUV(x + 30 + 2, offsetY + 30, parent.zLevel, 1, 1);
                    var5.addVertexWithUV(x + 30 + 2, offsetY, parent.zLevel, 1, 0);
                    var5.addVertexWithUV(x + 2, offsetY, parent.zLevel, 0, 0);
                    var5.draw();
                }
            } catch (IOException ignored) {
            }
        }
    }
}
