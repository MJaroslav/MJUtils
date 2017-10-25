package mjaroslav.mcmods.mjutils.common.config;

import mjaroslav.mcmods.mjutils.MJInfo;
import mjaroslav.mcmods.mjutils.MJUtils;
import mjaroslav.mcmods.mjutils.common.objects.ConfigurationBase;
import net.minecraftforge.common.config.Configuration;

public class Config extends ConfigurationBase {
	public static boolean chainToIron = true;

	public Config() {
		super(MJInfo.MODID, MJUtils.log);
	}

	@Override
	public void readFields() {
		chainToIron = config.getBoolean("chain_to_iron", Configuration.CATEGORY_GENERAL, true,
				"Change chainmail armor to iron by iron ingot on anvil");
	}
}
