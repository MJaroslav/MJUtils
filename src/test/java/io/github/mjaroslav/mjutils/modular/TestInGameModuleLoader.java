package io.github.mjaroslav.mjutils.modular;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.BeforeClass;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import cpw.mods.fml.common.LoaderState;
import io.github.mjaroslav.mjutils.internal.MJUtils;

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
        Assert.isTrue(instance == loader.getModInstance(), "Different internal instances");
    }

    @Test
    void test$activatedModules() {
        // Expected modules: MainModule, Proxy and not activated ThaumcraftModule
        // But I added extra logic because I sometimes run tests by IDEA
//        val thaumcraft = UtilsMods.isModsLoaded("Thaumcraft");
//        Assert.isEquals(loader.getActivatedModulesCount(), thaumcraft ? 4 : 3, "Found non expected modules");
//        Assert.isEquals(loader.getFoundModulesCount(), 4, "Thaumcraft module not found");
//        Assert.isEquals(loader.getModules().get(0).getModuleClassName(), MainModule.class.getName(),
//            "Non expected module activated");
//        Assert.isEquals(loader.getModules().get(thaumcraft ? 3 : 2).getModule(), proxy, "Loaded different proxy module");
    }
}
