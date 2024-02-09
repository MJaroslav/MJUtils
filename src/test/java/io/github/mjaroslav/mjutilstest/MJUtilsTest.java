package io.github.mjaroslav.mjutilstest;

import cpw.mods.fml.common.Mod;
import io.github.mjaroslav.mjutils.modular.ModuleLoader;
import io.github.mjaroslav.mjutils.modular.SubscribeLoader;
import io.github.mjaroslav.mjutilstest.lib.MJUtilsTestInfo;

@Mod(modid = MJUtilsTestInfo.MOD_ID, name = MJUtilsTestInfo.NAME, version = MJUtilsTestInfo.VERSION,
    dependencies = MJUtilsTestInfo.DEPENDENCIES)
public class MJUtilsTest {
    @SubscribeLoader
    public static ModuleLoader loader;
}
