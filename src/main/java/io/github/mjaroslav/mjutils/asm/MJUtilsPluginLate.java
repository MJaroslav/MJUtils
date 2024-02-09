package io.github.mjaroslav.mjutils.asm;

import cpw.mods.fml.common.Loader;
import io.github.mjaroslav.mjutils.asm.MixinPatches.Compatibility;
import io.github.mjaroslav.mjutils.asm.MixinPatches.Compatibility.Thaumcraft;
import io.github.tox1cozz.mixinbooterlegacy.ILateMixinLoader;
import io.github.tox1cozz.mixinbooterlegacy.LateMixin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

@LateMixin
public class MJUtilsPluginLate implements ILateMixinLoader {
    @Override
    public @NotNull List<String> getMixinConfigs() {
        return Collections.singletonList("mixin.mjutils.compatibility.thaumcraft.json");
    }

    @Override
    public boolean shouldMixinConfigQueue(@NotNull String mixinConfig) {
        return Compatibility.enable && "mixin.mjutils.compatibility.thaumcraft.json".equals(mixinConfig) &&
            Thaumcraft.enable && Thaumcraft.hidePopupsOfResearchCopies && Loader.isModLoaded("Thaumcraft");
    }
}
