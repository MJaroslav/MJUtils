package mjaroslav.mcmods.example;

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
}
