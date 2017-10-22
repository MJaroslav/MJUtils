package mjaroslav.mcmods.mjutils;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class MJInfo {
	/** MJUtils: mod id */
	public static final String MODID = "mjutils";
	/** MJUtils: mod name */
	public static final String NAME = "MJUtils";
	/** MJUtils: mod version */
	public static final String VERSION = "1.7.10-1";
	/** MJUtils: Mod package for modules */
	public static final String MODULEPACKAGE = "mjaroslav.mcmods.mjutils.common.init";

	/** Ticks in one second */
	public static final int ticksInSecond = 20;
	/** Ticks in one minute */
	public static final int ticksInMinute = 1200;
	/** Ticks in one hour */
	public static final int ticksInHour = 72000;
	/** Tick in one game day (20 minutes) */
	public static final int ticksInDay = 24000;
	/** Ticks in one smelt process */
	public static final int ticksInSmelt = 200;

	/** Max short value, uses for set any meta of ItemStack in recipes */
	public static final int anyMeta = 32767;

	/** Default ItemStack for FISH list */
	public static final ItemStack fishingDefaultFish = new ItemStack(Items.fish, 1, 0);
	/** Default ItemStack for JUNK list */
	public static final ItemStack fishingDefaultJunk = new ItemStack(Items.stick, 1, 0);
	/** Default ItemStack for TREASURE list */
	public static final ItemStack fishingDefaultTreasure = new ItemStack(Items.name_tag, 1, 0);
	/** Default weight (rarity) for FISH list */
	public static final int fishingRarityFish = 60;
	/** Default weight (rarity) for JUNK list */
	public static final int fishingRarityJunk = 10;
	/** Default weight (rarity) for TREASURE list */
	public static final int fishingRarityTreasure = 1;
}
