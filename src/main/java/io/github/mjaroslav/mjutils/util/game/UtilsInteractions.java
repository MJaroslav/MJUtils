package io.github.mjaroslav.mjutils.util.game;

import io.github.mjaroslav.mjutils.item.ItemStackSet;
import io.github.mjaroslav.mjutils.item.Stacks;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


@UtilityClass
public class UtilsInteractions {
    private final ItemStackSet PIG_ZOMBIE_GREED_BLOCKS = new ItemStackSet(Stacks.ITEM_NULLABLE | Stacks.META_WILDCARD);

    public void configureBlockAsPigZombieGreedTrigger(@NotNull Object block, boolean value) {
        val blockStack = Stacks.make(block);
        if (value) PIG_ZOMBIE_GREED_BLOCKS.add(blockStack);
        else PIG_ZOMBIE_GREED_BLOCKS.remove(blockStack);
    }

    public boolean isBlockPigZombieGreedTrigger(@NotNull Block block, int meta) {
        System.out.println(PIG_ZOMBIE_GREED_BLOCKS);
        System.out.println(PIG_ZOMBIE_GREED_BLOCKS.contains(new ItemStack(block, 1, meta)));
        return PIG_ZOMBIE_GREED_BLOCKS.contains(new ItemStack(block, 1, meta));
    }

    /**
     * Inaccurate copy of the method of multi-purpose
     * attack from Thaumcraft.
     *
     * @param target - target entity.
     * @param player - attacker.
     * @author Azanor
     */
    // TODO: Чё это такое...
    public void multiPurposeAttack(@NotNull Entity target, @NotNull EntityPlayer player) {
        if (MinecraftForge.EVENT_BUS.post(new AttackEntityEvent(player, target)))
            return;
        if (!target.canAttackWithItem() || target.hitByEntity(player))
            return;
        var damage = (float) player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        var knockback = 0;
        var magicDamage = 0.0F;
        if (target instanceof EntityLivingBase living) {
            magicDamage = EnchantmentHelper.getEnchantmentModifierLiving(player, living);
            knockback += EnchantmentHelper.getKnockbackModifier(player, living);
        }
        if (player.isSprinting())
            ++knockback;
        if (damage <= 0.0F && magicDamage <= 0.0F)
            return;
        val isCritical = isCriticalDamage(target, player);
        if (isCritical && damage > 0.0F)
            damage *= 1.5F;
        damage += magicDamage;
        var isFired = false;
        val fire = EnchantmentHelper.getFireAspectModifier(player);
        if (fire > 0 && canFireEntity(target)) {
            isFired = true;
            target.setFire(1);
        }
        val attackDone = target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
        if (attackDone) {
            if (knockback > 0) {
                target.addVelocity(-MathHelper.sin(player.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F, 0.1D,
                    MathHelper.cos(player.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F);
                player.motionX *= 0.6D;
                player.motionZ *= 0.6D;
                player.setSprinting(false);
            }
            if (isCritical)
                player.onCriticalHit(target);
            if (magicDamage > 0.0F)
                player.onEnchantmentCritical(target);
            if (damage >= 18.0F)
                player.triggerAchievement(AchievementList.overkill);
            player.setLastAttacker(target);
            if (target instanceof EntityLivingBase living) {
                EnchantmentHelper.func_151384_a(living, player);
            }
        }
        val eqItem = player.getCurrentEquippedItem();
        Object targetObj = target;
        if (target instanceof EntityDragonPart part)
            if (part.entityDragonObj instanceof EntityLivingBase)
                targetObj = part.entityDragonObj;
        if (eqItem != null && targetObj instanceof EntityLivingBase living) {
            eqItem.hitEntity(living, player);
            if (eqItem.stackSize <= 0)
                player.destroyCurrentEquippedItem();
        }
        if (target instanceof EntityLivingBase) {
            player.addStat(StatList.damageDealtStat, Math.round(damage * 10.0F));
            if (fire > 0 && attackDone)
                target.setFire(fire * 4);
            else if (isFired)
                target.extinguish();
        }
        player.addExhaustion(0.3F);
    }

    /**
     * Check critical attack.
     *
     * @param target - target entity.
     * @param player - attacker.
     * @return True when attack is critical.
     * @author Azanor
     */
    public boolean isCriticalDamage(@Nullable Entity target, @NotNull EntityPlayer player) {
        return player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater()
            && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null
            && target instanceof EntityLivingBase;
    }

    /**
     * Can fire entity.
     *
     * @param target - target entity.
     * @return True if it can fire target.
     * @author Azanor
     */
    public boolean canFireEntity(@Nullable Entity target) {
        return target instanceof EntityLivingBase && !target.isBurning();
    }
}
