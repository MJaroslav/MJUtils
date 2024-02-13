package io.github.mjaroslav.mjutils.util.object.game;

import cpw.mods.fml.common.ModContainer;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.val;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Special wrapper for working with resources. This allows you get input stream or reader from this path.
 */
// TODO: Add resource pack support for clients
@Getter
@ToString
public final class ResourcePath {
    /**
     * Absolute path.
     */
    @NotNull
    private final String path;
    /**
     * Assets domain if it is assets path.
     */
    @NotNull
    private final String namespace;
    /**
     * True if is path in /assets/.
     */
    @Getter
    private final boolean assetsPath;

    @Nullable
    @Getter
    private final ModContainer source;

    @Getter
    private final boolean searchInAllSources;

    private ResourcePath(@NotNull String path, boolean fullPath, @Nullable ModContainer source, boolean searchInAllSources) {
        this.searchInAllSources = searchInAllSources;
        this.source = source;
        if (fullPath) {
            this.path = path.startsWith("/") ? path : "/" + path;
            if (this.path.startsWith("/assets/")) { // Auto assets path detecting
                val info = this.path.split("/");
                if (info.length > 3) {
                    assetsPath = true;
                    namespace = info[2];
                    return;
                }
            }
            namespace = "minecraft";
            assetsPath = false;
        } else {
            val info = path.split(":");
            if (info.length == 1) namespace = "minecraft";
            else namespace = info[0];
            this.path = "/assets/" + namespace + "/" + info[info.length > 1 ? 1 : 0];
            assetsPath = true;
        }
    }

    private ResourcePath(@NotNull ResourceLocation location, @Nullable ModContainer source, boolean searchInAllSources) {
        path = "/assets/" + location.toString().replace(":", "/");
        namespace = location.toString().split(":")[0];
        assetsPath = true;
        this.source = source;
        this.searchInAllSources = searchInAllSources;
    }

    /**
     * New assets ResourcePath from string in ResourceLocation format.
     *
     * @param path "domain:path" string or "path" for "minecraft" domain.
     * @return new ResourcePath object with specified path.
     */
    @Contract("_ -> new")
    public static @NotNull ResourcePath of(@NotNull String path) {
        return new ResourcePath(path, false, null, true);
    }

    /**
     * New InputStreamReader from this resource path with UTF-8 charset.
     *
     * @return new InputStreamReader of this resource path.
     */
    @Contract(" -> new")
    public @NotNull InputStreamReader reader() {
        return reader(StandardCharsets.UTF_8);
    }

    /**
     * New BufferedReader from this resource path with UTF-8 charset.
     *
     * @return new BufferedReader of this resource path.
     */
    @Contract(" -> new")
    public @NotNull BufferedReader bufferedReader() {
        return bufferedReader(StandardCharsets.UTF_8);
    }

    /**
     * Create new InputStreamReader from this resource path.
     *
     * @param charset charset for reader.
     * @return new InputStreamReader of this resource path.
     */
    @Contract("_ -> new")
    public @NotNull InputStreamReader reader(@NotNull Charset charset) {
        return new InputStreamReader(stream(), charset);
    }

    @Contract("_, _ -> new")
    public static @NotNull ResourcePath of(@NotNull String path, @NotNull ModContainer source) {
        return new ResourcePath(path, false, source, false);
    }

    /**
     * Convert this ResourcePath to minecraft ResourceLocation.
     *
     * @return new ResourceLocation with converted path.
     * @throws IllegalStateException when trying to convert non-assets ResourcePath.
     */
    @Contract(" -> new")
    public @NotNull ResourceLocation toLocation() throws IllegalStateException {
        if (!isAssetsPath())
            throw new IllegalStateException("Trying to create ResourceLocation from non-assets ResourcePath");
        return new ResourceLocation(path.replace("/assets/" + namespace + "/", namespace + ":"));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull ResourcePath of(@NotNull String path, @Nullable ModContainer source, boolean searchInAllSources) {
        return new ResourcePath(path, false, source, searchInAllSources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, namespace);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof ResourcePath resource && path.equals(resource.path) && namespace.equals(resource.namespace);
    }

    /**
     * New assets ResourcePath from domain and path strings.
     *
     * @param namespace  assets domain.
     * @param assetsPath assets relative path.
     * @return new ResourcePath object with specified path.
     */
    @Contract("_, _ -> new")
    public static @NotNull ResourcePath of(@NotNull String namespace, @NotNull String assetsPath) {
        return new ResourcePath(namespace + ":" + assetsPath, false, null, true);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull ResourcePath of(@NotNull String namespace, @NotNull String assetsPath, @NotNull ModContainer source) {
        return new ResourcePath(namespace + ":" + assetsPath, false, source, false);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull ResourcePath of(@NotNull String namespace, @NotNull String assetsPath, @Nullable ModContainer source, boolean searchInAllSources) {
        return new ResourcePath(namespace + ":" + assetsPath, false, source, searchInAllSources);
    }

    /**
     * New full path ResourcePath.
     *
     * @param fullPath full resource path.
     * @return new ResourcePath object with specified path.
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ResourcePath full(@NotNull String fullPath) {
        return new ResourcePath(fullPath, true, null, true);
    }

    public static @NotNull ResourcePath full(@NotNull String fullPath, @NotNull ModContainer source) {
        return new ResourcePath(fullPath, true, source, false);
    }

    public static @NotNull ResourcePath full(@NotNull String fullPath, @Nullable ModContainer source, boolean searchInAllSources) {
        return new ResourcePath(fullPath, true, source, searchInAllSources);
    }

    /**
     * Convert ResourceLocation to ResourcePath.
     *
     * @param location ResourceLocation object.
     * @return new ResourcePath from this ResourceLocation.
     */
    @Contract("_ -> new")
    public static @NotNull ResourcePath of(@NotNull ResourceLocation location) {
        return new ResourcePath(location, null, true);
    }

    @Contract("_, _ -> new")
    public static @NotNull ResourcePath of(@NotNull ResourceLocation location, @NotNull ModContainer source) {
        return new ResourcePath(location, source, false);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull ResourcePath of(@NotNull ResourceLocation location, @Nullable ModContainer source, boolean searchInAllSources) {
        return new ResourcePath(location, source, searchInAllSources);
    }

    /**
     * Get input stream of this resource from current class loader.
     *
     * @return input stream of this resource.
     * @see Class#getResourceAsStream(String)
     */
    @SneakyThrows // TODO: Remove it with try catch or something more elegant
    @Contract(" -> new")
    public InputStream stream() {
        if (!searchInAllSources && source == null) throw new IOException();
        return source == null ? ResourcePath.class.getResourceAsStream(path)
            : UtilsMods.getResourceFromMod(source, path, false);
    }

    /**
     * Create new BufferedReader from this resource path.
     *
     * @param charset charset for reader.
     * @return new BufferedReader of this resource path.
     */
    @Contract("_ -> new")
    public @NotNull BufferedReader bufferedReader(@NotNull Charset charset) {
        return new BufferedReader(new InputStreamReader(stream(), charset));
    }

    public @NotNull String makeUnique() {
        return source == null ? searchInAllSources ? path : "UNKNOWN@" + path : searchInAllSources ? path
            : source.getModId() + "@" + path;
    }
}
