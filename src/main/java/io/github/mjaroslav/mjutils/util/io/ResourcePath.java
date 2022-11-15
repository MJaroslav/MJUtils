package io.github.mjaroslav.mjutils.util.io;

import lombok.Getter;
import lombok.ToString;
import lombok.val;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Getter
@ToString
public final class ResourcePath {
    @NotNull
    private final String path;
    @NotNull
    private final String namespace;
    @Getter
    private final boolean assetsPath;

    private ResourcePath(@NotNull String path, boolean fullPath) {
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

    private ResourcePath(@NotNull ResourceLocation location) {
        path = "/assets/" + location.toString().replace(":", "/");
        namespace = location.toString().split(":")[0];
        assetsPath = true;
    }

    public InputStream stream() {
        return ResourcePath.class.getResourceAsStream(path);
    }

    @UnknownNullability
    public InputStreamReader reader() {
        return reader(StandardCharsets.UTF_8);
    }

    @UnknownNullability
    public BufferedReader bufferedReader() {
        return bufferedReader(StandardCharsets.UTF_8);
    }

    @UnknownNullability
    public InputStreamReader reader(Charset charset) {
        val io = stream();
        if (io == null) return null;
        else return new InputStreamReader(io, charset);
    }

    @UnknownNullability
    public BufferedReader bufferedReader(Charset charset) {
        val io = stream();
        if (io == null) return null;
        else return new BufferedReader(new InputStreamReader(io, charset));
    }

    @Contract(" -> new")
    public @NotNull ResourceLocation toLocation() {
        return new ResourceLocation(path.replace("/assets/" + namespace + "/", namespace + ":"));
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

    @Contract("_ -> new")
    public static @NotNull ResourcePath of(@NotNull String path) {
        return new ResourcePath(path, false);
    }

    @Contract("_, _ -> new")
    public static @NotNull ResourcePath of(@NotNull String namespace, @NotNull String assetsPath) {
        return new ResourcePath(namespace + ":" + assetsPath, false);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ResourcePath full(@NotNull String fullPath) {
        return new ResourcePath(fullPath, true);
    }

    @Contract("_ -> new")
    public static @NotNull ResourcePath of(@NotNull ResourceLocation location) {
        return new ResourcePath(location);
    }
}
