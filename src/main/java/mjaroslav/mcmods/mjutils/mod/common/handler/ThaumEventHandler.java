package mjaroslav.mcmods.mjutils.mod.common.handler;

import static mjaroslav.mcmods.mjutils.util.UtilsThaum.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.util.StringUtils;

public class ThaumEventHandler {
    public static final ThaumEventHandler instance = new ThaumEventHandler();

    public static void newResearchCopy(ResearchCopy copy) {
        instance.researchCopyList.add(copy);
    }

    private ThaumEventHandler() {
    }

    private final Set<ResearchCopy> researchCopyList = new HashSet<>();

    @SubscribeEvent
    public void eventPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (!event.player.worldObj.isRemote)
            for (ResearchCopy researchCopy : researchCopyList)
                if (researchCopy.exist() && isComplete(event.player, researchCopy.getOriginalKey()))
                    complete(event.player, researchCopy.key);
    }

    public static class ResearchCopy {
        private String originalKey;
        private String key;

        public ResearchCopy(String originalKey, String key) {
            this.originalKey = originalKey;
            this.key = key;
        }

        public boolean exist() {
            return !StringUtils.isNullOrEmpty(originalKey) && !StringUtils.isNullOrEmpty(key);
        }

        public String getKey() {
            return key;
        }

        public String getOriginalKey() {
            return originalKey;
        }
    }
}
