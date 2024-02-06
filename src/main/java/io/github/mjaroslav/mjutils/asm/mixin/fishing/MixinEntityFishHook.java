package io.github.mjaroslav.mjutils.asm.mixin.fishing;

import io.github.mjaroslav.mjutils.event.FishingSuccessEvent;
import lombok.val;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityFishHook.class)
public abstract class MixinEntityFishHook extends Entity {
    private MixinEntityFishHook(World world) {
        super(world);
    }

    @Unique
    private FishingSuccessEvent mjutils$fishingSuccessEvent;

    // Replace drop by event
    @Redirect(method = "func_146034_e",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z",
            ordinal = 0))
    private boolean injectedEntityItem(World world, Entity entity) {
        if (mjutils$fishingSuccessEvent != null && !mjutils$fishingSuccessEvent.isCanceled() && mjutils$fishingSuccessEvent.caughtItem != null)
            world.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, mjutils$fishingSuccessEvent.caughtItem));
        return false;
    }

    // Replacing XP by event and resetting of event
    @Redirect(method = "func_146034_e",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z",
            ordinal = 1))
    private boolean injectedEntityXPOrb(World world, Entity entity) {
        if (mjutils$fishingSuccessEvent != null) {
            if (!mjutils$fishingSuccessEvent.isCanceled() && mjutils$fishingSuccessEvent.caughtExperience > 0)
                world.spawnEntityInWorld(new EntityXPOrb(worldObj, mjutils$fishingSuccessEvent.fishman.posX,
                    mjutils$fishingSuccessEvent.fishman.posY, mjutils$fishingSuccessEvent.fishman.posZ,
                    mjutils$fishingSuccessEvent.caughtExperience));
            if (mjutils$fishingSuccessEvent.increaseStatistic && mjutils$fishingSuccessEvent.caughtCategory != null)
                mjutils$fishingSuccessEvent.fishman.addStat(mjutils$fishingSuccessEvent.caughtCategory.stat, 1);
        }
        mjutils$fishingSuccessEvent = null;
        return false;
    }

    // Add stat removing and call injected event
    @Inject(method = "func_146033_f",
        at = @At("HEAD"), cancellable = true)
    private void func_146033_f(@NotNull CallbackInfoReturnable<ItemStack> ci) {
        val chance = worldObj.rand.nextFloat();
        val luck = EnchantmentHelper.func_151386_g(((EntityFishHook) (Object) this).field_146042_b);
        val lure = EnchantmentHelper.func_151387_h(((EntityFishHook) (Object) this).field_146042_b);
        val category = net.minecraftforge.common.FishingHooks.getFishableCategory(chance, luck, lure);
        val catchStack = FishingHooks.getRandomFishable(rand, chance, luck, lure);
        mjutils$fishingSuccessEvent = new FishingSuccessEvent(((EntityFishHook) (Object) this).field_146042_b,
            (EntityFishHook) (Object) this, category, catchStack, rand.nextInt(6) + 1, chance, luck, lure, true);
        MinecraftForge.EVENT_BUS.post(mjutils$fishingSuccessEvent);
        ci.setReturnValue(new ItemStack(Blocks.air));
    }
}
