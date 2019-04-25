package mjaroslav.mcmods.mjutils.hook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static mjaroslav.mcmods.mjutils.mod.lib.ModInfo.LOG;

public class HookConfig {
    private static final String DISABLED_HOOKS_FILE = "disabled_hooks_mjutils.txt";
    private static final String COMMENT_MARK = "#";
    private static final Set<String> DISABLED_HOOKS = new HashSet<>();

    static {
        LOG.info("Trying to load hooks configuration...");
        try {
            for (String line : Files.readAllLines(Paths.get(DISABLED_HOOKS_FILE)))
                if (!line.startsWith(COMMENT_MARK))
                    DISABLED_HOOKS.add(line.trim().toLowerCase());
        } catch (IOException e) {
            LOG.error("Failed to load hooks configuration!", e);
            LOG.error("Trying to create default hooks configuration file...");
            String[] defaultConfiguration = new String[]{
                    "# If you want to disable some hooks, just remove the comment mark (#) from its line.",
                    "# Required Minecraft restart.",
                    "# Game changes that are dependent on disabled hooks will be disabled.",
                    "#",
                    COMMENT_MARK + " " + HooksBlockBreakingCreative.DISABLE_ID,
                    COMMENT_MARK + " " + HooksFishing.DISABLE_ID,
            };
            try {
                Files.write(Paths.get(DISABLED_HOOKS_FILE), Arrays.asList(defaultConfiguration));
                LOG.info("Default hooks configuration created!");
            } catch (IOException ex) {
                LOG.error("Can not generate default hooks configuration!", ex);
            }
        }
    }

    public static boolean hookIsEnabled(String name) {
        return !DISABLED_HOOKS.contains(name);
    }

    public static boolean blockBreakingCreative() {
        return hookIsEnabled(HooksBlockBreakingCreative.DISABLE_ID);
    }

    public static boolean fishing() {
        return hookIsEnabled(HooksFishing.DISABLE_ID);
    }
}
