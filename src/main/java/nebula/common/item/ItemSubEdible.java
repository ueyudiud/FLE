/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.item;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import nebula.client.util.UnlocalizedList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ItemSubEdible extends ItemSubBehavior implements IFoodStat
{
	protected final Map<Integer, IFoodStat> foodstats = new HashMap<>();
	
	protected ItemSubEdible(String name)
	{
		super(name);
	}
	protected ItemSubEdible(String modid, String name)
	{
		super(modid, name);
	}
	
	public void addSubItem(int id, String name, String localName, @Nullable IItemCapabilityProvider provider, @Nullable IFoodStat stat,
			IBehavior... behaviors)
	{
		super.addSubItem(id, name, localName, provider, behaviors);
		if(stat == null)
		{
			stat = IFoodStat.NO_EATABLE;
		}
		this.foodstats.put(id, stat);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand)
	{
		ActionResult<ItemStack> result = super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
		if(result.getType() != EnumActionResult.PASS) return result;
		itemStackIn = result.getResult();
		if(isEdible(itemStackIn, playerIn))
		{
			playerIn.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}
		return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		try
		{
			if(entityLiving instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) entityLiving;
				player.getFoodStats().addStats(null, stack);
				if(!isDrink(stack))
				{
					worldIn.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
				}
				stack = onEat(stack, player);
				return stack;
			}
			return super.onItemUseFinish(stack, worldIn, entityLiving);
		}
		catch (Exception exception)
		{
			return stack;
		}
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return isDrink(stack) ? EnumAction.DRINK : EnumAction.EAT;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return getEatDuration(stack);
	}
	
	@Override
	public float[] getNutritionAmount(ItemStack stack)
	{
		try
		{
			return this.foodstats.get(getBaseDamage(stack)).getNutritionAmount(stack);
		}
		catch (Exception exception)
		{
			return new float[]{0, 0, 0, 0, 0, 0};
		}
	}
	
	@Override
	public float getDrinkAmount(ItemStack stack)
	{
		try
		{
			return this.foodstats.get(getBaseDamage(stack)).getDrinkAmount(stack);
		}
		catch (Exception exception)
		{
			return 0;
		}
	}
	
	@Override
	public float getFoodAmount(ItemStack stack)
	{
		try
		{
			return this.foodstats.get(getBaseDamage(stack)).getFoodAmount(stack);
		}
		catch (Exception exception)
		{
			return 0;
		}
	}
	
	@Override
	public float getSaturation(ItemStack stack)
	{
		try
		{
			return this.foodstats.get(getBaseDamage(stack)).getSaturation(stack);
		}
		catch (Exception exception)
		{
			return 0;
		}
	}
	
	@Override
	public boolean isEdible(ItemStack stack, EntityPlayer player)
	{
		try
		{
			return this.foodstats.get(getBaseDamage(stack)).isEdible(stack, player);
		}
		catch (Exception exception)
		{
			return false;
		}
	}
	
	@Override
	public boolean isWolfEdible(ItemStack stack)
	{
		try
		{
			return this.foodstats.get(getBaseDamage(stack)).isWolfEdible(stack);
		}
		catch (Exception exception)
		{
			return false;
		}
	}
	
	@Override
	public boolean isDrink(ItemStack stack)
	{
		try
		{
			return this.foodstats.get(getBaseDamage(stack)).isDrink(stack);
		}
		catch (Exception exception)
		{
			return false;
		}
	}
	
	@Override
	public int getEatDuration(ItemStack stack)
	{
		try
		{
			return this.foodstats.get(getBaseDamage(stack)).getEatDuration(stack);
		}
		catch (Exception exception)
		{
			return 72000;
		}
	}
	
	@Override
	public ItemStack onEat(ItemStack stack, EntityPlayer player)
	{
		try
		{
			return this.foodstats.get(getBaseDamage(stack)).onEat(stack, player);
		}
		catch (Exception exception)
		{
			return stack;
		}
	}
	
	private static final DecimalFormat FORMAT_1 = new DecimalFormat("#0.0");
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		if(playerIn.capabilities.isCreativeMode)
		{
			IFoodStat stat = this.foodstats.getOrDefault(getBaseDamage(stack), IFoodStat.NO_EATABLE);
			if (stat == IFoodStat.NO_EATABLE) return;
			if(unlocalizedList.isSneakDown())
			{
				unlocalizedList.add("info.food.label");
				try
				{
					unlocalizedList.add("info.food.display", FORMAT_1.format(stat.getFoodAmount(stack)), FORMAT_1.format(stat.getSaturation(stack)), FORMAT_1.format(stat.getDrinkAmount(stack)));
				}
				catch(Exception exception)
				{
					;
				}
			}
			else
			{
				unlocalizedList.addShiftClickInfo();
			}
		}
	}
}