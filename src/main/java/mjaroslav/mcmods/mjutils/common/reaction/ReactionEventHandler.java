package mjaroslav.mcmods.mjutils.common.reaction;

import static mjaroslav.mcmods.mjutils.common.reaction.UtilsReaction.*;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class ReactionEventHandler {
    @SubscribeEvent
    public void onBlockHarvest(HarvestDropsEvent event) {
        if (event.world.isRemote || event.harvester == null || event.isSilkTouching)
            return;
        if (checkBlockToPigAngryList(event.block, event.blockMetadata)) {
            AngryPigZombieByBlockBreakingEvent newEvent = new AngryPigZombieByBlockBreakingEvent(event.block, event.blockMetadata,
                    event.harvester, event.x, event.y, event.z, event.world);
            if (MinecraftForge.EVENT_BUS.post(newEvent))
                return;
            pigZombiesBecomeAngryInRadius(newEvent.world, newEvent.player, newEvent.x, newEvent.y, newEvent.z, 32D);
        }
    }
}
