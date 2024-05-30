package io.github.mjaroslav.mjutils.internal.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.mjaroslav.mjutils.config.PropertiesConfig;
import io.github.mjaroslav.mjutils.lib.MJUtilsInfo;
import io.github.mjaroslav.mjutils.util.UtilsDesktop;
import lombok.Getter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class IDManager {
    public static final String SEPARATOR = "@";

    protected final BiMap<Integer, String> ids = HashBiMap.create();
    protected final Map<String, Integer> repetitions = new HashMap<>();
    protected final Map<Integer, String> comments = new HashMap<>();
    protected final Map<Integer, Set<String>> conflictedIds = new HashMap<>();
    @Getter
    protected final Collection<Integer> ignoredIndexes = new HashSet<>();
    protected final String configComment;

    protected final @NotNull String classSimpleName;
    protected final boolean enabled;
    protected final int arraySize;
    protected final @NotNull OccupiedPolicy policy;
    protected final @NotNull PropertiesConfig config;

    public IDManager(@NotNull String classSimpleName, boolean enabled, int arraySize,
                     @NotNull Collection<Integer> ignoredIndexes, @NotNull OccupiedPolicy policy,
                     @NotNull Path configPath) {
        this.classSimpleName = classSimpleName;
        this.enabled = enabled;
        this.arraySize = arraySize;
        this.ignoredIndexes.addAll(ignoredIndexes);
        this.policy = policy;
        config = new PropertiesConfig(MJUtilsInfo.MOD_ID, configPath);
        configComment = String.format("""
                %1$s provides ID array size extension and adds ID occupied strategy.
                This file used for "AUTO" strategy, then means that all %2$s registrations
                will take new free ID if desired was occupied.

                You can use this for overrides all added %2$s IDs,
                but notice that it will not work for vanilla IDs.

                Pattern: className%3$sN=ID,
                where N is the number of the current repetition
                in additional, it may have comment with name of element.
                N value of each element may change when mod list was edited.

                %2$s array extended to %4$s, you can change it in configuration
                This comment will automatically regenerate on each game load with "AUTO" strategy.""",
            MJUtilsInfo.NAME, classSimpleName, SEPARATOR, arraySize
        );
        config.setComment(configComment);
        initialize();
    }

    public @NotNull String getName(@NotNull Class<?> type) {
        val name = type.getName();
        val repetition = repetitions.getOrDefault(name, 0) + 1;
        repetitions.put(name, repetition);
        return name + SEPARATOR + repetition;
    }

    public int registerId(@NotNull Class<?> type, int desiredId) {
        val name = getName(type);
        if (enabled && !ignoredIndexes.contains(desiredId)) {
            if (policy == OccupiedPolicy.AUTO)
                desiredId = config.getInt(name, desiredId);
            if (ids.containsKey(desiredId)) {
                if (policy == OccupiedPolicy.CRASH_DUMP) {
                    conflictedIds.putIfAbsent(desiredId, new HashSet<>());
                    conflictedIds.get(desiredId).add(name);
                    desiredId = getNextAvailableIdFromEnd();
                }
                if (policy == OccupiedPolicy.CRASH_FIRST)
                    throw new IllegalArgumentException(String.format("Occupied %1$s id %2$s --> %3$s and " +
                            "%4$s",
                        classSimpleName, desiredId, name, ids.get(desiredId)));
                else desiredId = getNextAvailableId();
            }
        }
        ids.put(desiredId, name);
        config.setValue(name, desiredId);
        return desiredId;
    }

    public void setComment(int id, @Nullable String comment) {
        if (StringUtils.isEmpty(comment)) comments.remove(id);
        else comments.put(id, comment);
    }

    public int getNextAvailableId() {
        for (var id = 0; id < arraySize; id++) if (!ignoredIndexes.contains(id) && !ids.containsKey(id)) return id;
        throw new ArrayIndexOutOfBoundsException(String.format("Can't generate ID for %1$s: out of free ids", classSimpleName));
    }

    public int getNextAvailableIdFromEnd() {
        for (var id = arraySize - 1; id > 0; id--) if (!ignoredIndexes.contains(id) && !ids.containsKey(id)) return id;
        throw new ArrayIndexOutOfBoundsException(String.format("Can't generate ID for %1$s: out of free ids", classSimpleName));
    }

    protected void initialize() {
        if (!enabled || policy != OccupiedPolicy.AUTO) return;
        config.load();
    }

    @Contract(" -> new")
    public @NotNull List<@NotNull Integer> getAvailableIds() {
        val result = new ArrayList<Integer>();
        for (var id = 0; id < arraySize; id++) if (!ignoredIndexes.contains(id) && !ids.containsKey(id)) result.add(id);
        return result;
    }

    public @NotNull String getFormattedElement(int id) {
        var result = ids.get(id);
        if (comments.containsKey(id)) result += " (" + comments.get(id) + ")";
        return result;
    }

    public @NotNull String getFormatElement(@NotNull String name) {
        return getFormattedElement(ids.inverse().get(name));
    }

    public void complete() {
        if (!enabled) return;
        if (policy == OccupiedPolicy.AUTO) {
            comments.forEach((id, comment) -> config.setComment(ids.get(id), comment));
            config.setComment(configComment);
            config.save();
            System.out.println(classSimpleName + " id manager saved values to file");
        } else if (policy == OccupiedPolicy.CRASH_DUMP) {
            MJUtilsInfo.LOG_LIB.error("Found conflicted {} ids!", classSimpleName);
            conflictedIds.entrySet().stream().sorted(Comparator.comparingInt(Entry::getKey)).forEach(e ->
                MJUtilsInfo.LOG_LIB.error("Id {} for next {} elements: {}", e.getKey(), classSimpleName,
                    e.getValue().stream().map(this::getFormatElement).collect(Collectors.joining(","))));

            MJUtilsInfo.LOG_LIB.error("Available ids: {}", String.join(",", getRangedAvailableIds()));

            UtilsDesktop.crashGame(new IllegalArgumentException(String.format("%s id conflict", classSimpleName)),
                "See above to more info");
        }
    }

    public @NotNull List<@NotNull String> getRangedAvailableIds() {
        val ranges = new ArrayList<String>();
        val available = getAvailableIds();
        if (available.size() > 1) {
            var begin = available.get(0);
            var prev = begin;
            for (int i = 0; i < available.size() - 1; i++) {
                if (begin - i > 1) {
                    if (begin - prev == 0) ranges.add(String.valueOf(begin));
                    else ranges.add("[" + begin + "-" + prev + "]");
                    begin = i;
                }
                prev = i;
            }
        }
        return ranges;
    }

    public enum OccupiedPolicy {
        CRASH_FIRST, CRASH_DUMP, AUTO
    }
}
