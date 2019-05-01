package mjaroslav.mcmods.mjutils.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

/**
 * Gson based custom configuration.
 *
 * @author MJaroslav
 */
public class JSONConfiguration implements Initializator.Configurable {
    private static final JsonParser PARSER = new JsonParser();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final String fileName;
    private JsonElement instance;
    private final JsonElement standard;
    private File file;

    /**
     * See class documentation.
     *
     * @param fileName                 configuration file name. Subdirectories
     *                                 will not be created and may cause crash.
     * @param standardResourceLocation path to default value in resources.
     */
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

    /**
     * Get current instance of configuration.
     *
     * @return Current instance object.
     */
    public JsonElement getInstance() {
        return instance;
    }

    /**
     * Set new instance for configuration without saving.
     *
     * @param instance new instance object.
     */
    public void setInstance(JsonElement instance) {
        this.instance = instance;
    }

    /**
     * Set current instance to standard and write to file.
     */
    public void toDefault() {
        // Copy
        instance = PARSER.parse(standard.toString());
        save();
    }

    /**
     * Read new current instance from file.
     */
    public void load() {
        try {
            if (file.isFile()) {
                instance = PARSER.parse(Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8));
            } else toDefault();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write current instance to file.
     */
    public void save() {
        try {
            Files.write(file.toPath(), Collections.singleton(GSON.toJson(instance)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
