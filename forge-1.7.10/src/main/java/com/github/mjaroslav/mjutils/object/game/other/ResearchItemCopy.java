package com.github.mjaroslav.mjutils.object.game.other;

import com.github.mjaroslav.mjutils.mod.common.handler.ThaumEventHandler;
import com.github.mjaroslav.mjutils.mod.common.handler.ThaumEventHandler.ResearchCopy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

/**
 * Decorative research. Copy of vanilla research. Autocompletable
 * by parent. If you use this, you must add to your modification a
 * dependency: "required-after:Thaumcraft;"
 */
public class ResearchItemCopy extends ResearchItem {
    private String originalKey;

    public ResearchItemCopy(String originalKey, String key, String category) {
        super(key, category);
        this.originalKey = originalKey;
        this.setStub();
        ThaumEventHandler.newResearchCopy(new ResearchCopy(originalKey, key));
        this.setParents(originalKey);
        this.setConcealed();
        this.setHidden();
        this.setPages(ResearchCategories.getResearch(originalKey).getPages());
    }

    public ResearchItemCopy(String originalKey, String key, String category, int col, int row, ResourceLocation icon) {
        super(key, category, new AspectList(), col, row, 1, icon);
        this.originalKey = originalKey;
        this.setStub();
        ThaumEventHandler.newResearchCopy(new ResearchCopy(originalKey, key));
        this.setParents(originalKey);
        this.setConcealed();
        this.setHidden();
        this.setPages(ResearchCategories.getResearch(originalKey).getPages());
    }

    public ResearchItemCopy(String originalKey, String key, String category, int col, int row, ItemStack icon) {
        super(key, category, new AspectList(), col, row, 1, icon);
        this.originalKey = originalKey;
        this.setStub();
        ThaumEventHandler.newResearchCopy(new ResearchCopy(originalKey, key));
        this.setParents(originalKey);
        this.setConcealed();
        this.setHidden();
        this.setPages(ResearchCategories.getResearch(originalKey).getPages());
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal("tc.research_name." + originalKey);
    }

    @Override
    public String getText() {
        return StatCollector.translateToLocal("tc.research_text." + originalKey);
    }

    @Override
    public ResearchItemCopy registerResearchItem() {
        ResearchCategories.addResearch(this);
        return this;
    }
}
