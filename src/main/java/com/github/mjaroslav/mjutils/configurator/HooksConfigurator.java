package com.github.mjaroslav.mjutils.configurator;

import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class HooksConfigurator extends PropertiesConfigurator {
    @Nonnull
    @Getter
    private Set<String> enabledHooks;

    public HooksConfigurator(@Nonnull String modId, @Nonnull ResourcePath defaultPath) {
        super("hooks/" + modId, defaultPath);
        enabledHooks = Collections.emptySet();
    }

    @Override
    public void sync() {
        enabledHooks = properties.entrySet().stream().filter(entry -> !entry.getKey().equals("config_version"))
                .filter(entry -> Boolean.parseBoolean((String) entry.getValue())).map(entry -> (String) entry.getKey()).collect(Collectors.toSet());
    }

    public boolean isHookEnabled(String hook) {
        return enabledHooks.contains(hook);
    }
}