package mjaroslav.mcmods.mjutils.common.thaum;

import mjaroslav.mcmods.mjutils.common.thaum.ThaumEventHandler.ResearchCopy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

/**
 * If you use this, you must add to your modification a dependency:
 * "required-after:Thaumcraft;"
 * 
 * @author MJaroslav
 *
 */
public class ResearchItemCopy extends ResearchItem {
	/**
	 * Key of original research.
	 */
	private String originalKey;

	/**
	 * @see ResearchItemCopy
	 * @param originalKey
	 *            - {@link #originalKey}
	 * @param key
	 *            - new research key, only upper case
	 * @param category
	 *            - new research category.
	 */
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

	/**
	 * @see ResearchItemCopy
	 * @param originalKey
	 *            - {@link #originalKey}
	 * @param key
	 *            - new research key, only upper case
	 * @param category
	 *            - new research category.
	 * @param col
	 *            - x coordinate in GUI (horizontal).
	 * @param row
	 *            - y coordinate in GUI (vertical).
	 * @param icon
	 *            - icon location of research.
	 */
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

	/**
	 * @see ResearchItemCopy
	 * @param originalKey
	 *            - {@link #originalKey}
	 * @param key
	 *            - new research key, only upper case
	 * @param category
	 *            - new research category.
	 * @param col
	 *            - x coordinate in GUI (horizontal).
	 * @param row
	 *            - y coordinate in GUI (vertical).
	 * @param icon
	 *            - item to be used as an icon.
	 */
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
