package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityPlayer.class)
public interface AccessorEntityPlayer {
    @Invoker
    void callAddMountedMovementStat(double deltaX, double deltaY, double deltaZ);

    @Accessor
    @NotNull
    ChunkCoordinates getPlayerLocation();

    @Accessor
    void setPlayerLocation(@NotNull ChunkCoordinates playerLocation);

    @Accessor
    @Nullable
    ChunkCoordinates getStartMinecartRidingCoordinate();

    @Accessor
    void setStartMinecartRidingCoordinate(@Nullable ChunkCoordinates startMinecartRidingCoordinate);
}
