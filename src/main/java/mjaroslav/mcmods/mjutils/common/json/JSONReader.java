package mjaroslav.mcmods.mjutils.common.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Reader for JSON files.
 * 
 * @author MJaroslav
 */
public class JSONReader<T> {
	/** JSON object/ */
	public T json;
	/** Default JSON object. */
	private T defaults;
	/** Class of JSON object. */
	private Class<T> clazz;

	/** JSON file path. */
	private String filePath;
	/** JSON file. */
	private File file;
	/** JSON file folder. */
	private File folder;

	private Gson gson;

	/**
	 * @param object
	 *            - JSON object (default).
	 * @param clazz
	 *            - JSON object class.
	 * @param file
	 *            - JSON file.
	 * @param isPretty
	 *            - pretty syntax of JSON string.
	 */
	public JSONReader(T object, Class<T> clazz, File file, boolean isPretty) {
		this.clazz = clazz;
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
	 * Set default value of JSON object.
	 * 
	 * @return True if done.
	 */
	public boolean toDefault() {
		json = defaults;
		return write();
	}

	/**
	 * Set new default JSON object.
	 */
	public JSONReader setNewDefault(T newValue) {
		this.defaults = newValue;
		return this;
	}

	/**
	 * Initialization of reader. Creating or reading JSON file.
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
			Reader reader = new InputStreamReader(new FileInputStream(file.getAbsolutePath()), StandardCharsets.UTF_8);
			json = gson.fromJson(reader, clazz);
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
			Writer writer = new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()),
					StandardCharsets.UTF_8);
			gson.toJson(json, writer);
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @return Default JSON object.
	 */
	public T getDefaults() {
		return defaults;
	}

	/**
	 * @return JSON file.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @return JSON file folder.
	 */
	public File getFolder() {
		return folder;
	}

	/**
	 * Set JSON file.
	 */
	public JSONReader setFile(File file) {
		this.file = file;
		this.filePath = file.getAbsolutePath();
		this.folder = getFolder(file);
		return this;
	}

	/**
	 * Set new class of JSON object.
	 */
	public JSONReader setClazz(Class<T> clazz) {
		this.clazz = clazz;
		return this;
	}

	/**
	 * Set new GSON.
	 */
	public JSONReader setGson(Gson gson) {
		this.gson = gson;
		return this;
	}

	/**
	 * @return Folder of file.
	 */
	public static File getFolder(File file) {
		String filePath = file.getAbsolutePath();
		return new File(filePath.substring(0, filePath.length() - (file.getName().length() + 1)));
	}
}
