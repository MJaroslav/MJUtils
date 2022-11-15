package io.github.mjaroslav.mjutils.mod.common.handler;

import io.github.mjaroslav.mjutils.util.game.compat.UtilsThaumcraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThaumEventHandler {
    public static final ThaumEventHandler instance = new ThaumEventHandler();

    public static void newResearchCopy(ResearchCopy copy) {
        instance.researchCopyList.add(copy);
    }

    private final Set<ResearchCopy> researchCopyList = new HashSet<>();

    @SubscribeEvent
    public void eventPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (!event.player.worldObj.isRemote)
            for (ResearchCopy researchCopy : researchCopyList)
                if (researchCopy.exist() && UtilsThaumcraft.isComplete(event.player, researchCopy.getOriginalKey()))
                    UtilsThaumcraft.complete(event.player, researchCopy.key);
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