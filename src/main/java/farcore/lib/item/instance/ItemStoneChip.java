package farcore.lib.item.instance;

import farcore.data.EnumItem;
import farcore.data.MC;
import farcore.lib.block.instance.BlockRock;
import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.entity.EntityProjectileItem;
import farcore.lib.item.IProjectileItem;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.util.DamageSourceProjectile;
import farcore.lib.util.Direction;
import farcore.lib.util.UnlocalizedList;
import farcore.util.U;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStoneChip extends ItemMulti implements IProjectileItem
{
	public ItemStoneChip()
	{
		super(MC.chip_rock);
		enableChemicalFormula = false;
		EnumItem.stone_chip.set(this);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(stack.stackSize < 9)
			return EnumActionResult.PASS;
		if(!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
		{
			pos = pos.offset(facing);
		}
		if(!playerIn.canPlayerEdit(pos, facing, stack))
			return EnumActionResult.FAIL;
		else
		{
			Mat material = getMaterialFromItem(stack);
			if(material.isRock && material.rock instanceof BlockRock)
			{
				if(worldIn.setBlockState(pos, material.rock.getDefaultState().withProperty(BlockRock.ROCK_TYPE, RockType.cobble_art), 3))
				{
					stack.stackSize -= 9;
					return EnumActionResult.SUCCESS;
				}
			}
			return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
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
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 100;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		float f = (getMaxItemUseDuration(stack) - timeLeft);
		f /= 20F;
		f = (f * f + f * 3.5F) / 3F;
		if(f < 0.2F) return;
		if(f > 1.0F)
		{
			f = 1.0F;
		}
		ItemStack stack1;
		if(entityLiving instanceof EntityPlayer &&
				((EntityPlayer) entityLiving).capabilities.isCreativeMode)
		{
			stack1 = stack.copy();
			stack1.stackSize = 1;
		}
		else
		{
			stack1 = stack.splitStack(1);
		}
		if(!worldIn.isRemote)
		{
			EntityProjectileItem entity = new EntityProjectileItem(worldIn, entityLiving, f * 3.0F, stack1);
			worldIn.spawnEntityInWorld(entity);
		}
	}
	
	@Override
	public void initEntity(EntityProjectileItem entity){ }
	
	@Override
	public void onEntityTick(EntityProjectileItem entity){	}
	
	@Override
	public boolean onHitGround(World world, BlockPos pos, EntityProjectileItem entity, Direction direction)
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
			float speed = damage;
			damage *= 0.4F;
			Mat material = getMaterialFromItem(entity.currentItem);
			if(material != null)
			{
				damage *= (1F + material.toolDamageToEntity);
			}
			if(entity.shooter != null)
				if(entity.shooter instanceof EntityPlayer)
				{
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
			if(entity.worldObj.rand.nextFloat() * 4F + 1F < speed)
			{
				entity.setDead();
			}
			return true;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		unlocalizedList.add("info.stone.chip.throwable");
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
	}
}