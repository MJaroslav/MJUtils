package com.github.mjaroslav.mjutils.test.ingame;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.BeforeClass;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import com.github.mjaroslav.mjutils.mod.MJUtils;
import com.github.mjaroslav.mjutils.mod.common.modular.MainModule;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.modular.Proxy;
import cpw.mods.fml.common.LoaderState;

@Common(when = LoaderState.CONSTRUCTING)
public class TestModuleLoader {
    static ModuleLoader loader;
    static Proxy proxy;
    static MJUtils instance;

    @BeforeClass
    static void beforeClass() {
        loader = MJUtils.loader;
        proxy = MJUtils.proxy;
        instance = MJUtils.instance;
    }

    @Test
    void test$loaderCreated() {
        Assert.isTrue(loader != null, "Loader not created");
    }

    @Test
    void test$proxyCreated() {
        Assert.isTrue(proxy != null, "Proxy not created");
    }

    @Test
    void test$modInstance() {
        Assert.isTrue(instance == loader.modInstance, "Different mod instances");
    }

    @Test
    void test$activatedModules() {
        // Expected modules: MainModule, Proxy and not activated ThaumcraftModule
        Assert.isEquals(loader.getActivatedModulesCount(), 2, "Found non expected modules");
        Assert.isEquals(loader.getFoundModulesCount(), 3, "Thaumcraft module not found");
        Assert.isEquals(loader.getModules().get(0).moduleClassName, MainModule.class.getName(),
                "Non expected module activated");
        Assert.isEquals(loader.getModules().get(1).getModule(), proxy, "Loaded different proxy module");
    }
}
