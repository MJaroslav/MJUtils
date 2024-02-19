package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockDoublePlant.class)
public interface AccessorBlockDoublePlant {
    @Invoker("func_149886_b")
    boolean dropBlockAndAddStat(@NotNull World world, int x, int y, int z, int meta, @NotNull EntityPlayer harvester);
}
