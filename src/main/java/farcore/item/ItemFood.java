package farcore.item;

import java.util.HashMap;
import java.util.Map;

import farcore.interfaces.IItemIconInfo;
import farcore.interfaces.item.IBehavior;
import farcore.interfaces.item.IFoodStat;
import farcore.interfaces.item.IItemInfo;
import farcore.interfaces.item.IItemProperty;
import farcore.util.FarFoodStats;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFood extends ItemSubBehavior
{
	protected final Map<String, IFoodStat> foodStats = new HashMap();
	
	protected ItemFood(String unlocalized)
	{
		super(unlocalized);
	}
	protected ItemFood(String unlocalized, String unlocalizedTooltip)
	{
		super(unlocalized, unlocalizedTooltip);
	}
	protected ItemFood(String modid, String unlocalized, String unlocalizedTooltip)
	{
		super(modid, unlocalized, unlocalizedTooltip);
	}
	
	public void addSubItem(int id, String name, String local, IBehavior behavior, IItemProperty property,
			IItemIconInfo iconInfo, IFoodStat stat)
	{
		super.addSubItem(id, name, local, behavior, property, iconInfo);
		foodStats.put(name, stat);
	}
	
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, IItemIconInfo iconInfo, IFoodStat stat)
	{
		super.addSubItem(id, name, local, itemInfo, iconInfo);
		foodStats.put(name, stat);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		IFoodStat stat = foodStats.get(register.name(getDamage(stack)));
		if(stat != null && player.canEat(stat.alwaysEdible(stack, player)))
		{
			player.setItemInUse(stack, stat.getEatTick(stack));
			return stack;
		}
		return super.onItemRightClick(stack, world, player);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		IFoodStat stat = foodStats.get(register.name(getDamage(stack)));
		if(stat != null)
		{
			return stat.getEatTick(stack);
		}
		return super.getMaxItemUseDuration(stack);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		IFoodStat stat = foodStats.get(register.name(getDamage(stack)));
		if(stat != null)
		{
			return stat.getFoodAction(stack);
		}
		return super.getItemUseAction(stack);
	}
	
	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		IFoodStat stat = foodStats.get(register.name(getDamage(stack)));
		if(stat != null)
		{
			((FarFoodStats) player.getFoodStats()).addFoodDigestionStats(stat.getFoodNutritionInfo(stack, player));
		}
		stat.onEaten(stack, player);
		return (stack = U.Inventorys.valid(stack)) == null ? null : super.onEaten(stack, world, player);
	}
}