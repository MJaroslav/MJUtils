package com.github.mjaroslav.mjutils.mod.client.gui.replace.modlist;

import com.github.mjaroslav.mjutils.gui.GuiContentScrollingPane;
import com.github.mjaroslav.mjutils.gui.GuiImage;
import com.github.mjaroslav.mjutils.gui.GuiScrollingPaneList;
import com.github.mjaroslav.mjutils.gui.GuiText;
import com.github.mjaroslav.mjutils.util.game.UtilsMods;
import com.github.mjaroslav.mjutils.util.game.client.UtilsGUI;
import com.github.mjaroslav.mjutils.util.game.client.UtilsTextures;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import lombok.val;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiModListReplacer extends GuiScreen {
    protected GuiScreen parentScreen;
    protected GuiSlotModListReplacer modList;
    protected GuiModInfoPane modInfo;

    protected int selected = -1;
    protected ModContainer selectedMod;

    protected int modListWidth;
    protected final ArrayList<ModContainer> mods;

    protected GuiButton configModButton;
    protected GuiButton disableModButton;
    protected GuiButton urlModButton;

    public GuiModListReplacer(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
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
        mods.addAll(UtilsMods.getDisabledMods());
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
                    mc.displayGuiScreen(parentScreen);
                    return;
                case 20: // Configure button
                    mc.displayGuiScreen(UtilsGUI.createModGUIConfig(selectedMod.getModId(), this));
                    return;
                case 21: // Disable/Enable button
                    if (UtilsMods.toggleModState(selectedMod).isEnabled())
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

    protected void updateElementsAttributes() {
        if (selectedMod != null) {
            if (UtilsMods.isModEnabled(selectedMod))
                disableModButton.displayString = I18n.format("gui.modlist.button.disable");
            else
                disableModButton.displayString = I18n.format("gui.modlist.button.enable");
            disableModButton.visible = true;
            urlModButton.enabled = !StringUtils.isEmpty(selectedMod.getMetadata().url);
            urlModButton.visible = true;
            configModButton.visible = true;
            configModButton.enabled = UtilsGUI.isModHaveGUIConfig(selectedMod.getModId());
            disableModButton.enabled = UtilsMods.canChangeState(selectedMod);
            modInfo.setVisible(true);
        } else {
            disableModButton.visible = false;
            urlModButton.visible = false;
            configModButton.visible = false;
            modInfo.setVisible(false);
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
                drawHoveringText(Lists.newArrayList(I18n.format("gui.modlist.text.internal")), mouseX, mouseY, fontRendererObj);

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
        if (index >= 0 && index <= mods.size()) {
            selectedMod = mods.get(selected);
            modInfo.init();
        } else
            selectedMod = null;
        modInfo.resetScrolling();
        updateElementsAttributes();
    }

    public boolean modIndexSelected(int index) {
        return index == selected;
    }

    @Override
    public void onGuiClosed() {
        modInfo.onClose();
        modList.onClose();
    }

    public static class GuiModInfoPane extends GuiContentScrollingPane {
        protected GuiModListReplacer parent;

        protected GuiImage modLogo;

        protected final List<GuiImage> modScreenshots = new ArrayList<>();

        public GuiModInfoPane(GuiModListReplacer parent, int x, int y, int width, int height, float scrollStep) {
            super(parent, x, y, width, height, scrollStep);
            this.parent = parent;
            init();
        }

        @Override
        public void onClose() {
            modScreenshots.forEach(GuiImage::delete);
            modScreenshots.clear();
            if (modLogo != null) {
                modLogo.delete();
                modLogo = null;
            }
        }

        @Override
        public void init() {
            if (modLogo != null) {
                modLogo.delete();
                modLogo = null;
            }
            modScreenshots.forEach(GuiImage::delete);
            modScreenshots.clear();
            contentList.clear();
            ModContainer mod = parent.selectedMod;
            if (mod == null)
                return;
            ModMetadata meta = mod.getMetadata();
            if (!meta.autogenerated) {
                if (modLogo == null) {
                    if (!StringUtils.isEmpty(meta.logoFile)) {
                        modLogo = GuiImage.builder().setImage(ResourcePath.of(mod.getModId(), meta.logoFile))
                                .setSize(width - 6, 65).setExtraYOffset(10).setPackHeight(true)
                                .setOnMouseClickListener((element, x, y) -> {
                                    System.out.println(x + " " + y);
                                }).build();
                        if (!modLogo.isLoaded()) {
                            modLogo.delete();
                            modLogo = null;
                        } else contentList.add(modLogo);
                    }
                }
                GuiText.GuiTextBuilder text = GuiText.builder().setSplitText(true).setWidth(width - 6).setCenterString(true).setRenderer(parent.fontRendererObj);
                contentList.add(text.set(meta.name).setColor(0xFFFFFF).setExtraYOffset(10).build());
                text.setColor(0xD7EDEA).setExtraYOffset(0).setXOffset(10).setCenterString(false);
                contentList.add(text.setTranslated("gui.modlist.text.version", mod.getDisplayVersion(), mod.getVersion()).build());
                contentList.add(text.setTranslated("gui.modlist.text.state", mod.getModId(), Loader.instance().getModState(mod)).build());
                if (!meta.credits.isEmpty())
                    contentList.add(text.setTranslated("gui.modlist.text.credits", meta.credits).build());
                contentList.add(text.setTranslated("gui.modlist.text.authors", mod.getMetadata().getAuthorList()).build());
                contentList.add(text.setTranslated("gui.modlist.text.url", meta.url).build());
                if (meta.childMods.isEmpty())
                    text.setTranslated("gui.modlist.text.nochild");
                else
                    text.setTranslated("gui.modlist.text.child", meta.childMods);
                contentList.add(text.setExtraYOffset(10).build());
                contentList.add(text.set(meta.description).setColor(0xDDDDDD).build());
                if (meta.screenshots != null && meta.screenshots.length > 0) {
                    contentList.add(text.setTranslated("gui.modlist.text.screens").setCenterString(true).setColor(0xFFFFFF).build());
                    for (int i = 0; i < meta.screenshots.length; i++) {
                        if (!StringUtils.isEmpty(meta.screenshots[i])) {
                            GuiImage screenshot = GuiImage.builder().setImage(ResourcePath.of(mod.getModId(), meta.screenshots[i])).setExtraYOffset(10).setSize(width - 6, 135).build();
                            if (screenshot.isLoaded()) {
                                modScreenshots.add(screenshot);
                                contentList.add(screenshot);
                            } else screenshot.delete();
                        }
                    }
                }
            } else {
                GuiText.GuiTextBuilder text = GuiText.builder().setSplitText(true).setWidth(width - 6).setRenderer(parent.fontRendererObj).setCenterString(true);
                contentList.add(text.setColor(0xFFFFFF).set(mod.getName()).build());
                contentList.add(text.setTranslated("gui.modlist.text.versionmin", mod.getVersion()).build());
                contentList.add(text.setTranslated("gui.modlist.text.statemin", Loader.instance().getModState(mod)).build());
                contentList.add(text.setTranslated("gui.modlist.text.noinfo").build());
                text.setColor(0xDDDDDD);
                contentList.add(text.setTranslated("gui.modlist.text.askinfo").build());
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
        public void onClose() {
            val tm = parent.mc.getTextureManager();
            cachedIcons.values().stream().filter(path -> path != UtilsTextures.UNKNOWN_PACK).forEach(tm::deleteTexture);
        }

        @Override
        public void drawEntry(int listIndex, int beginX, int var2, int offsetY, int var4, float floatTicks) {
            ModContainer mc = mods.get(listIndex);
            if (mc == null)
                return;
            Tessellator var5 = Tessellator.instance;
            val state = UtilsMods.getModState(mc);
            if (!state.isScheduled()) {
                if (state.isDisabled()) {
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getName(), width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 2, 0xFF2222);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getDisplayVersion(), width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 12, 0xFF2222);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth("DISABLED", width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 22, 0xFF2222);
                } else {
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getName(), width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 2, 0xFFFFFF);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getDisplayVersion(), width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 12, 0xCCCCCC);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getMetadata() != null ? mc.getMetadata().getChildModCountString() : "Metadata not found", width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 22, 0xCCCCCC);
                }
            } else {
                if (state.isDisabled()) {
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getName(), width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 2, 0xFFFF55);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getDisplayVersion(), width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 12, 0xFFFF55);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth("DISABLED", width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 22, 0xFFFF55);
                } else {
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getName(), width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 2, 0xFFFF55);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getDisplayVersion(), width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 12, 0xDDD605);
                    parent.fontRendererObj.drawString(parent.fontRendererObj.trimStringToWidth(mc.getMetadata() != null ? mc.getMetadata().getChildModCountString() : "Metadata not found", width - 10 - 30 - 3), beginX + 3 + 30 + 3, offsetY + 22, 0xDDD605);
                }
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            TextureManager tm = parent.mc.getTextureManager();
            try {
                if (cachedIcons.get(mc.getModId()) == null) {
                    BufferedImage logo = null;
                    val stream = UtilsMods.getResourceFromModIgnored(mc, "icon.png", false);
                    if (stream != null)
                        logo = ImageIO.read(stream);
                    if (logo != null)
                        cachedIcons.put(mc.getModId(), tm.getDynamicTextureLocation("icon_" + mc.getModId(), new DynamicTexture(logo)));
                    else cachedIcons.put(mc.getModId(), UtilsTextures.UNKNOWN_PACK);
                }
                if (cachedIcons.get(mc.getModId()) != null) {
                    parent.mc.renderEngine.bindTexture(cachedIcons.get(mc.getModId()));
                    var5.startDrawingQuads();
                    var5.addVertexWithUV(beginX + 2, offsetY + 30, parent.zLevel, 0, 1);
                    var5.addVertexWithUV(beginX + 30 + 2, offsetY + 30, parent.zLevel, 1, 1);
                    var5.addVertexWithUV(beginX + 30 + 2, offsetY, parent.zLevel, 1, 0);
                    var5.addVertexWithUV(beginX + 2, offsetY, parent.zLevel, 0, 0);
                    var5.draw();
                }
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }

}
