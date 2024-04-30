package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.EntityHanging;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemHangingEntity.class)
public interface AccessorItemHangingEntity {
    @Invoker
    @Nullable
    EntityHanging callCreateHangingEntity(@NotNull World world, int x, int y, int z, int direction);
}
