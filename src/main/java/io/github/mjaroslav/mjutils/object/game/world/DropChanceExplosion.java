package io.github.mjaroslav.mjutils.object.game.world;

import io.github.mjaroslav.mjutils.asm.mixin.AccessorExplosion;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Iterator;

public class DropChanceExplosion extends Explosion {
    @Getter
    @Setter
    protected float dropChance;

    public DropChanceExplosion(World world, Entity placer, double x, double y, double z, float strength, float dropChance) {
        super(world, placer, x, y, z, strength);
        this.dropChance = dropChance;
    }

    public DropChanceExplosion(World world, Entity placer, double x, double y, double z, float strength) {
        this(world, placer, x, y, z, strength, 1F);
    }

    public void doExplosionB(boolean addEffects) {
        val worldObj = ((AccessorExplosion) this).getWorldObj();
        worldObj.playSoundEffect(explosionX, explosionY, explosionZ, "random.explode", 4F, (1F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

        if (explosionSize >= 2.0F && isSmoking)
            worldObj.spawnParticle("hugeexplosion", explosionX, explosionY, explosionZ, 1D, 0D, 0D);
        else
            worldObj.spawnParticle("largeexplode", explosionX, explosionY, explosionZ, 1D, 0D, 0D);

        @SuppressWarnings("rawtypes") Iterator iterator;
        ChunkPosition chunkposition;
        int x;
        int y;
        int z;
        Block block;

        if (isSmoking) {
            iterator = affectedBlockPositions.iterator();

            while (iterator.hasNext()) {
                chunkposition = (ChunkPosition) iterator.next();
                x = chunkposition.chunkPosX;
                y = chunkposition.chunkPosY;
                z = chunkposition.chunkPosZ;
                block = worldObj.getBlock(x, y, z);

                if (addEffects) {
                    double d0 = x + worldObj.rand.nextFloat();
                    double d1 = y + worldObj.rand.nextFloat();
                    double d2 = z + worldObj.rand.nextFloat();
                    double d3 = d0 - explosionX;
                    double d4 = d1 - explosionY;
                    double d5 = d2 - explosionZ;
                    double d6 = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5D / (d6 / explosionSize + 0.1D);
                    d7 *= worldObj.rand.nextFloat() * worldObj.rand.nextFloat() + 0.3F;
                    d3 *= d7;
                    d4 *= d7;
                    d5 *= d7;
                    worldObj.spawnParticle("explode", (d0 + explosionX) / 2D, (d1 + explosionY) / 2D, (d2 + explosionZ) / 2D, d3, d4, d5);
                    worldObj.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);
                }

                if (block.getMaterial() != Material.air) {
                    if (block.canDropFromExplosion(this))
                        block.dropBlockAsItemWithChance(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z), dropChance, 0);

                    block.onBlockExploded(worldObj, x, y, z, this);
                }
            }
        }

        if (isFlaming) {
            iterator = affectedBlockPositions.iterator();

            while (iterator.hasNext()) {
                chunkposition = (ChunkPosition) iterator.next();
                x = chunkposition.chunkPosX;
                y = chunkposition.chunkPosY;
                z = chunkposition.chunkPosZ;
                block = worldObj.getBlock(x, y, z);
                Block block1 = worldObj.getBlock(x, y - 1, z);

                if (block.getMaterial() == Material.air && block1.func_149730_j() && ((AccessorExplosion) this).getExplosionRNG().nextInt(3) == 0)
                    worldObj.setBlock(x, y, z, Blocks.fire);
            }
        }
    }
}
