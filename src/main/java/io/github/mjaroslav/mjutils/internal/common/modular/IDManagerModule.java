package io.github.mjaroslav.mjutils.internal.common.modular;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import io.github.mjaroslav.mjutils.asm.MixinPatches.Enchantments;
import io.github.mjaroslav.mjutils.asm.MixinPatches.Potions;
import io.github.mjaroslav.mjutils.internal.data.IDManager;
import io.github.mjaroslav.mjutils.modular.SubscribeModule;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.*;

@SubscribeModule(priority = -1)
public class IDManagerModule {
    public static final IDManager POTIONS = new IDManager("Potion", Potions.enable, Potions.newArraySize,
        range(0, 24).boxed().collect(Collectors.toSet()),
        Potions.occupiedPolicy, Paths.get("config", "potions.properties"));
    public static final IDManager ENCHANTMENTS = new IDManager("Enchantment", Enchantments.enable,
        Enchantments.newArraySize, concat(concat(concat(concat(range(0, 8), range(16, 22)), range(32, 36)),
        range(48, 52)), range(61, 63)).boxed().collect(Collectors.toSet()), Enchantments.occupiedPolicy,
        Paths.get("config", "enchantments.properties"));

    public void listen(@NotNull FMLLoadCompleteEvent event) {
        Arrays.stream(Potion.potionTypes).filter(Objects::nonNull)
            .forEach(potion -> POTIONS.setComment(potion.id, potion.getName()));
        POTIONS.complete();
        Arrays.stream(Enchantment.enchantmentsList).filter(Objects::nonNull)
            .forEach(enchantment -> ENCHANTMENTS.setComment(enchantment.effectId, enchantment.getName()));
        ENCHANTMENTS.complete();
    }
}
