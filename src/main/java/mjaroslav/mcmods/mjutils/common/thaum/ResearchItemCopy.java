package mjaroslav.mcmods.mjutils.common.thaum;

import mjaroslav.mcmods.mjutils.common.thaum.ThaumEventHandler.ResearchCopy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class ResearchItemCopy extends ResearchItem {
	private String originalKey;

	public ResearchItemCopy(String originalKey, String key, String category) {
		super(key, category);
		this.originalKey = originalKey;
		this.setStub();
		ThaumEventHandler.researchCopyList.add(new ResearchCopy(originalKey, key));
		this.setParents(originalKey);
		this.setConcealed();
		this.setHidden();
		this.setPages(ResearchCategories.getResearch(originalKey).getPages());
	}

	public ResearchItemCopy(String originalKey, String key, String category, int col, int row, ResourceLocation icon) {
		super(key, category, new AspectList(), col, row, 1, icon);
		this.originalKey = originalKey;
		this.setStub();
		ThaumEventHandler.researchCopyList.add(new ResearchCopy(originalKey, key));
		this.setParents(originalKey);
		this.setConcealed();
		this.setHidden();
		this.setPages(ResearchCategories.getResearch(originalKey).getPages());
	}

	public ResearchItemCopy(String originalKey, String key, String category, int col, int row, ItemStack icon) {
		super(key, category, new AspectList(), col, row, 1, icon);
		this.originalKey = originalKey;
		this.setStub();
		ThaumEventHandler.researchCopyList.add(new ResearchCopy(originalKey, key));
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

	public ResearchItemCopy registerResearchItem() {
		ResearchCategories.addResearch(this);
		return this;
	}
}
