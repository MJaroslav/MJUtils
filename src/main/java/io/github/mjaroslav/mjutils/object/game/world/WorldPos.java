package io.github.mjaroslav.mjutils.object.game.world;

import io.github.mjaroslav.mjutils.object.Trio.NumberTrio;
import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class WorldPos {
    public Block getBlock(@NotNull IBlockAccess owner, @NotNull NumberTrio<?> pos) {
        return owner.getBlock(pos.getZ().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int getBlockMetadata(@NotNull IBlockAccess owner, @NotNull NumberTrio<?> pos) {
        return owner.getBlockMetadata(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean setBlock(@NotNull World owner, @NotNull NumberTrio<?> pos, @NotNull Block block) {
        return owner.setBlock(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block);
    }

    public void playSoundEffect(@NotNull World owner, @NotNull NumberTrio<?> pos, @NotNull String name, float volume,
                                float pitch) {
        owner.playSoundEffect(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(), name,
                volume, pitch);
    }

    public void spawnParticle(@NotNull World owner, @NotNull String name, @NotNull NumberTrio<?> pos,
                              @NotNull NumberTrio<?> velocity) {
        owner.spawnParticle(name, pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(),
                velocity.getX().doubleValue(), velocity.getY().doubleValue(), velocity.getZ().doubleValue());
    }
}
