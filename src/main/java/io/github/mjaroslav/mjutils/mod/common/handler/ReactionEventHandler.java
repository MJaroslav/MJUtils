package io.github.mjaroslav.mjutils.mod.common.handler;

import io.github.mjaroslav.mjutils.util.game.UtilsInteractions;
import io.github.mjaroslav.mjutils.util.game.world.UtilsWorld;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.mjaroslav.mjutils.object.game.event.BlockReactionEvent.PigmanTriggerEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReactionEventHandler {
    public static final ReactionEventHandler instance = new ReactionEventHandler();

    @SubscribeEvent
    public void onBlockHarvest(HarvestDropsEvent event) {
        if (event.world.isRemote || event.harvester == null || event.isSilkTouching)
            return;
        if (UtilsInteractions.blockIsPigmanTrigger(event.block, event.blockMetadata)) {
            PigmanTriggerEvent newEvent = new PigmanTriggerEvent(event.world, event.x, event.y, event.z, event.harvester,
                    event.block, event.blockMetadata);
            if (!MinecraftForge.EVENT_BUS.post(newEvent))
                UtilsWorld.pigZombiesBecomeAngryInRadius(newEvent.world, newEvent.harvester, newEvent.x, newEvent.y,
                        newEvent.z, 32D);
        }
    }
}