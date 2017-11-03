package mjaroslav.mcmods.mjutils.common.utils;

import java.util.List;

import mjaroslav.mcmods.mjutils.lib.MJInfo;
import mjaroslav.mcmods.mjutils.lib.TimeConstants;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

/**
 * Minecraft game utils.
 * 
 * @author MJaroslav
 *
 */
public class GameUtils {
	/**
	 * Inaccurate copy of the method of multi-purpose attack from Thaumcraft.
	 * 
	 * @param target
	 *            - target entity.
	 * @param player
	 *            - attacker.
	 * @author Azanor
	 */
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

	/**
	 * Check critical attack.
	 * 
	 * @param target
	 *            - target entity.
	 * @param player
	 *            - attacker.
	 * @author Azanor
	 * @return True if attack is critical.
	 */
	public static boolean isCricticalDamage(Entity target, EntityPlayer player) {
		return player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater()
				&& !player.isPotionActive(Potion.blindness) && player.ridingEntity == null
				&& target instanceof EntityLivingBase;
	}

	/**
	 * Can fire entity.
	 * 
	 * @param target
	 *            - target entity.
	 * @param player
	 *            - attacker.
	 * @author Azanor
	 * @return True if can fire target.
	 */
	public static boolean canFireEntity(Entity target) {
		return target instanceof EntityLivingBase && !target.isBurning();
	}

	/**
	 * Change seconds to ticks.
	 * 
	 * @param seconds
	 *            - seconds to change.
	 * @return Seconds in ticks.
	 */
	public static int getTicksFromSeconds(int seconds) {
		return seconds * TimeConstants.SECOND;
	}

	/**
	 * Change minutes to ticks.
	 * 
	 * @param minutes
	 *            - minutes to change.
	 * @return Minutes in ticks.
	 */
	public static int getTicksFromMinutes(int minutes) {
		return minutes * TimeConstants.MINUTE;
	}

	/**
	 * Change seconds and milliseconds to ticks.
	 * 
	 * @param seconds
	 *            - seconds to change.
	 * @param mills
	 *            - milliseconds to change.
	 * @return Seconds and milliseconds in ticks.
	 */
	public static int getTicksFromSeconds(int seconds, int mills) {
		return seconds * TimeConstants.SECOND + Math.round((mills / 1000) * TimeConstants.SECOND);
	}

	/**
	 * Change minutes and seconds to ticks.
	 * 
	 * @param minutes
	 *            - minutes to change.
	 * @param seconds
	 *            - seconds to change.
	 * @return Seconds to ticks.
	 */
	public static int getTicksFromMinutes(int minutes, int seconds) {
		return minutes * TimeConstants.MINUTE + seconds * TimeConstants.SECOND;
	}

	/**
	 * Change burnt items to ticks.
	 * 
	 * @param count
	 *            - burnt items.
	 * @return Burnt items in ticks.
	 */
	public static int getTicksFromSmelting(int count) {
		return count * TimeConstants.ONE_SMELT;
	}

	/**
	 * Change burnt items to ticks.
	 * 
	 * @param count
	 *            - burnt items.
	 * @return Burnt items in ticks.
	 */
	public static int getTicksFromSmelting(float count) {
		return Math.round(count * TimeConstants.ONE_SMELT);
	}

	/**
	 * Change ticks to burnt items.
	 * 
	 * @param ticks
	 *            - ticks to change.
	 * @return Ticks in burnt items.
	 */
	public static float getSmeltingCountFromTicks(int ticks) {
		return ticks / TimeConstants.ONE_SMELT;
	}

	public static boolean itemStackNotNull(ItemStack stack) {
		return stack != null ? stack.getItem() != null : false;
	}

	public static boolean itemStackNotNullAndEquals(ItemStack first, ItemStack second) {
		return first != null && second != null ? itemStacksEquals(first, second) ? true : false : false;
	}

	public static boolean itemStackNotNullAndEquals(ItemStack first, Item second) {
		return first != null ? itemStacksEquals(first, new ItemStack(second, 1, MJInfo.anyMeta)) ? true : false : false;
	}

	public static boolean itemStackNotNullAndEquals(ItemStack first, Block second) {
		return first != null ? itemStacksEquals(first, new ItemStack(second, 1, MJInfo.anyMeta)) ? true : false : false;
	}

	public static boolean itemStacksEquals(ItemStack itemStack, ItemStack itemStack1) {
		return itemStack1.getItem() == itemStack.getItem() && (itemStack1.getItemDamage() == MJInfo.anyMeta
				|| itemStack1.getItemDamage() == itemStack.getItemDamage());
	}
}
