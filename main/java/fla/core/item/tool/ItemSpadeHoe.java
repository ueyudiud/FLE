package fla.core.item.tool;

import net.minecraft.item.ItemStack;
import fla.api.util.FlaValue;
import fla.core.FlaCreativeTab;

public class ItemSpadeHoe extends ItemFlaAbstractHoe
{
	public ItemSpadeHoe(ToolMaterial aTm)
	{
		super(aTm);
		setCreativeTab(FlaCreativeTab.fla_new_stone_age_tab);
	}

	@Override
	public String getToolType(ItemStack stack) 
	{
		return FlaValue.spade_hoe;
	}
}