package com.github.mjaroslav.mjutils.mod.client.gui;

import com.github.mjaroslav.mjutils.configurator.PropertiesConfigurator;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import com.google.common.base.Strings;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.GuiScrollingList;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@SuppressWarnings("unchecked")
public class GuiModListReplacer extends GuiScreen {
    private final GuiScreen mainMenu;
    private GuiSlotModListReplacer modList;
    private int selected = -1;
    private ModContainer selectedMod;
    private int listWidth;
    private final ArrayList<ModContainer> mods;
    private GuiButton configModButton;
    private GuiButton disableModButton;
    private ResourceLocation cachedLogo;
    private Dimension cachedLogoDimensions;

    private final PropertiesConfigurator fmlModStateProps;

    public static final Set<String> blockedMods = new HashSet<>(Collections.singleton(ModInfo.modId));
    public static final Map<String, String> modIcons = new HashMap<>();

    private boolean getActualModState(ModContainer mc) {
        return Loader.instance().getModState(mc) != LoaderState.ModState.DISABLED;
    }

    private boolean getSavedModState(String modId) {
        return Boolean.parseBoolean(fmlModStateProps.getProperties().getProperty(modId, "true"));
    }

    private void setSavedModState(String modId, boolean value) {
        fmlModStateProps.getProperties().setProperty(modId, String.valueOf(value));
    }

    private boolean toggleSavedModState(String modId) {
        boolean value = !getSavedModState(modId);
        setSavedModState(modId, value);
        return value;
    }

    public GuiModListReplacer(GuiScreen mainMenu) {
        fmlModStateProps = new PropertiesConfigurator("fmlModState",
                ResourcePath.of("mjutils:configurators/fmlModState.properties"));
        fmlModStateProps.load();
        this.mainMenu = mainMenu;
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

    @Override
    public void initGui() {
        for (ModContainer mod : mods) {
            listWidth = Math.max(listWidth, getFontRenderer().getStringWidth(mod.getName()) + 10);
            listWidth = Math.max(listWidth, getFontRenderer().getStringWidth(mod.getVersion()) + 10);
        }
        listWidth = Math.min(listWidth + 30 + 3, 150 + 30);
        buttonList.add(new GuiButton(6, width / 2 - 75, height - 38, I18n.format("gui.done")));
        configModButton = new GuiButton(20, 10, height - 60, listWidth, 20, I18n.format("gui.modlist.button.config"));
        disableModButton = new GuiButton(21, 10, height - 38, listWidth, 20, "PLACEHOLDER");
        disableModButton.visible = false;
        buttonList.add(configModButton);
        buttonList.add(disableModButton);
        modList = new GuiSlotModListReplacer(this, mods, listWidth);
        modList.registerScrollButtons(buttonList, 7, 8);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            switch (button.id) {
                case 6: // Back button
                    mc.displayGuiScreen(mainMenu);
                    return;
                case 20: // Configure button
                    try {
                        IModGuiFactory guiFactory = FMLClientHandler.instance().getGuiFactoryFor(selectedMod);
                        GuiScreen newScreen = guiFactory.mainConfigGuiClass().getConstructor(GuiScreen.class).newInstance(this);
                        mc.displayGuiScreen(newScreen);
                    } catch (Exception e) {
                        FMLLog.log(Level.ERROR, e, "There was a critical issue trying to build the config GUI for %s", selectedMod.getModId());
                    }
                    return;
                case 21: // Disable/Enable button
                    if (toggleSavedModState(selectedMod.getModId()))
                        disableModButton.displayString = I18n.format("gui.modlist.button.disable");
                    else
                        disableModButton.displayString = I18n.format("gui.modlist.button.enable");
                    fmlModStateProps.save();
                    return;
            }
        }
        super.actionPerformed(button);
    }

    public int drawLine(String line, int offset, int shifty) {
        fontRendererObj.drawString(line, offset, shifty, 0xd7edea);
        return shifty + 10;
    }

