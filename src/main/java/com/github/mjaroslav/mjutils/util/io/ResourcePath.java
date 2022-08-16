package com.github.mjaroslav.mjutils.util.io;

import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

@Getter
public final class ResourcePath {
    @Nonnull
    private final String path;

    @Nonnull
    private final String packId;

    @Getter
    private final boolean assetsPath;

    private ResourcePath(@Nonnull String path) {
        String[] info = path.split(":");
        System.out.println(Arrays.toString(info));
        if (info.length == 1)
            packId = "minecraft";
        else
            packId = info[0];
        this.path = "/assets/" + packId + "/" + info[info.length > 1 ? 1 : 0];
        assetsPath = true;
    }

    private ResourcePath(@Nonnull ResourceLocation location) {
        path = "/assets/" + location.toString().replace(":", "/");
        packId = location.toString().split(":")[0];
        assetsPath = true;
    }

    private ResourcePath(@Nonnull String modId, @Nonnull String fullPath) {
        packId = modId;
        path = fullPath;
        assetsPath = false;
    }

    @Nullable
    public InputStream stream() {
        return ResourcePath.class.getResourceAsStream(path);
    }

    @Nullable
    public InputStreamReader reader() {
        InputStream io = stream();
        if (io == null)
            return null;
        else
            return new InputStreamReader(io);
    }

    @Nullable
    public BufferedReader bufferedReader() {
        InputStream io = stream();
        if (io == null)
            return null;
        else
            return new BufferedReader(new InputStreamReader(io));
    }

    @Nullable
    public InputStreamReader reader(Charset charset) {
        InputStream io = stream();
        if (io == null)
            return null;
        else
            return new InputStreamReader(io, charset);
    }

    @Nullable
    public BufferedReader bufferedReader(Charset charset) {
        InputStream io = stream();
        if (io == null)
            return null;
        else
            return new BufferedReader(new InputStreamReader(io, charset));
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (!(obj instanceof ResourcePath))
            return false;
        else return path.equals(((ResourcePath) obj).path);
    }

    public static ResourcePath of(@Nonnull String path) {
        return new ResourcePath(path);
    }

    public static ResourcePath of(@Nonnull String modId, @Nonnull String fullPath) {
        return new ResourcePath(modId, fullPath);
    }

    public static ResourcePath of(@Nonnull ResourceLocation location) {
        return new ResourcePath(location);
    }
}
