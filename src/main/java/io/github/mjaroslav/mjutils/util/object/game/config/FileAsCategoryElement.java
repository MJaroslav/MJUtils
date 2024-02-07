package io.github.mjaroslav.mjutils.util.object.game.config;

import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import org.jetbrains.annotations.NotNull;

public class FileAsCategoryElement extends DummyCategoryElement<Object> {
    protected final @NotNull Configuration configuration;
    protected final @NotNull String rootCategory;
    protected final @NotNull String configId;

    public FileAsCategoryElement(@NotNull Configuration configuration, @NotNull String rootCategory,
                                 @NotNull String configId) {
        super(configuration.getCategory(rootCategory).getName(),
            configuration.getCategory(rootCategory).getLanguagekey(), FileEntry.class);
        this.configuration = configuration;
        this.rootCategory = rootCategory;
        this.configId = configId;
    }

    @Override
    public String getComment() {
        return configuration.getCategory(rootCategory).getComment();
    }

    public static class FileEntry extends CategoryEntry {
        public FileEntry(GuiConfig owningScreen,
                         GuiConfigEntries owningEntryList,
                         IConfigElement configElement) {
            super(owningScreen, owningEntryList, configElement);
        }

        @Override
        protected GuiScreen buildChildScreen() {
            return configElement instanceof FileAsCategoryElement fileElement ? new GuiConfig(owningScreen,
                new ConfigElement<>(fileElement.configuration.getCategory(fileElement.rootCategory))
                    .getChildElements(), owningScreen.modID, fileElement.configId, configElement.requiresWorldRestart() ||
                owningScreen.allRequireWorldRestart, configElement.requiresMcRestart() || owningScreen.allRequireMcRestart,
                GuiConfig.getAbridgedConfigPath(fileElement.configuration.toString())) : super.buildChildScreen();
        }
    }
}
