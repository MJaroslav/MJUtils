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

public class JSONReader<T> {
	public T json;
	private T defaults;
	private Class<T> clazz;

	private String filePath;
	private File file;
	private File folder;

	private Gson gson;

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

	public boolean toDefault() {
		json = defaults;
		return write();
	}

	public JSONReader setNewDefault(T newValue) {
		this.defaults = newValue;
		return this;
	}

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

	public T getDefaults() {
		return defaults;
	}

	public File getFile() {
		return file;
	}

	public File getFolder() {
		return folder;
	}

	public JSONReader setFile(File file) {
		this.file = file;
		this.filePath = file.getAbsolutePath();
		this.folder = getFolder(file);
		return this;
	}

	public JSONReader setClazz(Class<T> clazz) {
		this.clazz = clazz;
		return this;
	}

	public JSONReader setGson(Gson gson) {
		this.gson = gson;
		return this;
	}

	public static File getFolder(File file) {
		String filePath = file.getAbsolutePath();
		return new File(filePath.substring(0, filePath.length() - (file.getName().length() + 1)));
	}
}
