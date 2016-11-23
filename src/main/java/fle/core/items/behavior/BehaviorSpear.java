package fle.core.items.behavior;

import farcore.data.EnumToolType;
import farcore.data.KS;
import farcore.data.MP;
import farcore.lib.entity.EntityProjectileItem;
import farcore.lib.item.IProjectileItem;
import farcore.lib.item.ITool;
import farcore.lib.item.ItemTool;
import farcore.lib.item.behavior.BehaviorBase;
import farcore.lib.material.Mat;
import farcore.lib.util.DamageSourceProjectile;
import farcore.lib.util.Direction;
import farcore.util.U;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BehaviorSpear extends BehaviorBase implements IProjectileItem
{
	float baseDamage;

	public BehaviorSpear()
	{
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		RayTraceResult result = U.Worlds.rayTrace(worldIn, playerIn, false);
		if(result == null)
		{
			playerIn.setActiveHand(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}
		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft)
	{
		float f = (stack.getMaxItemUseDuration() - timeLeft);
		f /= 20F;
		f = (f * f + f * 3.5F) / 3F;
		if(f < 0.2F) return;
		if(f > 1.0F)
		{
			f = 1.0F;
		}
		ItemStack stack1;
		if(entity instanceof EntityPlayer &&
				((EntityPlayer) entity).capabilities.isCreativeMode)
		{
			stack1 = stack.copy();
			stack1.stackSize = 1;
			if(!world.isRemote)
			{
				KS.HURLING.using((EntityPlayer) entity, 1.0F);
			}
		}
		else
		{
			stack1 = stack.splitStack(1);
		}
		if(!world.isRemote)
		{
			EntityProjectileItem entity1 = new EntityProjectileItem(world, entity, f * 1.0F, stack1, true);
			world.spawnEntityInWorld(entity1);
		}
	}
	
	@Override
	public void initEntity(EntityProjectileItem entity)
	{
	}
	
	@Override
	public void onEntityTick(EntityProjectileItem entity)
	{
		if(entity.inGround && !entity.worldObj.isRemote)
		{
			EntityPlayer player = entity.worldObj.getClosestPlayerToEntity(entity, 0.8F);
			if(player != null && (player.capabilities.isCreativeMode || player.inventory.addItemStackToInventory(entity.currentItem)))
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
		if(target instanceof EntityLivingBase)
		{
			EntityLivingBase entity1 = (EntityLivingBase) target;
			float damage = MathHelper.sqrt_double(entity.motionX * entity.motionX + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ);
			float speed = damage;
			Mat material = ItemTool.getMaterial(entity.currentItem, "head");
			if(material != null)
			{
				damage *= (1F + material.getProperty(MP.property_tool).damageToEntity);
			}
			if(entity.shooter != null)
				if(entity.shooter instanceof EntityPlayer)
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
			float use = entity.worldObj.rand.nextFloat() * speed * 0.2F + 0.8F;
			((ITool) entity.currentItem.getItem()).onToolUse(null, entity.currentItem, EnumToolType.spear, use);
			if(entity.currentItem.stackSize <= 0)
			{
				entity.setDead();
			}
			entity.motionX /= 40F;
			entity.motionZ /= 40F;
		}
		return true;
	}
}