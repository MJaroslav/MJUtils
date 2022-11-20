package io.github.mjaroslav.mjutils.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraftforge.event.entity.living.ZombieEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Cancelable
public class PigZombieBecomeAngryEvent extends ZombieEvent {
    public final @NotNull EntityPigZombie pigZombieEntity;
    public final @Nullable Entity causeEntity;

    public PigZombieBecomeAngryEvent(@NotNull EntityPigZombie pigZombie, @Nullable Entity causeEntity) {
        super(pigZombie);
        this.pigZombieEntity = pigZombie;
        this.causeEntity = causeEntity;
    }
}
