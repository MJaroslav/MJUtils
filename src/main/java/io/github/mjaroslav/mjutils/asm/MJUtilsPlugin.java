package io.github.mjaroslav.mjutils.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import io.github.mjaroslav.mjutils.internal.common.modular.MainModule;
import io.github.mjaroslav.mjutils.internal.lib.General.MixinPatches;
import io.github.mjaroslav.mjutils.internal.lib.General.MixinPatches.Enchantments;
import io.github.mjaroslav.mjutils.internal.lib.General.MixinPatches.Potions;
import io.github.mjaroslav.mjutils.internal.lib.ModInfo;
import io.github.tox1cozz.mixinbooterlegacy.IEarlyMixinLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@MCVersion("1.7.10")
@Name(ModInfo.modId)
public class MJUtilsPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {
    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public List<String> getMixinConfigs() {
        return Arrays.asList("mixin.mjutils.accessors.json", "mixin.mjutils.json", "mixin.mjutils.fishing.json",
            "mixin.mjutils.potions.json", "mixin.mjutils.potions.nbt.json", "mixin.mjutils.enchantments.json");
    }

    @Override
    public boolean shouldMixinConfigQueue(@NotNull String mixinConfig) {
        return switch (mixinConfig) {
            case "mixin.mjutils.fishing.json" -> MixinPatches.fishing;
            case "mixin.mjutils.potions.json" -> Potions.enable;
            case "mixin.mjutils.potions.nbt.json" -> Potions.enable && Potions.patchNBT;
            case "mixin.mjutils.enchantments.json" -> Enchantments.enable;
            default -> true; // All other configurations are not optional
        };
    }

    static {
        MainModule.config.load();
    }
}
