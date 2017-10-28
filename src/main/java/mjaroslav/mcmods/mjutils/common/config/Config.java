package mjaroslav.mcmods.mjutils.common.config;

import mjaroslav.mcmods.mjutils.MJInfo;
import mjaroslav.mcmods.mjutils.MJUtils;
import mjaroslav.mcmods.mjutils.common.objects.ConfigurationBase;

/**
 * Configuration for MJUtils mod
 * 
 * @author MJaroslav
 *
 */
public class Config extends ConfigurationBase {
	/**
	 * Category for server settings and those that require restarting the game.
	 */
	public static final String CATEGORY_COMMON = "common";
	/**
	 * Category for client settings and those that not require restarting the
	 * game.
	 */
	public static final String CATEGORY_CLIENT = "client";

	// COMMON FIELDS
	/** Change chainmail armor to iron by iron ingot on anvil. */
	public static boolean chainToIron = true;

	// CLIENT FIELDS
	/** Show ore dict names in tooltip (in advanced tooltip mode). */
	public static boolean showOreDict = true;
	/** Show ore dict names allways. */
	public static boolean showOreDictAllways = true;

	public Config() {
		super(MJInfo.MODID, MJUtils.log);
	}

	@Override
	public void readFields() {
		chainToIron = instance.getBoolean("chain_to_iron", CATEGORY_COMMON, true,
				"Change chainmail armor to iron by iron ingot on anvil.");

		showOreDict = instance.getBoolean("show_oredict", CATEGORY_CLIENT, true,
				"Show ore dict names in tooltip (in advanced tooltip mode).");
		showOreDictAllways = instance.getBoolean("show_oredict_allways", CATEGORY_CLIENT, false,
				"Show ore dict names allways.");
	}
}
