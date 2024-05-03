package io.github.mjaroslav.mjutils.pos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mjaroslav.mjutils.asm.mixin.accessors.AccessorWorld;
import io.github.mjaroslav.sharedjava.tuple.Triplet;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@UtilityClass
public class WorldPos {
    //region IBlockAccess
    //--------------------------
    public @NotNull Block blockAccess$getBlock(@NotNull IBlockAccess owner,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos) {
        return owner.getBlock(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public @Nullable TileEntity blockAccess$getTileEntity(@NotNull IBlockAccess owner,
                                                          @NotNull Triplet<? extends Number, ? extends Number,
                                                              ? extends Number> pos) {
        return owner.getTileEntity(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    @SideOnly(Side.CLIENT)
    public int blockAccess$getLightBrightnessForSkyBlocks(@NotNull IBlockAccess owner,
                                                          @NotNull Triplet<? extends Number, ? extends Number,
                                                              ? extends Number> pos, int light) {
        return owner.getLightBrightnessForSkyBlocks(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            light);
    }

    public int blockAccess$getBlockMetadata(@NotNull IBlockAccess owner,
                                            @NotNull Triplet<? extends Number, ? extends Number,
                                                ? extends Number> pos) {
        return owner.getBlockMetadata(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int blockAccess$isBlockProvidingPowerTo(@NotNull IBlockAccess owner,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos, int direction) {
        return owner.isBlockProvidingPowerTo(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            direction);
    }

    public boolean blockAccess$isAirBlock(@NotNull IBlockAccess owner,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isAirBlock(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    @SideOnly(Side.CLIENT)
    public @NotNull BiomeGenBase blockAccess$getBiomeGenForCoords(@NotNull IBlockAccess owner,
                                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                                      ? extends Number> pos) {
        return owner.getBiomeGenForCoords(pos.getX().intValue(), pos.getZ().intValue());
    }

    public boolean blockAccess$isSideSolid(@NotNull IBlockAccess owner,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                           @NotNull ForgeDirection side, boolean _default) {
        return owner.isSideSolid(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), side, _default);
    }
    //--------------------------
    //endregion

    //region IWorldAccess
    //--------------------------
    public void worldAccess$markBlockForUpdate(@NotNull IWorldAccess owner,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos) {
        owner.markBlockForUpdate(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void worldAccess$markBlockForRenderUpdate(@NotNull IWorldAccess owner,
                                                     @NotNull Triplet<? extends Number, ? extends Number,
                                                         ? extends Number> pos) {
        owner.markBlockForRenderUpdate(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void worldAccess$markBlockRangeForRenderUpdate(@NotNull IWorldAccess owner,
                                                          @NotNull Triplet<? extends Number, ? extends Number,
                                                              ? extends Number> a,
                                                          @NotNull Triplet<? extends Number, ? extends Number,
                                                              ? extends Number> b) {
        owner.markBlockRangeForRenderUpdate(
            Math.min(a.getX().intValue(), b.getX().intValue()),
            Math.min(a.getY().intValue(), b.getY().intValue()),
            Math.min(a.getZ().intValue(), b.getZ().intValue()),
            Math.max(a.getX().intValue(), b.getX().intValue()),
            Math.max(a.getY().intValue(), b.getY().intValue()),
            Math.max(a.getZ().intValue(), b.getZ().intValue())
        );
    }

    public void worldAccess$playSound(@NotNull IWorldAccess owner, @NotNull String soundName,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                      float volume, float pitch) {
        owner.playSound(soundName, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), volume, pitch);
    }

    public void worldAccess$playSoundToNearExcept(@NotNull IWorldAccess owner, @NotNull EntityPlayer player,
                                                  @NotNull String soundName,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos, float volume, float pitch) {
        owner.playSoundToNearExcept(player, soundName, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), volume, pitch);
    }

    public void worldAccess$spawnParticle(@NotNull IWorldAccess owner, @NotNull String particleName,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                          @NotNull Triplet<? extends Number, ? extends Number,
                                              ? extends Number> velocity) {
        owner.spawnParticle(particleName, pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(),
            velocity.getX().doubleValue(), velocity.getY().doubleValue(), velocity.getZ().doubleValue());
    }

    public void worldAccess$playRecord(@NotNull IWorldAccess owner, @NotNull String recordName,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.playRecord(recordName, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void worldAccess$broadcastSound(@NotNull IWorldAccess owner, int soundId,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                           int idk) {
        owner.broadcastSound(soundId, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), idk);
    }

    public void worldAccess$playAuxSFX(@NotNull IWorldAccess owner, @NotNull EntityPlayer player, int sfxId,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                       int type) {
        owner.playAuxSFX(player, sfxId, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), type);
    }

    public void worldAccess$destroyBlockPartially(@NotNull IWorldAccess owner, int blockId,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos, int value) {
        owner.destroyBlockPartially(blockId, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            value);
    }
    //--------------------------
    //endregion

    //region World
    //--------------------------
    public @NotNull BiomeGenBase world$getBiomeGenForCoordsBody(@NotNull World owner,
                                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                                    ? extends Number> pos) {
        return owner.getBiomeGenForCoordsBody(pos.getX().intValue(), pos.getZ().intValue());
    }

    public @NotNull Block world$getTopBlock(@NotNull World owner,
                                            @NotNull Triplet<? extends Number, ? extends Number,
                                                ? extends Number> pos) {
        return owner.getTopBlock(pos.getX().intValue(), pos.getZ().intValue());
    }

    public boolean world$blockExists(@NotNull World owner,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.blockExists(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean world$doChunksNearChunkExist(@NotNull World owner,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos, int distance) {
        return owner.doChunksNearChunkExist(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            distance);
    }

    public boolean world$checkChunksExist(@NotNull World owner,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> a,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> b) {
        return owner.checkChunksExist(
            Math.min(a.getX().intValue(), b.getX().intValue()),
            Math.min(a.getY().intValue(), b.getY().intValue()),
            Math.min(a.getZ().intValue(), b.getZ().intValue()),
            Math.max(a.getX().intValue(), b.getX().intValue()),
            Math.max(a.getY().intValue(), b.getY().intValue()),
            Math.max(a.getZ().intValue(), b.getZ().intValue())
        );
    }

    public boolean world$chunkExists(@NotNull World owner,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return ((AccessorWorld) owner).callChunkExists(pos.getX().intValue(), pos.getZ().intValue());
    }

    public @NotNull Chunk world$getChunkFromBlockCoords(@NotNull World owner,
                                                        @NotNull Triplet<? extends Number, ? extends Number,
                                                            ? extends Number> pos) {
        return owner.getChunkFromBlockCoords(pos.getX().intValue(), pos.getZ().intValue());
    }

    public @NotNull Chunk world$getChunkFromChunkCoords(@NotNull World owner,
                                                        @NotNull Triplet<? extends Number, ? extends Number,
                                                            ? extends Number> pos) {
        return owner.getChunkFromChunkCoords(pos.getX().intValue(), pos.getZ().intValue());
    }

    public boolean world$setBlock(@NotNull World owner,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                  @NotNull Block block, int meta, int flag) {
        return owner.setBlock(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block, meta, flag);
    }

    public void world$markAndNotifyBlock(@NotNull World owner,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                         @Nullable Chunk chunk, @NotNull Block oldBlock, @NotNull Block newBlock,
                                         int flag) {
        owner.markAndNotifyBlock(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), chunk, oldBlock,
            newBlock, flag);
    }

    public boolean world$setBlockMetadataWithNotify(@NotNull World owner,
                                                    @NotNull Triplet<? extends Number, ? extends Number,
                                                        ? extends Number> pos, int meta, int flag) {
        return owner.setBlockMetadataWithNotify(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            meta, flag);
    }

    public boolean world$setBlockToAir(@NotNull World owner,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.setBlockToAir(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean world$breakBlock(@NotNull World owner,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                    boolean drop) {
        return owner.func_147480_a(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), drop);
    }

    public boolean world$setBlock(@NotNull World owner,
                                  @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                  @NotNull Block block) {
        return owner.setBlock(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block);
    }

    public void world$markBlockForUpdate(@NotNull World owner,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.markBlockForUpdate(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void world$notifyBlockChange(@NotNull World owner,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        @NotNull Block block) {
        owner.notifyBlockChange(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block);
    }

    public void world$markBlocksDirtyVertical(@NotNull World owner,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos, int heightA, int heightB) {
        owner.markBlocksDirtyVertical(pos.getX().intValue(), pos.getZ().intValue(), heightA, heightB);
    }

    public void world$markBlockRangeForRenderUpdate(@NotNull World owner,
                                                    @NotNull Triplet<? extends Number, ? extends Number,
                                                        ? extends Number> a,
                                                    @NotNull Triplet<? extends Number, ? extends Number,
                                                        ? extends Number> b) {
        owner.markBlockRangeForRenderUpdate(
            Math.min(a.getX().intValue(), b.getX().intValue()),
            Math.min(a.getY().intValue(), b.getY().intValue()),
            Math.min(a.getZ().intValue(), b.getZ().intValue()),
            Math.max(a.getX().intValue(), b.getX().intValue()),
            Math.max(a.getY().intValue(), b.getY().intValue()),
            Math.max(a.getZ().intValue(), b.getZ().intValue())
        );
    }

    public void world$notifyBlocksOfNeighborChange(@NotNull World owner,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos, @NotNull Block block) {
        owner.notifyBlocksOfNeighborChange(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block);
    }

    public void world$notifyBlocksOfNeighborChange(@NotNull World owner,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos, @NotNull Block block, int direction) {
        owner.notifyBlocksOfNeighborChange(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block,
            direction);
    }

    public void world$notifyBlockOfNeighborChange(@NotNull World owner,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos, @NotNull final Block block) {
        owner.notifyBlockOfNeighborChange(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block);
    }

    public boolean world$isBlockTickScheduledThisTick(@NotNull World owner,
                                                      @NotNull Triplet<? extends Number, ? extends Number,
                                                          ? extends Number> pos, @NotNull Block block) {
        return owner.isBlockTickScheduledThisTick(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            block);
    }

    public boolean world$canBlockSeeTheSky(@NotNull World owner,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canBlockSeeTheSky(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int world$getFullBlockLightValue(@NotNull World owner,
                                            @NotNull Triplet<? extends Number, ? extends Number,
                                                ? extends Number> pos) {
        return owner.getFullBlockLightValue(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int world$getBlockLightValue(@NotNull World owner,
                                        @NotNull Triplet<? extends Number, ? extends Number,
                                            ? extends Number> pos) {
        return owner.getBlockLightValue(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int world$getBlockLightValue_do(@NotNull World owner,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                           boolean maxLightIsNear) {
        return owner.getBlockLightValue_do(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            maxLightIsNear);
    }

    public int world$getHeightValue(@NotNull World owner,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getHeightValue(pos.getX().intValue(), pos.getZ().intValue());
    }

    public int world$getChunkHeightMapMinimum(@NotNull World owner,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos) {
        return owner.getChunkHeightMapMinimum(pos.getX().intValue(), pos.getZ().intValue());
    }

    @SideOnly(Side.CLIENT)
    public int world$getSkyBlockTypeBrightness(@NotNull World owner, @NotNull EnumSkyBlock skyBlock,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos) {
        return owner.getSkyBlockTypeBrightness(skyBlock, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public int world$getSavedLightValue(@NotNull World owner, @NotNull EnumSkyBlock skyBlock,
                                        @NotNull Triplet<? extends Number, ? extends Number,
                                            ? extends Number> pos) {
        return owner.getSavedLightValue(skyBlock, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public void world$setLightValue(@NotNull World owner, @NotNull EnumSkyBlock skyBlock,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                    int value) {
        owner.setLightValue(skyBlock, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), value);
    }

    public void world$markBlockForRenderUpdateInAllAccessors(@NotNull World owner,
                                                             @NotNull Triplet<? extends Number, ? extends Number,
                                                                 ? extends Number> pos) {
        owner.func_147479_m(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public float world$getLightBrightness(@NotNull World owner,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getLightBrightness(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public @Nullable MovingObjectPosition world$rayTraceBlocks(@NotNull World owner,
                                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                                   ? extends Number> a,
                                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                                   ? extends Number> b) {
        return owner.rayTraceBlocks(Pos.of(a).asVec3(), Pos.of(b).asVec3());
    }

    public @Nullable MovingObjectPosition world$rayTraceBlocks(@NotNull World owner,
                                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                                   ? extends Number> a,
                                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                                   ? extends Number> b, boolean stopOnLiquid) {
        return owner.rayTraceBlocks(Pos.of(a).asVec3(), Pos.of(b).asVec3(), stopOnLiquid);
    }

    public @Nullable MovingObjectPosition world$rayTraceBlocks(@NotNull World owner,
                                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                                   ? extends Number> a,
                                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                                   ? extends Number> b,
                                                               boolean stopOnLiquid,
                                                               boolean ignoreBlocksWithBoundingBox,
                                                               boolean returnLastUncollidableBlock) {
        return owner.func_147447_a(Pos.of(a).asVec3(), Pos.of(b).asVec3(), stopOnLiquid, ignoreBlocksWithBoundingBox,
            returnLastUncollidableBlock);
    }

    public void world$playSoundEffect(@NotNull World owner,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                      @NotNull String soundName, float volume, float pitch) {
        owner.playSoundEffect(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(),
            soundName, volume, pitch);
    }

    public void world$playSound(@NotNull World owner,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                @NotNull String soundName, float volume, float pitch, boolean delayed) {
        owner.playSound(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(), soundName,
            volume, pitch, delayed);
    }

    public void world$playRecord(@NotNull World owner, @NotNull String recordName,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.playRecord(recordName, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void world$spawnParticle(@NotNull World owner, @NotNull String particleName,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> velocity) {
        owner.spawnParticle(particleName, pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(),
            velocity.getX().doubleValue(), velocity.getY().doubleValue(), velocity.getZ().doubleValue());
    }

    public int world$getPrecipitationHeight(@NotNull World owner,
                                            @NotNull Triplet<? extends Number, ? extends Number,
                                                ? extends Number> pos) {
        return owner.getPrecipitationHeight(pos.getX().intValue(), pos.getZ().intValue());
    }

    public int world$getTopSolidOrLiquidBlock(@NotNull World owner,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos) {
        return owner.getTopSolidOrLiquidBlock(pos.getX().intValue(), pos.getZ().intValue());
    }

    public void world$scheduleBlockUpdate(@NotNull World owner,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                          @NotNull Block block, int delay) {
        owner.scheduleBlockUpdate(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block, delay);
    }

    public void world$scheduleBlockUpdateWithPriority(@NotNull World owner,
                                                      @NotNull Triplet<? extends Number, ? extends Number,
                                                          ? extends Number> pos, @NotNull Block block, int delay,
                                                      int priority) {
        owner.scheduleBlockUpdateWithPriority(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            block, delay, priority);
    }

    public void world$scheduleBlockUpdateWithPriorityForced(@NotNull World owner,
                                                            @NotNull Triplet<? extends Number, ? extends Number,
                                                                ? extends Number> pos, @NotNull Block block,
                                                            int delay, int priority) {
        owner.func_147446_b(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block, delay,
            priority);
    }

    public @NotNull Explosion world$createExplosion(@NotNull World owner, @Nullable Entity exploder,
                                                    @NotNull Triplet<? extends Number, ? extends Number,
                                                        ? extends Number> pos, float strength, boolean smoking) {
        return owner.createExplosion(exploder, pos.getX().doubleValue(), pos.getY().doubleValue(),
            pos.getZ().doubleValue(), strength, smoking);
    }

    public @NotNull Explosion world$newExplosion(@NotNull World owner, @Nullable Entity exploder,
                                                 @NotNull Triplet<? extends Number, ? extends Number,
                                                     ? extends Number> pos, float strength, boolean flaming,
                                                 boolean smoking) {
        return owner.newExplosion(exploder, pos.getX().doubleValue(), pos.getY().doubleValue(),
            pos.getZ().doubleValue(), strength, flaming, smoking);
    }

    public boolean world$extinguishFire(@NotNull World owner, @NotNull EntityPlayer initiator,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        int direction) {
        return owner.extinguishFire(initiator, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            direction);
    }

    public void world$setTileEntity(@NotNull World owner,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                    @Nullable TileEntity tile) {
        owner.setTileEntity(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), tile);
    }

    public void world$removeTileEntity(@NotNull World owner,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.removeTileEntity(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean world$isAverageEdgeLengthBig(@NotNull World owner,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos) {
        return owner.func_147469_q(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean world$doesBlockHaveSolidTopSurface(@NotNull IBlockAccess world,
                                                      @NotNull Triplet<? extends Number, ? extends Number,
                                                          ? extends Number> pos) {
        return World.doesBlockHaveSolidTopSurface(world, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public boolean world$isBlockNormalCubeDefault(@NotNull World owner,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos, boolean _default) {
        return owner.isBlockNormalCubeDefault(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            _default);
    }

    public boolean world$isBlockFreezable(@NotNull World owner,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.isBlockFreezable(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean world$isBlockFreezableNaturally(@NotNull World owner,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos) {
        return owner.isBlockFreezableNaturally(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean world$canBlockFreeze(@NotNull World owner,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                        boolean checkForNearBeach) {
        return owner.canBlockFreeze(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            checkForNearBeach);
    }

    public boolean world$canBlockFreezeBody(@NotNull World owner,
                                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                            boolean checkForNearBeach) {
        return owner.canBlockFreezeBody(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            checkForNearBeach);
    }

    public boolean world$canSnowAt(@NotNull World owner,
                                   @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                   boolean checkLight) {
        return owner.func_147478_e(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), checkLight);
    }

    public boolean world$canSnowAtBody(@NotNull World owner,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                       boolean checkLight) {
        return owner.canSnowAtBody(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), checkLight);
    }

    public boolean world$updateLight(@NotNull World owner,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.func_147451_t(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public int world$computeLightValue(@NotNull World owner,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                       @NotNull EnumSkyBlock skyBlock) {
        return ((AccessorWorld) owner).callComputeLightValue(pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue(), skyBlock);
    }

    public boolean world$updateLightByType(@NotNull World owner, @NotNull EnumSkyBlock skyBlock,
                                           @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.updateLightByType(skyBlock, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void world$markTileEntityChunkModified(@NotNull World owner,
                                                  @NotNull Triplet<? extends Number, ? extends Number,
                                                      ? extends Number> pos, @NotNull TileEntity tile) {
        owner.markTileEntityChunkModified(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), tile);
    }

    public boolean world$canPlaceEntityOnSide(@NotNull World owner, @NotNull Block block,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos, boolean ignoreCollision, int side,
                                              @NotNull Entity entity, @NotNull ItemStack stack) {
        return owner.canPlaceEntityOnSide(block, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            ignoreCollision, side, entity, stack);
    }

    public @NotNull PathEntity world$getEntityPathToXYZ(@NotNull World owner, @NotNull Entity entity,
                                                        @NotNull Triplet<? extends Number, ? extends Number,
                                                            ? extends Number> pos, float searchRange,
                                                        boolean woddenDoorAllowed, boolean movementBlockAllowed,
                                                        boolean pathingInWater, boolean entityDrown) {
        return owner.getEntityPathToXYZ(entity, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            searchRange, woddenDoorAllowed, movementBlockAllowed, pathingInWater, entityDrown);
    }

    public int world$getBlockPowerInput(@NotNull World owner,
                                        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getBlockPowerInput(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean world$getIndirectPowerOutput(@NotNull World owner,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos, int direction) {
        return owner.getIndirectPowerOutput(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            direction);
    }

    public int world$getIndirectPowerLevelTo(@NotNull World owner,
                                             @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                             int direction) {
        return owner.getIndirectPowerLevelTo(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            direction);
    }

    public boolean world$isBlockIndirectlyGettingPowered(@NotNull World owner,
                                                         @NotNull Triplet<? extends Number, ? extends Number,
                                                             ? extends Number> pos) {
        return owner.isBlockIndirectlyGettingPowered(pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
    }

    public int world$getStrongestIndirectPower(@NotNull World owner,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos) {
        return owner.getStrongestIndirectPower(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public @Nullable EntityPlayer world$getClosestPlayer(@NotNull World owner,
                                                         @NotNull Triplet<? extends Number, ? extends Number,
                                                             ? extends Number> pos, double distance) {
        return owner.getClosestPlayer(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(),
            distance);
    }

    public @Nullable EntityPlayer world$getClosestVulnerablePlayer(@NotNull World owner,
                                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                                       ? extends Number> pos, double distance) {
        return owner.getClosestVulnerablePlayer(pos.getX().doubleValue(), pos.getY().doubleValue(),
            pos.getZ().doubleValue(), distance);
    }

    public @NotNull Pos getSpawnPoint(@NotNull World owner) {
        return Pos.of(owner.getSpawnPoint());
    }

    public void world$setSpawnLocation(@NotNull World owner,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        owner.setSpawnLocation(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean world$canMineBlock(@NotNull World owner, @NotNull EntityPlayer miner,
                                      @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canMineBlock(miner, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean world$canMineBlockBody(@NotNull World owner, @NotNull EntityPlayer miner,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.canMineBlockBody(miner, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void world$addBlockEvent(@NotNull World owner,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                    @NotNull Block block, int eventId, int eventParam) {
        owner.addBlockEvent(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block, eventId,
            eventParam);
    }

    public boolean world$canLightningStrikeAt(@NotNull World owner,
                                              @NotNull Triplet<? extends Number, ? extends Number,
                                                  ? extends Number> pos) {
        return owner.canLightningStrikeAt(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean world$isBlockHighHumidity(@NotNull World owner,
                                             @NotNull Triplet<? extends Number, ? extends Number,
                                                 ? extends Number> pos) {
        return owner.isBlockHighHumidity(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public void world$playBroadcastSound(@NotNull World owner, int soundId,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                         int idk) {
        owner.playBroadcastSound(soundId, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), idk);
    }

    public void world$playAuxSFX(@NotNull World owner, int sfxId,
                                 @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                 int type) {
        owner.playAuxSFX(sfxId, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), type);
    }

    public void world$playAuxSFXAtEntity(@NotNull World owner, @NotNull EntityPlayer player, int sfxId,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                         int type) {
        owner.playAuxSFXAtEntity(player, sfxId, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            type);
    }

    public @Nullable Pos world$findClosestStructure(@NotNull World owner, @NotNull String name,
                                                    @NotNull Triplet<? extends Number, ? extends Number,
                                                        ? extends Number> pos) {
        val coord = owner.findClosestStructure(name, pos.getX().intValue(), pos.getY().intValue(),
            pos.getZ().intValue());
        return coord != null ? Pos.of(coord) : null;
    }

    public void world$destroyBlockInWorldPartially(@NotNull World owner, int blockId,
                                                   @NotNull Triplet<? extends Number, ? extends Number,
                                                       ? extends Number> pos, int value) {
        owner.destroyBlockInWorldPartially(blockId, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(),
            value);
    }

    @SideOnly(Side.CLIENT)
    public void world$makeFireworks(@NotNull World owner,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                    @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> velocity,
                                    @NotNull NBTTagCompound data) {
        owner.makeFireworks(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(),
            velocity.getX().doubleValue(), velocity.getY().doubleValue(), velocity.getZ().doubleValue(), data);
    }

    public void world$cau8seNeighborChangeAround(@NotNull World owner,
                                                 @NotNull Triplet<? extends Number, ? extends Number,
                                                     ? extends Number> pos, @NotNull Block block) {
        owner.func_147453_f(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block);
    }

    public float world$getLocalDifficultFloored(@NotNull World owner,
                                                @NotNull Triplet<? extends Number, ? extends Number,
                                                    ? extends Number> pos) {
        return owner.func_147462_b(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
    }

    public float world$getLocalDifficult(@NotNull World owner,
                                         @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.func_147473_B(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public boolean world$isSideSolid(@NotNull World owner,
                                     @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                     @NotNull ForgeDirection side) {
        return owner.isSideSolid(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), side);
    }

    public int world$getBlockLightOpacity(@NotNull World owner,
                                          @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        return owner.getBlockLightOpacity(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion

    //region WorldServer
    //--------------------------
    public @Nullable BiomeGenBase.SpawnListEntry worldServer$spawnRandomCreature(@NotNull WorldServer owner,
                                                                                 @NotNull EnumCreatureType type,
                                                                                 @NotNull Triplet<? extends Number,
                                                                                     ? extends Number,
                                                                                     ? extends Number> pos) {
        return owner.spawnRandomCreature(type, pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }

    public @Nullable List<TileEntity> worldServer$getTileEntitiesInBox(@NotNull WorldServer owner,
                                                                       @NotNull Triplet<? extends Number,
                                                                           ? extends Number, ? extends Number> a,
                                                                       @NotNull Triplet<? extends Number,
                                                                           ? extends Number, ? extends Number> b) {
        //noinspection unchecked
        return (List<TileEntity>) owner.func_147486_a(
            Math.min(a.getX().intValue(), b.getX().intValue()),
            Math.min(a.getY().intValue(), b.getY().intValue()),
            Math.min(a.getZ().intValue(), b.getZ().intValue()),
            Math.max(a.getX().intValue(), b.getX().intValue()),
            Math.max(a.getY().intValue(), b.getY().intValue()),
            Math.max(a.getZ().intValue(), b.getZ().intValue())
        );
    }

    public void worldServer$sendParticlePacket(@NotNull WorldServer owner, @NotNull String particleName,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos, int count,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> velocity, double spreading) {
        owner.func_147487_a(particleName, pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue(),
            count, velocity.getX().doubleValue(), velocity.getY().doubleValue(), velocity.getZ().doubleValue(),
            spreading);
    }
    //--------------------------
    //endregion

    //region WorldClient
    //--------------------------
    @SideOnly(Side.CLIENT)
    public void worldClient$invalidateBlockReceiveRegion(@NotNull WorldClient owner,
                                                         @NotNull Triplet<? extends Number, ? extends Number,
                                                             ? extends Number> a,
                                                         @NotNull Triplet<? extends Number, ? extends Number,
                                                             ? extends Number> b) {
        owner.invalidateBlockReceiveRegion(
            Math.min(a.getX().intValue(), b.getX().intValue()),
            Math.min(a.getY().intValue(), b.getY().intValue()),
            Math.min(a.getZ().intValue(), b.getZ().intValue()),
            Math.max(a.getX().intValue(), b.getX().intValue()),
            Math.max(a.getY().intValue(), b.getY().intValue()),
            Math.max(a.getZ().intValue(), b.getZ().intValue())
        );
    }

    @SideOnly(Side.CLIENT)
    public void worldClient$doPreChunk(@NotNull WorldClient owner,
                                       @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                       boolean load) {
        owner.doPreChunk(pos.getX().intValue(), pos.getZ().intValue(), load);
    }

    @SideOnly(Side.CLIENT)
    public boolean worldClient$softSetBlock(@NotNull WorldClient owner,
                                            @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                            @NotNull Block block, int meta) {
        return owner.func_147492_c(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue(), block, meta);
    }

    @SideOnly(Side.CLIENT)
    public void worldClient$doVoidFogParticles(@NotNull WorldClient owner,
                                               @NotNull Triplet<? extends Number, ? extends Number,
                                                   ? extends Number> pos) {
        owner.doVoidFogParticles(pos.getX().intValue(), pos.getY().intValue(), pos.getZ().intValue());
    }
    //--------------------------
    //endregion
}
