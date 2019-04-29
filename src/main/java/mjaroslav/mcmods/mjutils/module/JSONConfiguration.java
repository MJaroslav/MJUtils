package mjaroslav.mcmods.mjutils.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

public class JSONConfiguration implements Initializator.Configurable {
    private static final JsonParser PARSER = new JsonParser();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final String fileName;
    private JsonElement instance;
    private final JsonElement standard;
    private File file;

    public JSONConfiguration(String fileName, String standardResourceLocation) {
        this.fileName = fileName;
        standard = PARSER.parse(new InputStreamReader(JSONConfiguration.class.getResourceAsStream("/" +
                standardResourceLocation), StandardCharsets.UTF_8));
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        file = new File(event.getModConfigurationDirectory(), fileName);
        load();
    }

    public JsonElement getInstance() {
        return instance;
    }

    public void setInstance(JsonElement instance) {
        this.instance = instance;
    }

    public void toDefault() {
        // Copy
        instance = PARSER.parse(standard.toString());
        save();
    }

    public void load() {
        try {
            if (file.isFile()) {
                instance = PARSER.parse(Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8));
            } else toDefault();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            Files.write(file.toPath(), Collections.singleton(GSON.toJson(instance)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
