package mjaroslav.mcmods.mjutils.hook;

import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.asm.Hook;
import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.asm.ReturnCondition;
import mjaroslav.mcmods.mjutils.object.event.FishingSuccessEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.MinecraftForge;

public class HooksFishingEvent {
    public static final String DISABLE_ID = "hooks_fishing_event";

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
