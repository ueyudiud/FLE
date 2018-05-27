/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.items.behavior;

import farcore.data.EnumPhysicalDamageType;
import farcore.data.EnumToolTypes;
import farcore.data.KS;
import farcore.lib.entity.IEntityDamageEffect;
import farcore.lib.item.ItemTool;
import farcore.lib.material.Mat;
import farcore.lib.util.DamageSourceProjectile;
import nebula.common.entity.EntityProjectileItem;
import nebula.common.item.BehaviorBase;
import nebula.common.item.IProjectileItem;
import nebula.common.item.ITool;
import nebula.common.util.Direction;
import nebula.common.util.Entities;
import nebula.common.util.W;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BehaviorSpear extends BehaviorBase implements IProjectileItem
{
	float baseDamage;
	
	public BehaviorSpear()
	{
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		RayTraceResult result = W.rayTrace(worldIn, playerIn, false);
		if (result == null)
		{
			playerIn.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}
		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft)
	{
		float f = (stack.getMaxItemUseDuration() - timeLeft) / 20F;
		f = f * (f + 3.5F) / 3F;
		if (f < 0.2F) return;
		if (f > 1.0F)
		{
			f = 1.0F;
		}
		ItemStack stack1;
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)
		{
			stack1 = stack.copy();
			stack1.stackSize = 1;
		}
		else
		{
			stack1 = stack.splitStack(1);
			if (!world.isRemote)
			{
				KS.HURLING.using((EntityPlayer) entity, 1.0F);
			}
		}
		if (!world.isRemote)
		{
			float inaccuracy = !(entity instanceof EntityPlayer) ? 1.0F : 3F / (1F + KS.SHOOTING.level((EntityPlayer) entity) * 0.4F);
			EntityProjectileItem entity1 = new EntityProjectileItem(world, entity, f * 1.0F, stack1, inaccuracy);
			world.spawnEntity(entity1);
		}
	}
	
	@Override
	public void initEntity(EntityProjectileItem entity)
	{
	}
	
	@Override
	public void onEntityTick(EntityProjectileItem entity)
	{
		if (entity.inGround && !entity.world.isRemote)
		{
			EntityPlayer player = entity.world.getClosestPlayerToEntity(entity, 0.8F);
			if (player != null && (player.capabilities.isCreativeMode || player.inventory.addItemStackToInventory(entity.currentItem)))
			{
				entity.setDead();
			}
		}
	}
	
	@Override
	public boolean onHitGround(World world, BlockPos pos, EntityProjectileItem entity, Direction direction)
	{
		return true;
	}
	
	@Override
	public boolean onHitEntity(World world, Entity target, EntityProjectileItem entity)
	{
		if (target instanceof EntityLivingBase)
		{
			EntityLivingBase entity1 = (EntityLivingBase) target;
			float damage = (float) Entities.velocity(entity);
			float speed = damage;
			Mat material = ItemTool.getMaterial(entity.currentItem, "head");
			if (material != null)
			{
				damage *= (1F + material.toolDamageToEntity);
			}
			if (target instanceof IEntityDamageEffect)
			{
				damage *= ((IEntityDamageEffect) target).getDamageMultiplier(EnumPhysicalDamageType.PUNCTURE);
			}
			if (entity.shooter != null)
				if (entity.shooter instanceof EntityPlayer)
				{
					damage *= 1 + KS.HURLING.level((EntityPlayer) entity.shooter) * 0.05F;
					entity1.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) entity.shooter), damage);
				}
				else
				{
					entity1.attackEntityFrom(DamageSource.causeMobDamage(entity.shooter), damage);
				}
			else
			{
				entity1.attackEntityFrom(DamageSourceProjectile.instance, damage);
			}
			entity1.addVelocity(entity.motionX * .02, entity.motionY * .02, entity.motionZ * .02);
			float use = entity.world.rand.nextFloat() * speed * 0.2F + 0.8F;
			((ITool) entity.currentItem.getItem()).onToolUse(null, entity.currentItem, EnumToolTypes.SPEAR, use);
			if (entity.currentItem.stackSize <= 0)
			{
				entity.setDead();
			}
			entity.motionX /= 40F;
			entity.motionZ /= 40F;
		}
		return true;
	}
}
