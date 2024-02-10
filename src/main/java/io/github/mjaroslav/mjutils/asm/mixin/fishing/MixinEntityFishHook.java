package io.github.mjaroslav.mjutils.asm.mixin.fishing;

import io.github.mjaroslav.mjutils.event.world.FishingSuccessEvent;
import lombok.val;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Shadow
    public EntityPlayer field_146042_b;

    @Unique
    private FishingSuccessEvent mjutils$event;

    @Inject(method = "func_146033_f", at = @At("HEAD"), cancellable = true)
    private void stage1$initializeEvent(@NotNull CallbackInfoReturnable<ItemStack> ci) {
        val chance = worldObj.rand.nextFloat();
        val luck = EnchantmentHelper.func_151386_g(((EntityFishHook) (Object) this).field_146042_b);
        val lure = EnchantmentHelper.func_151387_h(((EntityFishHook) (Object) this).field_146042_b);
        val category = FishingHooks.getFishableCategory(chance, luck, lure);
        val catchStack = FishingHooks.getRandomFishable(rand, chance, luck, lure);
        mjutils$event = new FishingSuccessEvent(field_146042_b, (EntityFishHook) (Object) this, category,
            catchStack, rand.nextInt(6) + 1, chance, luck, lure, true);
        MinecraftForge.EVENT_BUS.post(mjutils$event);
        ci.setReturnValue(new ItemStack(Blocks.air));
    }

    @Redirect(method = "func_146034_e", at = @At(value = "INVOKE", ordinal = 0,
        target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z"))
    private boolean stage2$replaceDropByEvent(@NotNull World world, @NotNull Entity entity) {
        if (mjutils$event != null && !mjutils$event.isCanceled() && mjutils$event.itemStack != null) {
            val entityItem = (EntityItem) entity;
            entityItem.setEntityItemStack(mjutils$event.itemStack);
            world.spawnEntityInWorld(entityItem);
        }
        return false;
    }

    @Redirect(method = "func_146034_e", at = @At(value = "INVOKE", ordinal = 1,
        target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z"))
    private boolean stage3$replaceXPCountByEventAddStatAndFinalizeEvent(@NotNull World world, @NotNull Entity entity) {
        if (mjutils$event != null && !mjutils$event.isCanceled()) {
            if (mjutils$event.experience > 0) {
                val xpOrb = (EntityXPOrb) entity;
                xpOrb.xpValue = mjutils$event.experience;
                world.spawnEntityInWorld(xpOrb);
            }
            if (mjutils$event.incStat && mjutils$event.category != null)
                field_146042_b.addStat(mjutils$event.category.stat, 1);
        }
        mjutils$event = null;
        return false;
    }
}
