package com.github.mjaroslav.mjutils.util.io;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ResourcePath {
    @Nonnull
    private final String PATH;

    public ResourcePath(String path) {
        PATH = path.startsWith("/") ? path : "/" + path;
    }

    public ResourcePath(ResourceLocation location) {
        PATH = "/assets/" + location.toString().replace(":", "/");
    }

    @Nullable
    public InputStream stream() {
        return ResourcePath.class.getResourceAsStream(PATH);
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
        return PATH.hashCode();
    }

    @Override
    public String toString() {
        return PATH;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (!(obj instanceof ResourcePath))
            return false;
        else return PATH.equals(((ResourcePath)obj).PATH);
    }

    public static ResourcePath of(@Nonnull String path) {
        return new ResourcePath(path);
    }

    public static ResourcePath of(@Nonnull ResourceLocation location) {
        return new ResourcePath(location);
    }
}
