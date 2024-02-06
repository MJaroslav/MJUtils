package io.github.mjaroslav.mjutils.util.object.game;

import io.github.mjaroslav.mjutils.asm.mixin.accessors.AccessorExplosion;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.github.mjaroslav.mjutils.util.game.UtilsPosBlock.*;
import static io.github.mjaroslav.mjutils.util.game.UtilsPosWorld.*;

public class DropChanceExplosion extends Explosion {
    @Getter
    @Setter
    protected float dropChance;

    public DropChanceExplosion(@NotNull World world, @Nullable Entity placer, @NotNull Pos pos, float strength,
                               float dropChance) {
        this(world, placer, pos.getX(), pos.getY(), pos.getZ(), strength, dropChance);
    }

    public DropChanceExplosion(@NotNull World world, @Nullable Entity placer, double x, double y, double z,
                               float strength, float dropChance) {
        super(world, placer, x, y, z, strength);
        this.dropChance = dropChance;
    }

    @Override
    public void doExplosionB(boolean addEffects) {
        val expPos = new Pos(explosionX, explosionY, explosionZ);
        val world = ((AccessorExplosion) this).getWorldObj();
        playSoundEffect(world, expPos, "random.explode", 4F, (1F + (world.rand.nextFloat() - world.rand.nextFloat())
            * 0.2F) * 0.7F);
        if (explosionSize >= 2.0F && isSmoking) spawnParticle(world, "hugeexplosion", expPos, Pos.X);
        else spawnParticle(world, "largeexplode", expPos, Pos.X);
        var temp = new Pos();
        Block block;
        if (isSmoking)
            for (var affectedBlockPosition : affectedBlockPositions) {
                temp.set((ChunkPosition) affectedBlockPosition);
                block = getBlock(world, temp);
                if (addEffects) {
                    temp = temp.add(world.rand.nextFloat(), world.rand.nextFloat(), world.rand.nextFloat());
                    var velocity = temp.sub(explosionX, explosionY, explosionZ);
                    val diameter = velocity.diameter();
                    velocity = velocity.div(diameter).mul(0.5D / (diameter / explosionSize + 0.1D)).mul(
                        world.rand.nextFloat() * world.rand.nextFloat() + 0.3F);
                    spawnParticle(world, "explode", temp.add(explosionX, explosionY, explosionZ).div(2d), velocity);
                    spawnParticle(world, "smoke", temp, velocity);
                }
                temp.set((ChunkPosition) affectedBlockPosition); // Reset of .part
                if (block.getMaterial() != Material.air) {
                    if (block.canDropFromExplosion(this))
                        dropBlockAsItemWithChance(block, world, temp, getBlockMetadata(world, temp), dropChance, 0);
                    onBlockExploded(block, world, temp, this);
                }
            }
        if (isFlaming)
            for (var affectedBlockPosition : affectedBlockPositions) {
                temp.set((ChunkPosition) affectedBlockPosition);
                block = getBlock(world, temp);
                val underBlock = getBlock(world, temp.minusY());
                if (block.getMaterial() == Material.air && underBlock.func_149730_j() &&
                    ((AccessorExplosion) this).getExplosionRNG().nextInt(3) == 0)
                    setBlock(world, temp, Blocks.fire);
            }
    }
}
