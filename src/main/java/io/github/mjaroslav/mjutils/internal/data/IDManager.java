package io.github.mjaroslav.mjutils.internal.data;

import io.github.mjaroslav.mjutils.asm.MixinPatches.OccupiedPolicy;
import io.github.mjaroslav.mjutils.config.PropertiesConfig;
import io.github.mjaroslav.mjutils.lib.ModInfo;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IDManager {
    protected final Map<Integer, String> cache = new HashMap<>();

    protected final @NotNull String classSimpleName;
    protected final boolean enabled;
    protected final int arraySize;
    protected final Collection<Integer> vanillaIndexes;
    protected final @NotNull OccupiedPolicy policy;
    protected final @NotNull PropertiesConfig config;

    public IDManager(@NotNull String classSimpleName, boolean enabled, int arraySize,
                     @NotNull Collection<Integer> vanillaIndexes, @NotNull OccupiedPolicy policy, @NotNull Path configPath) {
        this.classSimpleName = classSimpleName;
        this.enabled = enabled;
        this.arraySize = arraySize;
        this.vanillaIndexes = vanillaIndexes;
        this.policy = policy;
        config = new PropertiesConfig(ModInfo.MOD_ID, configPath);
        config.setComment(String.format("""
                MJUtils provides ID array size extension and adds ID occupied strategy.
                This file used for "AUTO" strategy, then means that all %1$s registrations
                will take new free ID if desired was occupied.
                            
                You can use this for overrides all added %1$s IDs,
                but notice that it will not work for vanilla IDs.
                            
                Pattern: className=ID
                            
                %1$s array extended to %2$s, you can change it in configuration
                This comment will automatically regenerate on each game load with "AUTO" strategy.""",
            classSimpleName, arraySize
        ));
    }

    public int registerID(@NotNull Class<?> type, int desiredId) {
        System.out.printf("Trying register %s %s with ID %s%n", classSimpleName, type.getName(), desiredId);
        if (policy == OccupiedPolicy.IGNORE || vanillaIndexes.contains(desiredId)) return desiredId; // Vanilla IDs
        val name = type.getName();
        if (policy == OccupiedPolicy.AUTO) {
            val id = verifyID(name, config.getInt(name, desiredId), true, false);
            config.setValue(name, id);
            return id;
        } else return verifyID(name, config.getInt(name, desiredId), true, false);
    }

    public int verifyID(@NotNull String type, int desiredId, boolean throwException, boolean dryRun) {
        System.out.printf("Trying verify %s %s with ID %s%n", classSimpleName, type, desiredId);
        if (cache.containsKey(desiredId)) {
            if (policy == OccupiedPolicy.AUTO) {
                val id = getNextAvailableID();
                if (!dryRun) cache.put(desiredId, type);
                return id;
            }
            if (throwException)
                throw new IllegalArgumentException(String.format("Occupied %1$s ID %2$s --> %3$s and %4$s",
                    classSimpleName, desiredId, type, cache.get(desiredId)));
        }
        if (!dryRun) cache.put(desiredId, type);
        return desiredId;
    }

    public int getNextAvailableID() {
        System.out.printf("Trying get next available ID for %s%n", classSimpleName);
        for (var id = 0; id < arraySize; id++) if (!vanillaIndexes.contains(id) && !cache.containsKey(id)) return id;
        throw new ArrayIndexOutOfBoundsException(String.format("Can't generate ID for %1$s: out of free ids", classSimpleName));
    }

    public void loadIDsFromFileIfEnabled() {
        if (!enabled || policy != OccupiedPolicy.AUTO) return;
        config.load();
    }

    public void saveIDsToFileIfEnabled() {
        if (!enabled || policy != OccupiedPolicy.AUTO) return;
        config.save();
    }
}
