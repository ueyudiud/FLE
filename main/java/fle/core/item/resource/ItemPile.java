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
		addSubItem(1, "gravel", "Gravel Pile", "gravel");
		addSubItem(2, "sand", "Sand Pile", "sand");
		addSubItem(3, "dirt", "Dirt Pile", "dirt");
		addSubItem(4, "humus", "Humus Pile", "humus");
		addSubItem(5, "sludge", "Sludge Pile", "sludge");
	}
	
	@Override
	public void addSubItem(int id, String name, String local, String iconName)
	{
		super.addSubItem(id, name, local, "fle:resource/pile/" + iconName);
	}
}