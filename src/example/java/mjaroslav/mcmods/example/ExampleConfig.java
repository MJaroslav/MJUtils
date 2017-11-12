package mjaroslav.mcmods.example;

import mjaroslav.mcmods.mjutils.common.objects.ConfigurationBase;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

public class ExampleConfig extends ConfigurationBase {
    // Configuration categories.
    public static final String categoryExample = "example";
    // Configuration fields.
    public static boolean iAmABoolean;
    public static int iAmAnInteger;
    public static String iAmTheString;
    public static boolean useExampleMod;
    /**
     * Instance (file) of configuration.
     */
    private Configuration instance;

    @Override
    public String getModId() {
        return ExampleInfo.MODID;
    }

    @Override
    public Logger getLogger() {
        return ExampleMod.LOG;
    }

    @Override
    public void readFields() {
        // You can use getInstance() or instance.
        iAmABoolean = getInstance().getBoolean("iAmABoolean", categoryExample, true, "I am a boolean!");
        iAmAnInteger = instance.getInt("iAmAnInteger", categoryExample, 1917, Integer.MIN_VALUE, Integer.MAX_VALUE,
                "I am an integer!");
        iAmTheString = getInstance().getString("iAmTheString", categoryExample,
                "Time is the cage that we created to feel safe.", "I am the string!");
        useExampleMod = getInstance().getBoolean("use_example_mod", categoryExample, true,
                "Use examples. Requires game restart.");
    }

    @Override
    public Configuration getInstance() {
        return this.instance;
    }

    @Override
    public void setInstance(Configuration newConfig) {
        this.instance = newConfig;
    }
}
