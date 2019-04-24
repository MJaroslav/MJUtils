package mjaroslav.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Reader (wrapper) for JSON files.
 *
 * @author MJaroslav
 */
public class JSONReader<T> {
    /**
     * JSON object
     */
    public T json;
    /**
     * Default JSON object.
     */
    public T defaults;

    /**
     * JSON file path.
     */
    private String filePath;
    /**
     * JSON file.
     */
    private File file;
    /**
     * JSON file folder.
     */
    private File folder;

    private Gson gson;

    /**
     * @param object
     *            - JSON object (default).
     * @param file
     *            - JSON file.
     * @param isPretty
     *            - pretty syntax of JSON string.
     */
    public JSONReader(T object, File file, boolean isPretty) {
        this.json = object;
        this.defaults = object;
        this.file = file;
        this.filePath = file.getAbsolutePath();
        this.folder = getFolder(file);
        if (isPretty)
            gson = new GsonBuilder().setPrettyPrinting().create();
        else
            gson = new GsonBuilder().create();
    }

    /**
     * Get folder from file.
     *
     * @param file
     *            - file.
     * @return Folder of file.
     */
    public static File getFolder(File file) {
        String filePath = file.getAbsolutePath();
        return new File(filePath.substring(0, filePath.length() - (file.getName().length() + 1)));
    }

    /**
     * Set default value of JSON object.
     *
     * @return True if done.
     */
    public boolean toDefault() {
        json = defaults;
        return write();
    }

    /**
     * Initialization of reader. Creating or/and reading JSON file.
     *
     * @return True of done.
     */
    public boolean init() {
        file = new File(filePath);
        folder = getFolder(file);
        if (!folder.exists() || !folder.isDirectory())
            folder.mkdirs();
        if (!file.exists() || !file.isFile()) {
            try {
                file.createNewFile();
                toDefault();
                return write();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return read();
    }

    /**
     * Read JSON file.
     *
     * @return True if done.
     */
    public boolean read() {
        try {
            Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
            json = gson.fromJson(reader, (Class<T>) json.getClass());
            reader.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Write to JSON file.
     *
     * @return True if done.
     */
    public boolean write() {
        try {
            Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
            gson.toJson(json, writer);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get reader JSON file.
     *
     * @return JSON file.
     */
    public File getFile() {
        return file;
    }

    /**
     * Set JSON file.
     *
     * @param file
     *            - new file.
     * @return JSONReader with new file.
     */
    public JSONReader<T> setFile(File file) {
        this.file = file;
        this.filePath = file.getAbsolutePath();
        this.folder = getFolder(file);
        return this;
    }

    /**
     * Get reader folder.
     *
     * @return JSON file folder.
     */
    public File getFolder() {
        return folder;
    }

    /**
     * Set new GSON.
     *
     * @param gson
     *            - new gson.
     * @return This reader with new gson.
     */
    public JSONReader<T> setGson(Gson gson) {
        this.gson = gson;
        return this;
    }
}
