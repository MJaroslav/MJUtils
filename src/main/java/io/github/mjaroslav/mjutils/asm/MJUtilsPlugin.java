package io.github.mjaroslav.mjutils.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import io.github.mjaroslav.mjutils.asm.MixinPatches.Enchantments;
import io.github.mjaroslav.mjutils.asm.MixinPatches.Potions;
import io.github.mjaroslav.mjutils.config.ForgeAnnotationConfig;
import io.github.mjaroslav.mjutils.lib.MJUtilsInfo;
import io.github.tox1cozz.mixinbooterlegacy.IEarlyMixinLoader;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@MCVersion("1.7.10")
@Name(MJUtilsInfo.MOD_ID)
public class MJUtilsPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {
    public static final ForgeAnnotationConfig CONFIG = new ForgeAnnotationConfig(MJUtilsInfo.MOD_ID,
        Paths.get("config", MJUtilsInfo.MOD_ID, "mixins.cfg"), "2.0.0", MixinPatches.class);

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
        CONFIG.load();
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
}
