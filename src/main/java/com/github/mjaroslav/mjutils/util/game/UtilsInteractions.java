package com.github.mjaroslav.mjutils.util.game;

import com.github.mjaroslav.mjutils.mod.lib.General.Creative.BlockBreaking;
import com.github.mjaroslav.mjutils.object.game.item.ItemStackSet;
import com.github.mjaroslav.mjutils.util.game.item.UtilsItemStack;
import com.github.mjaroslav.mjutils.util.game.item.UtilsItemStack.CompareParameter;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * The utilities for interactions that carry
 * any changes in the behavior of the player
 * or mobs.
 */
public class UtilsInteractions {
    private static final ItemStackSet DISABLED_FOR_BREAKING_IN_CREATIVE = new ItemStackSet(CompareParameter.ITEM, CompareParameter.WILDCARD_META);
    private static final ItemStackSet PIGMAN_TRIGGER_BLOCKS = new ItemStackSet(CompareParameter.ITEM, CompareParameter.WILDCARD_META);

    /**
     * To prohibit the breaking of blocks by
     * specified item in the creative mode.
     *
     * @param item  specified item. Metadata will be ignored.
     * @param value true to disable block breaking.
     */
    public static void setDisabledForBlockBreakingInCreative(Item item, boolean value) {
        setDisabledForBlockBreakingInCreative(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), value);
    }

    /**
     * To prohibit the breaking of blocks by
     * specified block in the creative mode.
     *
     * @param block specified block. Metadata will be ignored.
     * @param value true to disable block breaking.
     */
    public static void setDisabledForBlockBreakingInCreative(Block block, boolean value) {
        setDisabledForBlockBreakingInCreative(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE), value);
    }

    /**
     * To prohibit the breaking of blocks by
     * specified ItemStack in the creative mode.
     *
     * @param stack specified ItemStack.
     * @param value true to disable block breaking.
     */
    public static void setDisabledForBlockBreakingInCreative(ItemStack stack, boolean value) {
        DISABLED_FOR_BREAKING_IN_CREATIVE.add(stack);
        DISABLED_FOR_BREAKING_IN_CREATIVE.remove(stack);
    }

    /**
     * Check the possibility of block breaking by
     * the specified ItemStack.
     * Has IMC messages: ("interactions.block_in_creative", ItemStack)
     * and ("interactions.unblock_in_creative", itemStack).
     *
     * @param stack ItemStack to test.
     * @return True if stack can't break blocks.
     */
    public static boolean isBlockBreakingDisabledInCreative(ItemStack stack) {
        if (!UtilsItemStack.isNotEmpty(stack))
            return false;
        val item = stack.getItem();
        if (BlockBreaking.swords && item instanceof ItemSword)
            return true;
        if (BlockBreaking.tools && item instanceof ItemTool)
            return true;
        if (BlockBreaking.axes && item instanceof ItemAxe)
            return true;
        if (BlockBreaking.pickaxes && item instanceof ItemPickaxe)
            return true;
        if (BlockBreaking.shovels && item instanceof ItemSpade)
            return true;
        if (!BlockBreaking.extraItems)
            return false;
        return DISABLED_FOR_BREAKING_IN_CREATIVE.contains(stack);
    }

    /**
     * Make the specified block a trigger for PigZombie.
     * If you destroy it, they will attack the player.
     * Has IMC messages: ("interactions.pigzombie_trigger_block", ItemStack)
     * and ("interactions.not_pigzombie_trigger_block", ItemStack).
     *
     * @param stack item stack with block and metadata.
     * @param value set true if PigZombie should
     *              attack player when specified
     *              block is destroyed.
     */
    public static void setPigmanTriggerBlock(ItemStack stack, boolean value) {
        if (value)
            PIGMAN_TRIGGER_BLOCKS.add(stack);
        else PIGMAN_TRIGGER_BLOCKS.remove(stack);
    }

    /**
     * Make the specified block a trigger for PigZombie.
     * If you destroy it, they will attack the player.
     *
     * @param block specified block. Metadata will be ignored.
     * @param value set true if PigZombie should
     *              attack player when specified
     *              block is destroyed.
     */
    public static void setPigmanTriggerBlock(Block block, boolean value) {
        setPigmanTriggerBlock(block, OreDictionary.WILDCARD_VALUE, value);
    }

    /**
     * Make the specified block a trigger for PigZombie.
     * If you destroy it, they will attack the player.
     *
     * @param block specified block.
     * @param meta  specified metadata. For any metadata use
     *              {@link UtilsInteractions#setPigmanTriggerBlock(Block, boolean)
     *              setPigmanTriggerBlock(Block, boolean)}
     * @param value set true if PigZombie should
     *              attack player when specified
     *              block is destroyed.
     */
    public static void setPigmanTriggerBlock(Block block, int meta, boolean value) {
        setPigmanTriggerBlock(new ItemStack(block, 1, meta), value);
    }

    /**
     * Should a pigman attack a player if
     * the specified block is broken?
     *
     * @param block specified block.
     * @param meta  specified metadata.
     * @return True if pigman should attach player.
     */
    public static boolean blockIsPigmanTrigger(Block block, int meta) {
        return PIGMAN_TRIGGER_BLOCKS.contains(new ItemStack(block, 1, meta));
    }

    /**
     * Inaccurate copy of the method of multi-purpose
     * attack from Thaumcraft.
     *
     * @param target - target entity.
     * @param player - attacker.
     * @author Azanor
     */
    public static void multiPurposeAttack(Entity target, EntityPlayer player) {
        if (MinecraftForge.EVENT_BUS.post(new AttackEntityEvent(player, target)))
            return;
        if (!target.canAttackWithItem() || target.hitByEntity(player))
            return;
        float damage = (float) player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int knockback = 0;
        float magicDamage = 0.0F;
        if (target instanceof EntityLivingBase) {
            magicDamage = EnchantmentHelper.getEnchantmentModifierLiving(player, (EntityLivingBase) target);
            knockback += EnchantmentHelper.getKnockbackModifier(player, (EntityLivingBase) target);
        }
        if (player.isSprinting()) {
            ++knockback;
        }
        if ((damage <= 0.0F) && (magicDamage <= 0.0F))
            return;
        boolean isCritical = isCriticalDamage(target, player);
        if ((isCritical) && (damage > 0.0F)) {
            damage *= 1.5F;
        }
        damage += magicDamage;
        boolean isFired = false;
        int fire = EnchantmentHelper.getFireAspectModifier(player);
        if (fire > 0 && canFireEntity(target)) {
            isFired = true;
            target.setFire(1);
        }
        boolean attackDone = target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
        if (attackDone) {
            if (knockback > 0) {
                target.addVelocity(-MathHelper.sin(player.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F, 0.1D,
                        MathHelper.cos(player.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F);
                player.motionX *= 0.6D;
                player.motionZ *= 0.6D;
                player.setSprinting(false);
            }
            if (isCritical) {
                player.onCriticalHit(target);
            }
            if (magicDamage > 0.0F) {
                player.onEnchantmentCritical(target);
            }
            if (damage >= 18.0F) {
                player.triggerAchievement(AchievementList.overkill);
            }
            player.setLastAttacker(target);
            if (target instanceof EntityLivingBase) {
                EnchantmentHelper.func_151384_a((EntityLivingBase) target, player);
            }
        }
        ItemStack eqItem = player.getCurrentEquippedItem();
        Object targetObj = target;
        if (target instanceof EntityDragonPart) {
            IEntityMultiPart mpEntity = ((EntityDragonPart) target).entityDragonObj;
            if (mpEntity instanceof EntityLivingBase) {
                targetObj = mpEntity;
            }
        }
        if (eqItem != null && targetObj instanceof EntityLivingBase) {
            eqItem.hitEntity((EntityLivingBase) targetObj, player);
            if (eqItem.stackSize <= 0) {
                player.destroyCurrentEquippedItem();
            }
        }
        if (target instanceof EntityLivingBase) {
            player.addStat(StatList.damageDealtStat, Math.round(damage * 10.0F));
            if ((fire > 0) && (attackDone)) {
                target.setFire(fire * 4);
            } else if (isFired) {
                target.extinguish();
            }
        }
        player.addExhaustion(0.3F);
    }

    /**
     * Check critical attack.
     *
     * @param target - target entity.
     * @param player - attacker.
     * @return True if attack is critical.
     * @author Azanor
     */
    public static boolean isCriticalDamage(Entity target, EntityPlayer player) {
        return player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater()
                && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null
                && target instanceof EntityLivingBase;
    }

    /**
     * Can fire entity.
     *
     * @param target - target entity.
     * @return True if can fire target.
     * @author Azanor
     */
    public static boolean canFireEntity(Entity target) {
        return target instanceof EntityLivingBase && !target.isBurning();
    }
}
