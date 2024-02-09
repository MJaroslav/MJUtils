package io.github.mjaroslav.mjutils.util.object.game;

import io.github.mjaroslav.mjutils.util.game.UtilsThaumcraft;
import lombok.Getter;
import lombok.val;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

import java.util.Arrays;

/**
 * Decorative research. Copy of vanilla research. Autocompletable
 * by parent. If you use this, you must add to your modification a
 * dependency: "required-after:Thaumcraft;"
 */
@Getter
public class ResearchItemShadow extends ResearchItem {
    protected final String originalKey;

    public ResearchItemShadow(@NotNull String originalKey, @NotNull String category, int col, int row,
                              @NotNull ResourceLocation icon) {
        super(UtilsThaumcraft.getNextCopyResearchKey(originalKey), category, new AspectList(), col, row, 1, icon);
        this.originalKey = originalKey;
        init();
    }

    public ResearchItemShadow(@NotNull String originalKey, @NotNull String category, int col, int row,
                              @NotNull ItemStack icon) {
        super(UtilsThaumcraft.getNextCopyResearchKey(originalKey), category, new AspectList(), col, row, 1, icon);
        this.originalKey = originalKey;
        init();
    }

    protected void init() {
        setPages(ResearchCategories.getResearch(originalKey).getPages());
        setStub();
        setParents(originalKey);
        setConcealed();
        setHidden();
    }

    @Override
    public @NotNull ResearchItemShadow registerResearchItem() {
        val original = ResearchCategories.getResearch(originalKey);
        var siblings = original.siblings;
        if (siblings == null) siblings = new String[0];
        val len = siblings.length;
        siblings = Arrays.copyOf(siblings, len + 1);
        siblings[len] = key;
        original.setSiblings(siblings);
        return (ResearchItemShadow) super.registerResearchItem();
    }

    @Override
    public @NotNull String getName() {
        return StatCollector.translateToLocal("tc.research_name." + originalKey);
    }

    @Override
    public @NotNull String getText() {
        return StatCollector.translateToLocal("tc.research_text." + originalKey);
    }
}
