package mjaroslav.mcmods.mjutils.hook;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static mjaroslav.mcmods.mjutils.mod.lib.ModInfo.LOG;
import static mjaroslav.mcmods.mjutils.mod.lib.ModInfo.MOD_ID;

public class HookConfig {
    private static final String DISABLED_HOOKS_FILE = String.format("%s_disabled_hooks.txt", MOD_ID);
    private static final String COMMENT_MARK = "#";
    private static final Set<String> DISABLED_HOOKS = new HashSet<>();
    private static final File CONFIG = new File("config");

    static {
        LOG.info("Trying to load hooks configuration...");
        try {
            for (String line : Files.readAllLines(CONFIG.toPath().resolve(DISABLED_HOOKS_FILE)))
                if (!line.startsWith(COMMENT_MARK))
                    DISABLED_HOOKS.add(line.trim().toLowerCase());
            LOG.info(String.format("Disabled hooks: [%s]", String.join(", ", DISABLED_HOOKS)));
        } catch (IOException e) {
            LOG.error("Failed to load hooks configuration!", e);
            LOG.error("Trying to create default hooks configuration file...");
            String[] defaultConfiguration = new String[]{
                    COMMENT_MARK + " If you want to disable some hooks, just remove the comment mark (" + COMMENT_MARK +
                            ") from its " + "line.",
                    COMMENT_MARK + " Required Minecraft restart.",
                    COMMENT_MARK + " Game changes that are dependent on disabled hooks will be disabled.",
                    COMMENT_MARK,
                    COMMENT_MARK + " " + HooksBlockBreakingCreative.DISABLE_ID,
                    COMMENT_MARK + " " + HooksFishingEvent.DISABLE_ID,
                    COMMENT_MARK + " " + HooksFishingCache.DISABLE_ID,
                    COMMENT_MARK + " " + HooksFishingNullFix.DISABLE_ID
            };
            try {
                if (CONFIG.isDirectory() || CONFIG.mkdirs()) {
                    Files.write(CONFIG.toPath().resolve(DISABLED_HOOKS_FILE), Arrays.asList(defaultConfiguration));
                    LOG.info("Default hooks configuration created!");
                } else throw new IOException("Can not create config folder");
            } catch (IOException ex) {
                LOG.error("Can not generate default hooks configuration!", ex);
            }
        }
    }

    private static boolean hookIsEnabled(String name) {
        return !DISABLED_HOOKS.contains(name);
    }

    public static boolean blockBreakingCreative() {
        return hookIsEnabled(HooksBlockBreakingCreative.DISABLE_ID);
    }

    public static boolean fishingEvent() {
        return hookIsEnabled(HooksFishingEvent.DISABLE_ID);
    }

    public static boolean fishingCache() {
        return hookIsEnabled(HooksFishingCache.DISABLE_ID);
    }

    public static boolean fishingNullFix() {
        return hookIsEnabled(HooksFishingNullFix.DISABLE_ID);
    }
}
