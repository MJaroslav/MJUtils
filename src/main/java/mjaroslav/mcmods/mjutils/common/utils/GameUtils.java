package mjaroslav.mcmods.mjutils.common.utils;

import mjaroslav.mcmods.mjutils.MJInfo;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class GameUtils {
	public static void attackEntity(Entity target, EntityPlayer player) {
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
		boolean isCritical = isCricticalDamage(target, player);
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
			if (mpEntity != null && mpEntity instanceof EntityLivingBase) {
				targetObj = (EntityLivingBase) mpEntity;
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

	public static boolean isCricticalDamage(Entity target, EntityPlayer player) {
		return player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater()
				&& !player.isPotionActive(Potion.blindness) && player.ridingEntity == null
				&& target instanceof EntityLivingBase;
	}

	public static boolean canFireEntity(Entity target) {
		return target instanceof EntityLivingBase && !target.isBurning();
	}

	public static int getTicksFromSeconds(int seconds) {
		return seconds * MJInfo.ticksInSecond;
	}

	public static int getTicksFromMinutes(int minutes) {
		return minutes * MJInfo.ticksInMinute;
	}

	public static int getTicksFromSeconds(int seconds, int mills) {
		return seconds * MJInfo.ticksInSecond + Math.round((mills / 1000) * MJInfo.ticksInSecond);
	}

	public static int getTicksFromMinutes(int minutes, int seconds) {
		return minutes * MJInfo.ticksInMinute + seconds * MJInfo.ticksInSecond;
	}

	public static int getTicksFromSmelting(int count) {
		return count * MJInfo.ticksInSmelt;
	}

	public static int getTicksFromSmelting(float count) {
		return Math.round(count * MJInfo.ticksInSmelt);
	}

	public static boolean itemStackNotNull(ItemStack stack) {
		return stack != null ? stack.getItem() != null : false;
	}

	public static boolean itemStackNotNullAndEquals(ItemStack first, ItemStack second) {
		return first != null && second != null
				? first.getItem() != null && second.getItem() != null ? first.getItem() == second.getItem() : false
				: false;
	}

	public static boolean itemStackNotNullAndEquals(ItemStack first, Item second) {
		return first != null ? first.getItem() != null ? first.getItem() == second : false : false;
	}

	public static boolean itemStackNotNullAndEquals(ItemStack first, Block block) {
		return itemStackNotNullAndEquals(first, Item.getItemFromBlock(block));
	}

	public static boolean itemStacksEquals(ItemStack itemStack, ItemStack itemStack1) {
		return itemStack1.getItem() == itemStack.getItem()
				&& (itemStack1.getItemDamage() == MJInfo.anyMeta || itemStack1.getItemDamage() == itemStack.getItemDamage());
	}
}
