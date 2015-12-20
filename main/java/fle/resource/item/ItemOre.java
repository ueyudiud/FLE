package fle.resource.item;

import java.util.List;

import net.minecraft.item.ItemStack;
import flapi.material.MaterialOre;
import flapi.material.MaterialRock;
import fle.core.init.IB;
import fle.core.item.ItemSub;
import fle.core.item.behavior.BehaviorBlockable;
import fle.core.util.ItemTextureHandler;

public class ItemOre extends ItemSub
{
	public static ItemStack a(MaterialOre material)
	{
		return a(material, 1);
	}
	public static ItemStack a(MaterialOre material, int size)
	{
		return new ItemStack(IB.oreChip, size, MaterialOre.getOreID(material));
	}
	
	public ItemOre(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
	}

	public ItemOre init()
	{
		ItemTextureHandler locate = new ItemTextureHandler("iconsets/chip_rock", "void", "iconsets/chip_ore1", "iconsets/chip_ore2", "void");
		for(MaterialOre ore : MaterialOre.getOres())
		{
			addSubItem(MaterialOre.getOreID(ore), ore.getOreName().toLowerCase(), ore.getOreName() + " Chip", locate, new BehaviorBlockable(9, IB.ore_cobble, MaterialOre.getOreID(ore)));
		}
		return this;
	}
	
	@Override
	protected void addAdditionalToolTips(List aList, ItemStack aStack)
	{
		super.addAdditionalToolTips(aList, aStack);
		aList.add(MaterialOre.getOreFromID(getDamage(aStack)).getChemcalReaction());
	}
	
	@Override
	public int getColorFromItemStack(ItemStack aStack, int aPass)
	{
		switch(aPass)
		{
		case 1 : return MaterialRock.getOreFromID(getDamage(aStack) / 4096).getPropertyInfo().getColors()[0];
		case 2 : return MaterialOre.getOreFromID(getDamage(aStack) % 4096).getPropertyInfo().getColors()[0];
		case 3 : return MaterialOre.getOreFromID(getDamage(aStack) % 4096).getPropertyInfo().getColors()[1];
		default : return 0xFFFFFF;
		}
	}
}