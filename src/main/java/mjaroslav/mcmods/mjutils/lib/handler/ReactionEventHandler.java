package mjaroslav.mcmods.mjutils.lib.handler;

import static mjaroslav.mcmods.mjutils.lib.utils.UtilsReaction.*;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.lib.event.BlockReactionEvent.PigZombieEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class ReactionEventHandler {
    @SubscribeEvent
    public void onBlockHarvest(HarvestDropsEvent event) {
        if (event.world.isRemote || event.harvester == null || event.isSilkTouching)
            return;
        if (checkBlockToPigAngryList(event.block, event.blockMetadata)) {
            PigZombieEvent newEvent = new PigZombieEvent(event.world, event.x, event.y, event.z, event.harvester,
                    event.block, event.blockMetadata);
            if (!MinecraftForge.EVENT_BUS.post(newEvent))
                pigZombiesBecomeAngryInRadius(newEvent.world, newEvent.harvester, newEvent.x, newEvent.y, newEvent.z,
                        32D);
        }
    }
}
