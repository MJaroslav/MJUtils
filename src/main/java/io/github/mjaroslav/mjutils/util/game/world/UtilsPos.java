package io.github.mjaroslav.mjutils.util.game.world;

import io.github.mjaroslav.mjutils.object.Trio;
import io.github.mjaroslav.mjutils.object.game.world.Pos;
import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class UtilsPos {
    public Block getBlock(@NotNull IBlockAccess world, @NotNull Pos pos) {
        return world.getBlock(pos.intX(), pos.intY(), pos.intZ());
    }

    public Block getBlock(@NotNull IBlockAccess world, @NotNull Trio<Integer, Integer, Integer> pos) {
        return world.getBlock(pos.getX(), pos.getY(), pos.getZ());
    }

    public int getBlockMetadata(@NotNull IBlockAccess world, @NotNull Pos pos) {
        return world.getBlockMetadata(pos.intX(), pos.intY(), pos.intZ());
    }

    public int getBlockMetadata(@NotNull IBlockAccess world, @NotNull Trio<Integer, Integer, Integer> pos) {
        return world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
    }

    public void dropBlockAsItemWithChance(@NotNull Block block, @NotNull World world, @NotNull Pos pos, int meta,
                                          float chance, int fortune) {
        block.dropBlockAsItemWithChance(world, pos.intX(), pos.intY(), pos.intZ(), meta, chance, fortune);
    }

    public void dropBlockAsItemWithChance(@NotNull Block block, @NotNull World world,
                                          @NotNull Trio<Integer, Integer, Integer> pos, int meta, float chance, int fortune) {
        block.dropBlockAsItemWithChance(world, pos.getX(), pos.getY(), pos.getZ(), meta, chance, fortune);
    }

    public void onBlockExploded(@NotNull Block block, @NotNull World world, @NotNull Pos pos, @NotNull Explosion explosion) {
        block.onBlockExploded(world, pos.intX(), pos.intY(), pos.intZ(), explosion);
    }

    public void onBlockExploded(@NotNull Block block, @NotNull World world, @NotNull Trio<Integer, Integer, Integer> pos,
                                @NotNull Explosion explosion) {
        block.onBlockExploded(world, pos.getX(), pos.getY(), pos.getZ(), explosion);
    }

    public boolean setBlock(@NotNull World world, @NotNull Trio<Integer, Integer, Integer> pos, @NotNull Block block) {
        return world.setBlock(pos.getX(), pos.getY(), pos.getZ(), block);
    }

    public boolean setBlock(@NotNull World world, @NotNull Pos pos, @NotNull Block block) {
        return world.setBlock(pos.intX(), pos.intY(), pos.intZ(), block);
    }

    public void playSoundEffect(@NotNull World world, @NotNull Trio<Double, Double, Double> pos, @NotNull String name,
                                float volume, float pitch) {
        world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), name, volume, pitch);
    }

    public void spawnParticle(@NotNull World world, @NotNull String name, @NotNull Trio<Double, Double, Double> pos,
                              @NotNull Trio<Double, Double, Double> velocity) {
        world.spawnParticle(name, pos.getX(), pos.getY(), pos.getZ(), velocity.getX(), velocity.getY(), velocity.getZ());

    }
}
