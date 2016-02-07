package fle.dict.item;

import farcore.collection.Register;
import flapi.item.ItemFle;
import net.minecraft.item.ItemStack;

public class ItemOres extends ItemFle
{
	private static final Register<OreTag> register = new Register();
	
	public ItemOres(String unlocalized)
	{
		super(unlocalized);
	}
	
	@Override
	public int getDamage(ItemStack stack)
	{
		String string = setupNBT(stack).getString("meta");
		return register.serial(string);
	}
}