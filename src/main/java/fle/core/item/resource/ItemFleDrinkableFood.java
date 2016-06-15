package fle.core.item.resource;

import farcore.enums.EnumItem;
import farcore.interfaces.item.IFoodStat;
import farcore.interfaces.item.IItemInfo;
import farcore.lib.collection.IRegister;
import fle.api.item.ItemSmeltableFood;
import fle.core.item.ItemBowl;
import net.minecraft.item.ItemStack;

@Deprecated
public class ItemFleDrinkableFood extends ItemSmeltableFood
{
	public static void registerFoods(ItemBowl item, IRegister<IItemInfo> register)
	{
		
	}
	
	public ItemFleDrinkableFood()
	{
		super("drinkable.food");
		EnumItem.food_drinkable.set(new ItemStack(this));
		init();
	}

	private void init()
	{
	
	}
		
	@Override
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName, IFoodStat stat)
	{
		super.addSubItem(id, name, local, itemInfo, "fle:resource/food/delta/" + iconName, stat);
	}
}