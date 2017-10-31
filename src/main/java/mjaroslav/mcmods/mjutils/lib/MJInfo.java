package mjaroslav.mcmods.mjutils.lib;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Class with MJUtils constants.
 * 
 * @author MJaroslav
 * 
 */
public class MJInfo {
	/** MJUtils: mod id. */
	public static final String MODID = "mjutils";
	/** MJUtils: mod name. */
	public static final String NAME = "MJUtils";
	/** MJUtils: mod version. */
	public static final String VERSION = "1.7.10-2";
	/** MJUtils: Mod common proxy. */
	public static final String COMMONPROXY = "mjaroslav.mcmods.mjutils.common.MJUtilsCommonProxy";
	/** MJUtils: Mod client proxy. */
	public static final String CLIENTPROXY = "mjaroslav.mcmods.mjutils.client.MJUtilsClientProxy";
	/** MJUtils: Mod gui factory. */
	public static final String GUIFACTORY = "mjaroslav.mcmods.mjutils.client.gui.MJUtilsGUIFactory";

	/** Max short value, uses for set any meta of ItemStack in recipes. */
	public static final int anyMeta = 32767;

	/** Default ItemStack for FISH list. */
	public static final ItemStack fishingDefaultFish = new ItemStack(Items.fish, 1, 0);
	/** Default ItemStack for JUNK list. */
	public static final ItemStack fishingDefaultJunk = new ItemStack(Items.stick, 1, 0);
	/** Default ItemStack for TREASURE list. */
	public static final ItemStack fishingDefaultTreasure = new ItemStack(Items.name_tag, 1, 0);
	/** Default weight (rarity) for FISH list. */
	public static final int fishingRarityFish = 60;
	/** Default weight (rarity) for JUNK list. */
	public static final int fishingRarityJunk = 10;
	/** Default weight (rarity) for TREASURE list. */
	public static final int fishingRarityTreasure = 1;
}