    @Override
    public void drawScreen(int p_571_1_, int p_571_2_, float p_571_3_) {
        modList.drawScreen(p_571_1_, p_571_2_, p_571_3_);
        drawCenteredString(fontRendererObj, "Mod List", width / 2, 16, 0xFFFFFF);
        int offset = this.listWidth + 20;
        if (selectedMod != null) {
            GL11.glEnable(GL11.GL_BLEND);
            if (!selectedMod.getMetadata().autogenerated) {
                configModButton.visible = true;
                disableModButton.visible = true;
                disableModButton.packedFGColour = 0xFF3377;
                configModButton.enabled = false;
                int shifty = 35;
                String logoFile = selectedMod.getMetadata().logoFile;
                if (!logoFile.isEmpty()) {
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    TextureManager tm = mc.getTextureManager();
                    IResourcePack pack = FMLClientHandler.instance().getResourcePackFor(selectedMod.getModId());
                    try {
                        if (cachedLogo == null) {
                            BufferedImage logo = null;
                            if (pack != null)
                                logo = pack.getPackImage();
                            else {
                                InputStream logoResource = getClass().getResourceAsStream(logoFile);
                                if (logoResource != null)
                                    logo = ImageIO.read(logoResource);
                            }
                            if (logo != null) {
                                cachedLogo = tm.getDynamicTextureLocation("modlogo", new DynamicTexture(logo));
                                cachedLogoDimensions = new Dimension(logo.getWidth(), logo.getHeight());
                            }
                        }
                        if (cachedLogo != null) {
                            mc.renderEngine.bindTexture(cachedLogo);
                            double scaleX = cachedLogoDimensions.width / 200.0;
                            double scaleY = cachedLogoDimensions.height / 65.0;
                            double scale = 1.0;
                            if (scaleX > 1 || scaleY > 1)
                                scale = 1.0 / Math.max(scaleX, scaleY);
                            cachedLogoDimensions.width *= scale;
                            cachedLogoDimensions.height *= scale;
                            int top = 32;
                            Tessellator tess = Tessellator.instance;
                            tess.startDrawingQuads();
                            tess.addVertexWithUV(offset, top + cachedLogoDimensions.height, zLevel, 0, 1);
                            tess.addVertexWithUV(offset + cachedLogoDimensions.width, top + cachedLogoDimensions.height, zLevel, 1, 1);
                            tess.addVertexWithUV(offset + cachedLogoDimensions.width, top, zLevel, 1, 0);
                            tess.addVertexWithUV(offset, top, zLevel, 0, 0);
                            tess.draw();

                            shifty += 65;
                        }
                    } catch (IOException ignored) {
                    }
                }
                fontRendererObj.drawStringWithShadow(selectedMod.getMetadata().name, offset, shifty, 0xFFFFFF);
                shifty += 12;

                shifty = drawLine(String.format("Version: %s (%s)", selectedMod.getDisplayVersion(), selectedMod.getVersion()), offset, shifty);
                shifty = drawLine(String.format("Mod ID: '%s' Mod State: %s", selectedMod.getModId(), Loader.instance().getModState(selectedMod)), offset, shifty);
                if (!selectedMod.getMetadata().credits.isEmpty())
                    shifty = drawLine(String.format("Credits: %s", selectedMod.getMetadata().credits), offset, shifty);
                shifty = drawLine(String.format("Authors: %s", selectedMod.getMetadata().getAuthorList()), offset, shifty);
                shifty = drawLine(String.format("URL: %s", selectedMod.getMetadata().url), offset, shifty);
                shifty = drawLine(selectedMod.getMetadata().childMods.isEmpty() ? "No child mods for this mod" : String.format("Child mods: %s", selectedMod.getMetadata().getChildModList()), offset, shifty);
                int rightSide = width - offset - 20;
                if (rightSide > 20)
                    getFontRenderer().drawSplitString(selectedMod.getMetadata().description, offset, shifty + 10, rightSide, 0xDDDDDD);
                ModContainer.Disableable disableable = selectedMod.canBeDisabled();
                if (disableable == ModContainer.Disableable.YES) {
                    disableModButton.enabled = !blockedMods.contains(selectedMod.getModId());
                    disableModButton.visible = true;
                    disableModButton.packedFGColour = 0;
                } else {
                    disableModButton.enabled = !blockedMods.contains(selectedMod.getModId());
                    disableModButton.visible = true;
                    disableModButton.packedFGColour = 0xFF3377;
                }
                IModGuiFactory guiFactory = FMLClientHandler.instance().getGuiFactoryFor(selectedMod);
                if (guiFactory == null || guiFactory.mainConfigGuiClass() == null) {
                    configModButton.visible = true;
                    configModButton.enabled = false;
                } else {
                    configModButton.visible = true;
                    configModButton.enabled = true;
                }
            } else {
                offset = (listWidth + width) / 2;
                drawCenteredString(fontRendererObj, selectedMod.getName(), offset, 35, 0xFFFFFF);
                drawCenteredString(fontRendererObj, String.format("Version: %s", selectedMod.getVersion()), offset, 45, 0xFFFFFF);
                drawCenteredString(fontRendererObj, String.format("Mod State: %s", Loader.instance().getModState(selectedMod)), offset, 55, 0xFFFFFF);
                drawCenteredString(fontRendererObj, "No mod information found", offset, 65, 0xDDDDDD);
                drawCenteredString(fontRendererObj, "Ask your mod author to provide a mod mcmod.info file", offset, 75, 0xDDDDDD);
                configModButton.visible = false;
                disableModButton.visible = true; // false
            }
            GL11.glDisable(GL11.GL_BLEND);
        } else {
            configModButton.visible = false;
            disableModButton.visible = false;
        }
        super.drawScreen(p_571_1_, p_571_2_, p_571_3_);
    }

    Minecraft getMinecraftInstance() {
        return mc;
    }

    FontRenderer getFontRenderer() {
        return fontRendererObj;
    }

