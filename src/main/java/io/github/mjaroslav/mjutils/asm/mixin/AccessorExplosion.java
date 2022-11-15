package io.github.mjaroslav.mjutils.asm.mixin;

import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(Explosion.class)
public interface AccessorExplosion {
    @Accessor("worldObj")
    World getWorldObj();

    @Accessor("explosionRNG")
    Random getExplosionRNG();
}
