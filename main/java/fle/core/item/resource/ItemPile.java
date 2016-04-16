package fle.core.item.resource;

import farcore.enums.EnumItem;
import fle.api.item.ItemResource;
import net.minecraft.item.ItemStack;

public class ItemPile extends ItemResource
{
	public ItemPile()
	{
		super("pile");
		EnumItem.pile.set(new ItemStack(this));
		init();
	}

	private void init()
	{
		addSubItem(1, "gravel", "gravel");
		addSubItem(2, "sand", "sand");
		addSubItem(3, "dirt", "dirt");
		addSubItem(4, "humus", "humus");
		addSubItem(5, "sludge", "sludge");
	}
	
	@Override
	public void addSubItem(int id, String name, String iconName)
	{
		super.addSubItem(id, name, "fle:resource/pile/" + iconName);
	}
}