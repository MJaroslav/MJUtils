package mjaroslav.mcmods.mjutils.hook;

import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.asm.Hook;
import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.asm.ReturnCondition;
import mjaroslav.mcmods.mjutils.object.event.FishingSuccessEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("ALL")
public class HooksFishingEvent {
    public static final String DISABLE_ID = "hooks_fishing_event";

    // Return fishingEvent rood damage after right click.
    @Hook(returnCondition = ReturnCondition.ALWAYS)
    public static int func_146034_e(EntityFishHook instance) {
        if (instance.worldObj.isRemote)
            return 0;
        else {
            byte b0 = 0;
            if (instance.field_146043_c != null) {
                double d0 = instance.field_146042_b.posX - instance.posX;
                double d2 = instance.field_146042_b.posY - instance.posY;
                double d4 = instance.field_146042_b.posZ - instance.posZ;
                double d6 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d4 * d4);
                double d8 = 0.1D;
                instance.field_146043_c.motionX += d0 * d8;
                instance.field_146043_c.motionY += d2 * d8 + (double) MathHelper.sqrt_double(d6) * 0.08D;
                instance.field_146043_c.motionZ += d4 * d8;
                b0 = 3;
            } else if (instance.field_146045_ax > 0) {
                ItemStack catchStack = func_146033_f(instance);
                // NullPo crash fixed
                if (catchStack != null) {
                    EntityItem entityitem = new EntityItem(instance.worldObj, instance.posX, instance.posY,
                            instance.posZ, catchStack);
                    double d1 = instance.field_146042_b.posX - instance.posX;
                    double d3 = instance.field_146042_b.posY - instance.posY;
                    double d5 = instance.field_146042_b.posZ - instance.posZ;
                    double d7 = (double) MathHelper.sqrt_double(d1 * d1 + d3 * d3 + d5 * d5);
                    double d9 = 0.1D;
                    entityitem.motionX = d1 * d9;
                    entityitem.motionY = d3 * d9 + (double) MathHelper.sqrt_double(d7) * 0.08D;
                    entityitem.motionZ = d5 * d9;
                    instance.worldObj.spawnEntityInWorld(entityitem);
                    instance.field_146042_b.worldObj.spawnEntityInWorld(new EntityXPOrb(
                            instance.field_146042_b.worldObj, instance.field_146042_b.posX, instance.field_146042_b.posY
                            + 0.5D, instance.field_146042_b.posZ + 0.5D, instance.rand.nextInt(6) + 1));
                }
                b0 = 1;
            }
            if (instance.field_146051_au)
                b0 = 2;
            instance.setDead();
            instance.field_146042_b.fishEntity = null;
            return b0;
        }
    }

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
        if(event.category != null)
            instance.field_146042_b.addStat(event.category.stat, 1);
        return event.catchStack;
    }
}
