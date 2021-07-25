package com.github.mjaroslav.mjutils.hook;

import com.github.mjaroslav.mjutils.object.event.FishingSuccessEvent;
import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm.Hook;
import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.MinecraftForge;

public class HooksFishingEvent {
    @Hook(returnCondition = ReturnCondition.ALWAYS)
    public static ItemStack func_146033_f(EntityFishHook instance) {
        float chance = instance.worldObj.rand.nextFloat();
        int luck = EnchantmentHelper.func_151386_g(instance.field_146042_b);
        int speed = EnchantmentHelper.func_151387_h(instance.field_146042_b);
        FishingHooks.FishableCategory category = net.minecraftforge.common.FishingHooks.getFishableCategory(chance,
                luck, speed);
        ItemStack catchStack = FishingHooks.getRandomFishable(instance.rand, chance, luck, speed);
        FishingSuccessEvent event = new FishingSuccessEvent(instance.field_146042_b, instance, category, catchStack,
                chance, luck, speed);
        // Hooked custom event
        MinecraftForge.EVENT_BUS.post(event);
        if (event.category != null)
            instance.field_146042_b.addStat(event.category.stat, 1);
        return event.catchStack;
    }
}
