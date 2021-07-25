package com.github.mjaroslav.mjutils.hook;

import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm.Hook;
import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class HooksFishingNullFix {
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
                ItemStack catchStack = instance.func_146033_f();
                // NullPo crash fix
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
                    b0 = 1;
                }
            }
            if (instance.field_146051_au)
                b0 = 2;
            instance.setDead();
            instance.field_146042_b.fishEntity = null;
            return b0;
        }
    }
}