    public void selectModIndex(int var1) {
        selected = var1;
        if (var1 >= 0 && var1 <= mods.size()) {
            selectedMod = mods.get(selected);
            if (getSavedModState(selectedMod.getModId()))
                disableModButton.displayString = I18n.format("gui.modlist.button.disable");
            else
                disableModButton.displayString = I18n.format("gui.modlist.button.enable");
        } else
            selectedMod = null;
        cachedLogo = null;
    }

    public boolean modIndexSelected(int var1) {
        return var1 == selected;
    }

    public static class GuiSlotModListReplacer extends GuiScrollingList {
        private final GuiModListReplacer parent;
        private final ArrayList<ModContainer> mods;

        private final Map<String, ResourceLocation> cachedIcons = new HashMap<>();

        public GuiSlotModListReplacer(GuiModListReplacer parent, ArrayList<ModContainer> mods, int listWidth) {
            super(parent.getMinecraftInstance(), listWidth, parent.height, 32, parent.height - 66 + 4, 10, 35);
            this.parent = parent;
            this.mods = mods;
        }

        @Override
        protected int getSize() {
            return mods.size();
        }

        @Override
        protected void elementClicked(int var1, boolean var2) {
            parent.selectModIndex(var1);
        }

        @Override
        protected boolean isSelected(int var1) {
            return parent.modIndexSelected(var1);
        }

        @Override
        protected void drawBackground() {
            parent.drawDefaultBackground();
        }

        @Override
        protected int getContentHeight() {
            return (getSize()) * 35 + 1;
        }

        @Override
        protected void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator var5) {
            ModContainer mc = mods.get(listIndex);
            boolean actual = parent.getActualModState(mc);
            boolean saved = parent.getSavedModState(mc.getModId());
            if (actual == saved) {
                if (!actual) {
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mc.getName(), listWidth - 10), left + 3 + 30 + 3, var3 + 2, 0xFF2222);
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mc.getDisplayVersion(), listWidth - 10), left + 3 + 30 + 3, var3 + 12, 0xFF2222);
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth("DISABLED", listWidth - 10), left + 3 + 30 + 3, var3 + 22, 0xFF2222);
                } else {
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mc.getName(), listWidth - 10), left + 3 + 30 + 3, var3 + 2, 0xFFFFFF);
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mc.getDisplayVersion(), listWidth - 10), left + 3 + 30 + 3, var3 + 12, 0xCCCCCC);
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mc.getMetadata() != null ? mc.getMetadata().getChildModCountString() : "Metadata not found", listWidth - 10), left + 3 + 30 + 3, var3 + 22, 0xCCCCCC);
                }
            } else {
                if (!actual) {
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mc.getName(), listWidth - 10), left + 3 + 30 + 3, var3 + 2, 0xFFFF55);
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mc.getDisplayVersion(), listWidth - 10), left + 3 + 30 + 3, var3 + 12, 0xFFFF55);
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth("DISABLED", listWidth - 10), left + 3 + 30 + 3, var3 + 22, 0xFFFF55);
                } else {
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mc.getName(), listWidth - 10), left + 3 + 30 + 3, var3 + 2, 0xFFFF55);
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mc.getDisplayVersion(), listWidth - 10), left + 3 + 30 + 3, var3 + 12, 0xDDD605);
                    parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(mc.getMetadata() != null ? mc.getMetadata().getChildModCountString() : "Metadata not found", listWidth - 10), left + 3 + 30 + 3, var3 + 22, 0xDDD605);
                }
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            TextureManager tm = parent.mc.getTextureManager();
            try {
                if (cachedIcons.get(mc.getModId()) == null) {
                    if (modIcons.containsKey(mc.getModId())) {
                        BufferedImage logo = null;
                        InputStream logoResource = getClass().getResourceAsStream(modIcons.get(mc.getModId()));
                        if (logoResource != null)
                            logo = ImageIO.read(logoResource);
                        if (logo != null) {
                            cachedIcons.put(mc.getModId(), tm.getDynamicTextureLocation("icon_" + mc.getModId(), new DynamicTexture(logo)));
                        }
                    } else {
                        cachedIcons.put(mc.getModId(), tm.getDynamicTextureLocation("icon_" + mc.getModId(), TextureUtil.missingTexture));
                    }
                }
                if (cachedIcons.get(mc.getModId()) != null) {
                    parent.mc.renderEngine.bindTexture(cachedIcons.get(mc.getModId()));
                    var5.startDrawingQuads();
                    var5.addVertexWithUV(left + 3, var3 + 2 + 30, parent.zLevel, 0, 1);
                    var5.addVertexWithUV(left + 3 + 30, var3 + 2 + 30, parent.zLevel, 1, 1);
                    var5.addVertexWithUV(left + 3 + 30, var3 + 2, parent.zLevel, 1, 0);
                    var5.addVertexWithUV(left + 3, var3 + 2, parent.zLevel, 0, 0);
                    var5.draw();
                }
            } catch (IOException ignored) {
            }
        }

    }
}
