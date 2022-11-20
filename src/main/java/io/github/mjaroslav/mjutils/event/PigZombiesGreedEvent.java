package io.github.mjaroslav.mjutils.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Calls when something destroy block from {@link io.github.mjaroslav.mjutils.util.game.UtilsInteractions
 * UtilsInteractions} in the Nether.
 */
@Cancelable
public class PigZombiesGreedEvent extends BlockEvent {
    /**
     * Entity that broke block, can be null for breaks by explosions and something.
     */
    public final @Nullable EntityLivingBase triggerEntity;

    public PigZombiesGreedEvent(int x, int y, int z, @NotNull World world, @NotNull Block block, int blockMetadata,
                                @Nullable EntityLivingBase triggerEntity) {
        super(x, y, z, world, block, blockMetadata);
        this.triggerEntity = triggerEntity;
    }
}
