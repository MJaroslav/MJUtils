# MJUtils #

A set of tools to simplify writing mods. It contains a modular system
for initialization of mods and a system of configurations by
annotations.

The source code contains the documentation.

If you are using ResearchItemCopy, you must add the following
dependency: **"required-after:Thaumcraft;"**.

You can send a pull request if you find grammar errors.

## Modular system examples ##

Add module and configuration handlers to the main mod class:

```java
package your.path;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import mjaroslav.mcmods.mjutils.module.ModuleSystem;
import mjaroslav.mcmods.mjutils.module.AnnotationBasedConfiguration;

@Mod(modid = "your_modid", version = "0.0.0", name = "Module example mod")
public class ModExample {
    // Handler for annotation configurations.
    private static AnnotationBasedConfiguration config;
    // Handler for work with modules.
    private static ModuleSystem modSys;
    
    @EventHandler
    public void construct(FMLConstructionEvent event) {
        // The second argument is logger.
        config = new AnnotationBasedConfiguration("your_modid", null);
        // The third argument is a proxy.
        modSys = new ModuleSystem(MODID, config, null);
        // Find all modules and create their instances..
        modSys.initSystem(event);
    }
    
    // DON'F FORGET CALL ALL INITIALIZATION METHODS:
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        modSys.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        modSys.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        modSys.postInit(event);
    }
}
```

Now you can create simple module:

```java
package your.path;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
//import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import mjaroslav.mcmods.mjutils.module.Modular;
import mjaroslav.mcmods.mjutils.module.Module;

// All classes with Module annotation must implement the Modular
// interface.
@Module("your_modid")
public class ModuleExample implements Modular {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("Hi! I'm in pre init!");
    }
    
//    All interface methods that can be neglected contain the default
//    implementation: you can inherit them only when necessary.
//    
//    @Override
//    public void init(FMLInitializationEvent event) {
//        System.out.println("I'm ignored :c");
//    }
    
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("Hi! I'm in post init!");
    }
}
```

An example of creating an annotation configuration:

```java
package your.path;

import mjaroslav.mcmods.mjutils.module.ConfigurationCategory;
import mjaroslav.mcmods.mjutils.module.ConfigurationProperty;

@ConfigurationCategory(modID = "your_modid", name = ConfigurationCategory.GENERAL_NAME,
        comment = ConfigurationCategory.GENERAL_COMMENT)
public class CategoryGeneral {
    @ConfigurationCategory(name = "subcategory")
    public static class SubCategory {
        @ConfigurationProperty(defaultBoolean = true)
        public static boolean test;

        @ConfigurationProperty(name = "test2", defaultInt = 1)
        public static int test1;
    }
    
    @ConfigurationProperty(defaultDouble = Math.E)
    public static double e;
}

```

Use the documentation to find out more info about library usages.

