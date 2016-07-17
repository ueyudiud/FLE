package farcore.lib.item.instance;

import farcore.data.EnumItem;
import farcore.data.MC;
import farcore.lib.block.instance.BlockRock;
import farcore.lib.entity.EntityProjectileItem;
import farcore.lib.item.IProjectileItem;
import farcore.lib.item.ItemGrouped;
import farcore.lib.material.Mat;
import farcore.lib.util.DamageSourceProjectile;
import farcore.lib.util.Direction;
import farcore.util.U;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemStoneChip extends ItemGrouped implements IProjectileItem
{
	public ItemStoneChip()
	{
		super("farcore", MC.chip);
		EnumItem.stone_chip.set(this);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ)
	{
		if(stack.stackSize < 9)
			return false;
		if(!world.getBlock(x, y, z).isReplaceable(world, x, y, z))
		{
			Direction dir = Direction.directions[side];
			x += dir.x;
			y += dir.y;
			z += dir.z;
		}
		if(!player.canPlayerEdit(x, y, z, side, stack))
			return false;
		else
		{
			Mat material = Mat.register.get(getDamage(stack));
			if(material != null && material.rock instanceof BlockRock)
				if(world.setBlock(x, y, z, material.rock, 2, 3))
				{
					stack.stackSize -= 9;
					return true;
				}
			return false;
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		MovingObjectPosition mop = U.Worlds.getMovingObjectPosition(world, player, false);
		if(mop.typeOfHit == MovingObjectType.MISS)
		{
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
			return stack;
		}
		return super.onItemRightClick(stack, world, player);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 3600;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int tick)
	{
		float f = tick;
		f /= 20F;
		f = (f * f + f * 3.5F) / 3F;
		if(f < 0.1F) return;
		if(f > 1.0F) f = 1.0F;
		ItemStack stack1;
		if(player.capabilities.isCreativeMode)
		{
			stack1 = stack.copy();
			stack1.stackSize = 1;
		}
		else
			stack1 = stack.splitStack(1);
		if(!world.isRemote)
		{
			EntityProjectileItem entity = new EntityProjectileItem(world, player, f * 1.5F, stack1);
			world.spawnEntityInWorld(entity);
		}
	}

	@Override
	public void initEntity(EntityProjectileItem entity){	}

	@Override
	public void onEntityTick(EntityProjectileItem entity){	}

	@Override
	public boolean onHitGround(World world, int x, int y, int z, EntityProjectileItem entity)
	{
		return false;
	}

	@Override
	public boolean onHitEntity(World world, Entity target, EntityProjectileItem entity)
	{
		if(target instanceof EntityLivingBase)
		{
			EntityLivingBase entity1 = (EntityLivingBase) target;
			float damage = MathHelper.sqrt_double(entity.motionX * entity.motionX + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ);
			damage *= 0.2F;
			Mat material = Mat.register.get(getDamage(entity.currentItem));
			if(material != null)
				damage *= (1F + material.toolDamageToEntity);
			entity1.attackEntityFrom(DamageSourceProjectile.instance, damage);
			entity1.addVelocity(entity.motionX * .3, entity.motionY * .3 + 0.1, entity.motionZ * .3);
			return true;
		}
		return false;
	}
}