package io.github.mjaroslav.mjutilstest.modular.compat;

import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import io.github.mjaroslav.mjutils.modular.SubscribeModule;
import io.github.mjaroslav.mjutils.util.game.UtilsThaumcraft;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.research.ResearchCategories;

@SubscribeModule(modDependencies = "Thaumcraft", loadOn = ModState.POSTINITIALIZED, priority = 10)
public class ThaumcraftTestModule {
    public static final String CATEGORY = "MJUTILSTEST";
    public static final String NITOR = "NITOR";

    public void listen(@NotNull FMLPostInitializationEvent event) {
        ResearchCategories.registerCategory(CATEGORY, new ResourceLocation("textures/items/apple.png"),
            new ResourceLocation("thaumcraft:textures/gui/gui_researchback.png"));

        UtilsThaumcraft.createCopyResearch(NITOR, CATEGORY, 0, 0).registerResearchItem();
        UtilsThaumcraft.createCopyResearch(NITOR, CATEGORY, 2, 2).registerResearchItem();
        UtilsThaumcraft.createCopyResearch(NITOR, CATEGORY, -2, -2).registerResearchItem();
    }
}
