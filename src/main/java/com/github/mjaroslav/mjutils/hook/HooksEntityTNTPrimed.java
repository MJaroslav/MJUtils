package com.github.mjaroslav.mjutils.hook;

//import gloomyfolken.hooklib.asm.Hook;
//import gloomyfolken.hooklib.asm.ReturnCondition;
import com.github.mjaroslav.mjutils.mod.lib.CategoryRoot;
import com.github.mjaroslav.mjutils.util.game.world.UtilsWorld;
import net.minecraft.entity.item.EntityTNTPrimed;

public class HooksEntityTNTPrimed {
    //@Hook(returnCondition = ReturnCondition.ALWAYS)
    public static void explode(EntityTNTPrimed instance) {
        if (CategoryRoot.noLossTNTExplosion)
            UtilsWorld.newExplosionWithDropChance(instance.worldObj, instance, instance.posX, instance.posY,
                    instance.posZ, 4F, false, true, 1F);
        else
            instance.worldObj.createExplosion(instance, instance.posX, instance.posY, instance.posZ, 4F, true);
    }
}
