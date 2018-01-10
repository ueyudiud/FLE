/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.items;

import farcore.blocks.terria.BlockRock;
import farcore.data.EnumPhysicalDamageType;
import farcore.data.EnumRockType;
import farcore.data.KS;
import farcore.data.MC;
import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.block.behavior.RockBehavior;
import farcore.lib.entity.IEntityDamageEffect;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.util.DamageSourceProjectile;
import nebula.client.util.UnlocalizedList;
import nebula.common.entity.EntityProjectileItem;
import nebula.common.item.IProjectileItem;
import nebula.common.util.Direction;
import nebula.common.util.Worlds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
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
	public static float shootStoneChipExp = 0.0F;
	
	public ItemStoneChip()
	{
		super(MC.chip_rock);
		this.enableChemicalFormula = false;
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (stack.stackSize < 9) return EnumActionResult.PASS;
		if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
		{
			pos = pos.offset(facing);
		}
		if (!playerIn.canPlayerEdit(pos, facing, stack))
			return EnumActionResult.FAIL;
		else
		{
			Mat material = getMaterialFromItem(stack);
			if (material.contain(SubTags.ROCK))
			{
				RockBehavior property = material.getProperty(MP.property_rock);
				if (worldIn.setBlockState(pos, property.block.getDefaultState().withProperty(BlockRock.TYPE, EnumRockType.cobble_art), 3))
				{
					stack.stackSize -= 9;
					return EnumActionResult.SUCCESS;
				}
			}
			return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		RayTraceResult result = Worlds.rayTrace(worldIn, playerIn, false);
		if (result == null)
		{
			playerIn.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
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
		if (f < 0.2F) return;
		if (f > 1.0F)
		{
			f = 1.0F;
		}
		ItemStack stack1;
		if (entityLiving instanceof EntityPlayer && ((EntityPlayer) entityLiving).capabilities.isCreativeMode)
		{
			stack1 = stack.copy();
			stack1.stackSize = 1;
			if (!worldIn.isRemote)
			{
				KS.HURLING.using((EntityPlayer) entityLiving, shootStoneChipExp);
			}
		}
		else
		{
			stack1 = stack.splitStack(1);
		}
		if (!worldIn.isRemote)
		{
			float inaccuracy = !(entityLiving instanceof EntityPlayer) ? 1.0F : 3F / (1F + KS.SHOOTING.level((EntityPlayer) entityLiving));
			EntityProjectileItem entity = new EntityProjectileItem(worldIn, entityLiving, f * 3.0F, stack1, inaccuracy);
			if (worldIn.spawnEntity(entity) && entityLiving instanceof EntityPlayer)
			{
				((EntityPlayer) entityLiving).addExhaustion(0.05F);
			}
		}
	}
	
	@Override
	public void initEntity(EntityProjectileItem entity)
	{
	}
	
	@Override
	public void onEntityTick(EntityProjectileItem entity)
	{
	}
	
	@Override
	public boolean onHitGround(World world, BlockPos pos, EntityProjectileItem entity, Direction direction)
	{
		return false;
	}
	
	@Override
	public boolean onHitEntity(World world, Entity target, EntityProjectileItem entity)
	{
		if (target instanceof EntityLivingBase)
		{
			EntityLivingBase entity1 = (EntityLivingBase) target;
			float damage = MathHelper.sqrt(entity.motionX * entity.motionX + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ);
			float speed = damage;
			damage *= 0.4F;
			Mat material = getMaterialFromItem(entity.currentItem);
			if (material != null)
			{
				damage *= (1F + material.toolDamageToEntity);
			}
			if (target instanceof IEntityDamageEffect)
			{
				damage *= ((IEntityDamageEffect) target).getDamageMultiplier(EnumPhysicalDamageType.HIT);
			}
			entity1.playSound(SoundEvents.BLOCK_STONE_HIT, 1.0F, 3.0F);
			if (entity.shooter != null)
				if (entity.shooter instanceof EntityPlayer)
				{
					damage *= 1 + KS.HURLING.level((EntityPlayer) entity.shooter) * 0.05F;
					entity1.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) entity.shooter).setProjectile(), damage);
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
			if (entity.world.rand.nextFloat() * 4F + 1F < speed)
			{
				entity.setDead();
			}
			return true;
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList, boolean advanced)
	{
		unlocalizedList.add("info.stone.chip.throwable");
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
	}
}
