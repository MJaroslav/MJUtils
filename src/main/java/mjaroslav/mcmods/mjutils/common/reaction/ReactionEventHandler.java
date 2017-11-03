package mjaroslav.mcmods.mjutils.common.reaction;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class ReactionEventHandler {
	@SubscribeEvent
	public void onBlockHarvest(HarvestDropsEvent event) {
		if (event.world.isRemote || event.harvester == null || event.isSilkTouching)
			return;
		if (ReactionUtils.checkBlockToPigAngryList(event.block, event.blockMetadata))
			ReactionUtils.pigZombiesBecomeAngryInRadius(event.world, event.harvester, event.x, event.y, event.z, 32D);
	}
}
