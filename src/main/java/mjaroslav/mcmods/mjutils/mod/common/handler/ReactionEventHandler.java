package mjaroslav.mcmods.mjutils.mod.common.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.object.event.BlockReactionEvent.PigmanTriggerEvent;
import mjaroslav.mcmods.mjutils.util.UtilsInteractions;
import mjaroslav.mcmods.mjutils.util.UtilsWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class ReactionEventHandler {
    public static final ReactionEventHandler instance = new ReactionEventHandler();

    private ReactionEventHandler() {
    }

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
