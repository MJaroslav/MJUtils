package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.object.game.event.FishingSuccessEvent;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityFishHook.class)
public abstract class MixinEntityFishHook extends Entity {
    private MixinEntityFishHook(World world) {
        super(world);
    }

    private FishingSuccessEvent fishingSuccessEvent;

    // Replace drop by event
    @Redirect(method = "func_146034_e",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z",
                    ordinal = 0))
    private boolean injectedEntityItem(World world, Entity entity) {
        if (fishingSuccessEvent != null && !fishingSuccessEvent.isCanceled() && fishingSuccessEvent.catchStack != null)
            world.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, fishingSuccessEvent.catchStack));
        return false;
    }

    // Replacing XP by event and resetting of event
    @Redirect(method = "func_146034_e",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntityInWorld(Lnet/minecraft/entity/Entity;)Z",
                    ordinal = 1))
    private boolean injectedEntityXPOrb(World world, Entity entity) {
        if (fishingSuccessEvent != null) {
            if (!fishingSuccessEvent.isCanceled() && fishingSuccessEvent.exp > 0)
                world.spawnEntityInWorld(new EntityXPOrb(worldObj, fishingSuccessEvent.fisher.posX,
                        fishingSuccessEvent.fisher.posY, fishingSuccessEvent.fisher.posZ, fishingSuccessEvent.exp));
            if (fishingSuccessEvent.incStat && fishingSuccessEvent.category != null)
                fishingSuccessEvent.fisher.addStat(fishingSuccessEvent.category.stat, 1);
        }
        fishingSuccessEvent = null;
        return false;
    }

    // Add stat removing and call injected event
    @Inject(method = "func_146033_f",
            at = @At("HEAD"), cancellable = true)
    private void func_146033_f(CallbackInfoReturnable<ItemStack> ci) {
        float chance = worldObj.rand.nextFloat();
        int luck = EnchantmentHelper.func_151386_g(((EntityFishHook) (Object) this).field_146042_b);
        int lure = EnchantmentHelper.func_151387_h(((EntityFishHook) (Object) this).field_146042_b);
        FishingHooks.FishableCategory category = net.minecraftforge.common.FishingHooks.getFishableCategory(chance,
                luck, lure);
        ItemStack catchStack = FishingHooks.getRandomFishable(rand, chance, luck, lure);
        fishingSuccessEvent = new FishingSuccessEvent(((EntityFishHook) (Object) this).field_146042_b,
                (EntityFishHook) (Object) this, category, catchStack,
                chance, luck, lure, rand.nextInt(6) + 1);
        MinecraftForge.EVENT_BUS.post(fishingSuccessEvent);
        ci.setReturnValue(new ItemStack(Blocks.air));
    }
}
