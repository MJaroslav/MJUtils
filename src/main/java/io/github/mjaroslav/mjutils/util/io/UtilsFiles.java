package io.github.mjaroslav.mjutils.util.io;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.stream.Stream;

/**
 * Set of wrappers for getting and using {@link Path} as "path/filename/file" parameters in other places.
 */
@UtilityClass
public class UtilsFiles {
    /**
     * Wrapper for {@link Paths#get(String, String...)} that use {@link UtilsFiles#normalizePath(Object)}
     * as first parameter.
     *
     * @return Normalized and absolute {@link Path} from first parameter with resolved childs or
     * null if type of first parameter not supported in {@link UtilsFiles#normalizePath(Object)}.
     * @see Paths#get(String, String...)
     * @see UtilsFiles#normalizePath(Object)
     */
    public @UnknownNullability Path get(@NotNull Object value, @NotNull String @NotNull ... childs) {
        var result = normalizePath(value);
        if (result != null) {
            for (var child : childs)
                result = result.resolve(child);
            return result/*.normalize().toAbsolutePath()*/; // I'm not sure in this
        } else return null;
    }

    /**
     * Wrapper for {@link FilenameUtils#removeExtension(String)} that use {@link Path} as filename.
     *
     * @see FilenameUtils#removeExtension(String)
     */
    public @NotNull String removeExtension(@NotNull Path file) {
        return FilenameUtils.removeExtension(file.getFileName().toString());
    }

    /**
     * Replaces file extension.
     *
     * @param file         file for extension replacing.
     * @param newExtension new file extension.
     * @return old extension or null on error when replacing.
     */
    public @Nullable String replaceExtension(@NotNull Path file, @NotNull String newExtension) {
        val ext = getExtension(file);
        val to = get(file.getParent(), removeExtension(file) + "." + newExtension);
        if (move(file, to) == null)
            return null;
        return ext;
    }

    /**
     * Wrapper for {@link Files#list(Path)} that return empty stream instead of {@link IOException}.
     *
     * @see Files#list(Path)
     */
    public @NotNull Stream<Path> list(@NotNull Path path) {
        try {
            return Files.list(path);
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    /**
     * Wrapper for {@link FilenameUtils#getExtension(String)} that use {@link Path} as filename.
     *
     * @see FilenameUtils#getExtension(String)
     */
    public @NotNull String getExtension(@NotNull Path path) {
        return FilenameUtils.getExtension(path.getFileName().toString());
    }

    /**
     * Wrapper for {@link FilenameUtils#isExtension(String, String[])} that use {@link Path} as filename
     * and not case-sensitive.
     *
     * @see FilenameUtils#isExtension(String, String[])
     */
    @Contract("null, _ -> false")
    public boolean isExtension(@Nullable Path path, @NotNull String @NotNull ... extensions) {
        if (path == null)
            return false;
        val ext = getExtension(path);
        for (var check : extensions)
            if (ext.equalsIgnoreCase(check))
                return true;
        return false;
    }

    /**
     * Makes normalized and absolute {@link Path} from some types in parameter.
     *
     * @param source source for normalize, supported types: {@link String}, {@link Path},
     *               {@link File}, {@link URL} (with jar or file protocols) and {@link URI}.
     * @return normalized absolute {@link Path} of parameter or null if its is null or not supported.
     * @see Path#normalize()
     * @see Path#toAbsolutePath()
     */
    @Contract("null -> null")
    public @UnknownNullability Path normalizePath(@Nullable Object source) {
        if (source instanceof String string)
            return Paths.get(string).normalize().toAbsolutePath();
        else if (source instanceof File file)
            return file.toPath().normalize().toAbsolutePath();
        else if (source instanceof Path path)
            return path.normalize().toAbsolutePath();
        else if (source instanceof URL url) {
            switch (url.getProtocol()) {
                case "jar": {
                    try {
                        val connection = (JarURLConnection) url.openConnection();
                        return Paths.get(connection.getJarFileURL().getFile()).normalize().toAbsolutePath();
                    } catch (IOException ignored) {
                        return null;
                    }
                }
                case "file":
                    return Paths.get(url.getFile()).normalize().toAbsolutePath();
                default: // switch used for new possible required protocols 
                    return null;
            }
        } else if (source instanceof URI uri)
            return Paths.get(uri).normalize().toAbsolutePath();
        else return null;
    }

    /**
     * Wrapper for {@link UtilsFiles#move(Path, Path, CopyOption...)} that use
     * {@link UtilsFiles#normalizePath(Object)} for arguments with {@link Path} type.
     *
     * @see UtilsFiles#move(Path, Path, CopyOption...)
     * @see Files#move(Path, Path, CopyOption...)
     */
    @Contract("null, _, _ -> null; _, null, _ -> null")
    public @Nullable Path move(@Nullable Object from, @Nullable Object to, @NotNull CopyOption @NotNull ... options) {
        return move(normalizePath(from), normalizePath(to), options);
    }

    /**
     * Wrapper for {@link Files#move(Path, Path, CopyOption...)} that return null on exception.
     *
     * @see Files#move(Path, Path, CopyOption...)
     */
    @Contract("null, _, _ -> null; _, null, _ -> null")
    public @Nullable Path move(@Nullable Path from, @Nullable Path to, @NotNull CopyOption @NotNull ... options) {
        if (from != null && to != null)
            try {
                return Files.move(from, to, options);
            } catch (IOException ignored) {
            }
        return null;
    }

    /**
     * Wrapper for {@link UtilsFiles#createDirectories(Path, FileAttribute[])} that use
     * {@link UtilsFiles#normalizePath(Object)} for arguments with {@link Path} type.
     *
     * @see UtilsFiles#createDirectories(Path, FileAttribute[])
     * @see Files#createDirectories(Path, FileAttribute[])
     */
    public @Nullable Path createDirectories(@Nullable Object file, @NotNull FileAttribute<?> @NotNull ... attrs) {
        return createDirectories(normalizePath(file), attrs);
    }

    /**
     * Wrapper for {@link Files#createDirectories(Path, FileAttribute[])} that return null on exception.
     *
     * @see Files#createDirectories(Path, FileAttribute[])
     */
    public @Nullable Path createDirectories(@Nullable Path file, @NotNull FileAttribute<?> @NotNull ... attrs) {
        if (file != null)
            try {
                return Files.createDirectories(file, attrs);
            } catch (IOException ignored) {
            }
        return null;
    }
}
