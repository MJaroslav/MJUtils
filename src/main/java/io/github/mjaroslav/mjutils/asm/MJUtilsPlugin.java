package io.github.mjaroslav.mjutils.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import io.github.mjaroslav.mjutils.internal.lib.ModInfo;
import io.github.tox1cozz.mixinbooterlegacy.IEarlyMixinLoader;

import java.util.Collections;
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
        return Collections.singletonList("mixin.mjutils.json");
    }
}
