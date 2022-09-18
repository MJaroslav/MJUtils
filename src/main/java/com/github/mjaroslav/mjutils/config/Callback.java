package com.github.mjaroslav.mjutils.config;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Callback {
    void call(@NotNull Config config);
}
