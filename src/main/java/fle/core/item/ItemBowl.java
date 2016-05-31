package fle.core.item;

import farcore.enums.EnumItem;
import farcore.item.ItemBase;
import farcore.lib.collection.Register;
import fle.api.util.BowlBehavior;
import fle.core.item.resource.ItemFleDrinkableFood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBowl extends ItemBase
{
	private final Register<BowlBehavior> behaviors = new Register();
	
	public ItemBowl()
	{
		super("fle.bowl");
		EnumItem.bowl.set(new ItemStack(this));
		init();
	}
	
	private void init()
	{
		ItemFleDrinkableFood.registerFoods(behaviors);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		BowlBehavior behavior;
		if((behavior = behaviors.get(getDamage(stack))) != null)
		{
			return behavior.onItemRightClick(stack, world, player);
		}
		return stack;
	}
}