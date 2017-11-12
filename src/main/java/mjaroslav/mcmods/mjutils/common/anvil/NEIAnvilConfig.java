package mjaroslav.mcmods.mjutils.common.anvil;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import mjaroslav.mcmods.mjutils.MJInfo;
import net.minecraft.client.gui.GuiRepair;

/**
 * NEI Integration with anvil recipes.
 *
 * @author MJaroslav
 */
public class NEIAnvilConfig implements IConfigureNEI {
  @Override
  public String getName() {
    return MJInfo.NAME + ":Anvil";
  }

  @Override
  public String getVersion() {
    return MJInfo.VERSION;
  }

  @Override
  public void loadConfig() {
    API.registerRecipeHandler(new NEIAnvilRecipeHandler());
    API.registerUsageHandler(new NEIAnvilRecipeHandler());
    API.setGuiOffset(GuiRepair.class, 5, 11);
  }
}
