package io.github.mjaroslav.mjutils.modular;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.BeforeClass;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import io.github.mjaroslav.mjutils.mod.MJUtils;
import io.github.mjaroslav.mjutils.mod.common.modular.MainModule;
import cpw.mods.fml.common.LoaderState;

@Common(when = LoaderState.CONSTRUCTING)
public class TestInGameModuleLoader {
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
        Assert.isTrue(instance == loader.getModInstance(), "Different mod instances");
    }

    @Test
    void test$activatedModules() {
        // Expected modules: MainModule, Proxy and not activated ThaumcraftModule
        Assert.isEquals(loader.getActivatedModulesCount(), 2, "Found non expected modules");
        Assert.isEquals(loader.getFoundModulesCount(), 3, "Thaumcraft module not found");
        Assert.isEquals(loader.getModules().get(0).getModuleClassName(), MainModule.class.getName(),
                "Non expected module activated");
        Assert.isEquals(loader.getModules().get(1).getModule(), proxy, "Loaded different proxy module");
    }
}