package fle.api.item;

import java.util.List;

import com.google.common.collect.ObjectArrays;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumItem.IInfomationable;
import farcore.interfaces.item.IContainerCustomBehaviorItem;
import farcore.interfaces.item.IFoodStat;
import farcore.interfaces.item.IItemInfo;
import farcore.item.ItemFood;
import farcore.lib.render.item.ItemRenderInfoSimple;
import farcore.util.U;
import fle.api.item.behavior.BehaviorBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDividableFood extends ItemFood implements IInfomationable, IContainerCustomBehaviorItem
{
	public static final String foodAmountKey = "amount";
	public static final float maxFoodAmount = 256F;
	
	public static boolean areFoodSimilar(ItemStack stack1, ItemStack stack2, String...ignores)
	{
		return U.Inventorys.areStackSimilarWithIgnore(stack1, stack2, ObjectArrays.concat(foodAmountKey, ignores));
	}
	
	public static float getFoodAmount(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getFloat(foodAmountKey);
	}
	
	public static void setFoodAmount(ItemStack stack, float amount)
	{
		U.Inventorys.setupNBT(stack, true).setFloat(foodAmountKey, amount);
	}
	
	public static float useAndDecrFood(ItemStack stack, float maxUse)
	{
		float amount = getFoodAmount(stack);
		if(amount > maxUse)
		{
			setFoodAmount(stack, amount - maxUse);
			return maxUse;
		}
		else
		{
			stack.stackSize--;
			return amount;
		}
	}
	
	protected ItemDividableFood(String unlocalized)
	{
		super(unlocalized);
	}
	protected ItemDividableFood(String unlocalized, String unlocalizedTooltip)
	{
		super(unlocalized, unlocalizedTooltip);
	}
	protected ItemDividableFood(String modid, String unlocalized, String unlocalizedTooltip)
	{
		super(modid, unlocalized, unlocalizedTooltip);
	}
	
	public void addSubItem(int id, String name, String local, String iconName, IFoodStat stat)
	{
		addSubItem(id, name, local, BehaviorBase.SIMPLE, iconName, stat);
	}
	
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName, IFoodStat stat)
	{
		super.addSubItem(id, name, local, itemInfo, new ItemRenderInfoSimple(iconName), stat);
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(String name : register.names())
		{
			ItemStack stack = new ItemStack(item, 1, register.id(name));
			setFoodAmount(stack, maxFoodAmount / 10F);
			list.add(stack);
		}
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return 1;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return (double) ((maxFoodAmount - getFoodAmount(stack)) / maxFoodAmount);
	}
	
	@Override
	public boolean canAccessStack(Slot slot, ItemStack stack, ItemStack target)
	{
		return areFoodSimilar(stack, target);
	}
	
	@Override
	public ItemStack splitSize(Slot slot, ItemStack stack, ItemStack target, boolean clickMode)
	{
		if(clickMode)
		{
			float amount = getFoodAmount(stack);
			float amount1 = getFoodAmount(target);
			amount = (amount + amount1);
			if(amount > maxFoodAmount)
			{
				amount -= maxFoodAmount;
				setFoodAmount(stack, maxFoodAmount);
				setFoodAmount(target, amount1);
				slot.putStack(stack);
				return target;
			}
			else
			{
				setFoodAmount(stack, amount);
				slot.putStack(stack);
				return null;
			}
		}
		else
		{
			float amount = getFoodAmount(stack);
			float amount1 = getFoodAmount(target);
			amount1 = amount + amount1;
			amount = Math.min(amount + 16F, amount1);
			if(amount > maxFoodAmount)
			{
				setFoodAmount(stack, maxFoodAmount);
				setFoodAmount(target, amount1 - maxFoodAmount);
				slot.putStack(stack);
				return target;
			}
			else
			{
				setFoodAmount(stack, amount);
				slot.putStack(stack);
				if(amount1 > amount)
				{
					setFoodAmount(target, amount1 - amount);
					return target;
				}
				else
				{
					return null;
				}
			}
		}
	}
	
	@Override
	public ItemStack splitItemWhenRightClick(Slot slot, ItemStack stack)
	{
		float amount = getFoodAmount(stack);
		if(amount > 0.1F)
		{
			setFoodAmount(stack, amount / 2F);
			slot.putStack(stack);
			return stack.copy();
		}
		else
		{
			slot.putStack(null);
			return stack;
		}
	}
	
	@Override
	public ItemStack provide(int size, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof String)
			{
				return new ItemStack(this, size, register.id((String) objects[0]));
			}
			else if(objects[0] instanceof Number)
			{
				return new ItemStack(this, size, ((Number) objects[0]).intValue());
			}
		}
		else if(objects.length == 2)
		{
			if(objects[1] instanceof Number)
			{
				if(objects[0] instanceof String)
				{
					ItemStack stack = new ItemStack(this, size, register.id((String) objects[0]));
					setFoodAmount(stack, ((Number) objects[1]).floatValue());
					return stack;
				}
				else if(objects[0] instanceof Number)
				{
					ItemStack stack = new ItemStack(this, size, ((Number) objects[0]).intValue());
					setFoodAmount(stack, ((Number) objects[1]).floatValue());
					return stack;
				}
			}
		}
		return null;
	}
}