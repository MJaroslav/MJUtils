package io.github.mjaroslav.mjutils.mod.common.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.mjaroslav.mjutils.event.PigZombiesGreedEvent;
import io.github.mjaroslav.mjutils.mod.lib.General;
import io.github.mjaroslav.mjutils.util.game.UtilsInteractions;
import io.github.mjaroslav.mjutils.util.game.world.UtilsWorld;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockEventListener {
    public static final BlockEventListener INSTANCE = new BlockEventListener();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockHarvest(@NotNull HarvestDropsEvent event) {
        System.out.println("Pre");
        if (event.world.isRemote || event.harvester == null || (General.silkMakePigsBlind && event.isSilkTouching))
            return;
        System.out.println("Post");
        if (UtilsInteractions.isBlockPigZombieGreedTrigger(event.block, event.blockMetadata)) {
            val delegateEvent = new PigZombiesGreedEvent(event.x, event.y, event.z, event.world, event.block,
                event.blockMetadata, event.harvester);
            System.out.println("PrePig");
            if (!MinecraftForge.EVENT_BUS.post(delegateEvent))
                UtilsWorld.pigZombiesBecomeAngryInRadius(delegateEvent.world, delegateEvent.triggerEntity,
                    delegateEvent.x, delegateEvent.y, delegateEvent.z, 32D);
            System.out.println("PostPig");
        }
    }
}
