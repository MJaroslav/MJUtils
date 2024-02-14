package io.github.mjaroslav.mjutils.pos;

import io.github.mjaroslav.sharedjava.tuple.Triplet;
import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

// TODO: MAKE DOCS AND MAKE FULL IT
@UtilityClass
public class WorldPos {
    public Block getBlock(@NotNull IBlockAccess owner, @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getBlock(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int getBlockMetadata(@NotNull IBlockAccess owner, @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getBlockMetadata(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean setBlock(@NotNull World owner, @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos, @NotNull Block block) {
        return owner.setBlock(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block);
    }

    public void playSoundEffect(@NotNull World owner, @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos, @NotNull String name, float volume,
                                float pitch) {
        owner.playSoundEffect(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(), name,
            volume, pitch);
    }

    public void spawnParticle(@NotNull World owner, @NotNull String name, @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                              @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> velocity) {
        owner.spawnParticle(name, pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(),
            velocity.getX().doubleValue(), velocity.getY().doubleValue(), velocity.getZ().doubleValue());
    }
}
