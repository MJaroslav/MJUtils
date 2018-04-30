package mjaroslav.mcmods.mjutils.lib.handler;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import net.minecraft.client.gui.GuiRepair;

public class NEIAnvilConfig implements IConfigureNEI {
    @Override
    public String getName() {
        return ModInfo.NAME + ":Anvil";
    }

    @Override
    public String getVersion() {
        return ModInfo.VERSION;
    }

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new NEIAnvilRecipeHandler());
        API.registerUsageHandler(new NEIAnvilRecipeHandler());
        API.setGuiOffset(GuiRepair.class, 5, 11);
    }
}
