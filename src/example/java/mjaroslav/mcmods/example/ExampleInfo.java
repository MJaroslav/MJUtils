package mjaroslav.mcmods.example;

import mjaroslav.mcmods.mjutils.common.objects.ConfigField;

public class ExampleInfo {
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0.0";
    public static final String NAME = "Example Mod (MJUtils)";
    public static final String GUIFACTORY = "mjaroslav.mcmods.example.ExampleGuiFactory";
    // If you are using ReseararchItems, you must add this dependency (this
    // version is recommended).
    public static final String DEPENDENCIES = "required-after:Thaumcraft@[4.2.3.5,);";
    public static final String CLIENTPROXY = "mjaroslav.mcmods.example.ExampleClientProxy";
    public static final String COMMONPROXY = "mjaroslav.mcmods.example.ExampleCommonProxy";

    // Configuration categories.
    public static final String categoryExample = "example";
    // Configuration fields.
    @ConfigField(defaultBoolean = true, category = categoryExample, comment = "I am a boolean!")
    public static boolean iAmABoolean;
    @ConfigField(defaultInt = 1917, category = categoryExample, comment = "I am an integer!")
    public static int iAmAnInteger;
    @ConfigField(defaultString = "Time is the cage that we created to feel safe.", category = categoryExample, comment = "I am the string!")
    public static String iAmTheString;
    @ConfigField(customName = "enable_example_mod", defaultBoolean = true, category = categoryExample, comment = "Use examples. Requires game restart.")
    public static boolean useExampleMod;
}
