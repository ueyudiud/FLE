package fle.core.tile.statics;

import java.util.ArrayList;

import farcore.enums.EnumItem;
import farcore.lib.collection.Register;
import farcore.lib.substance.SubstanceRock;
import fle.api.block.BlockSubstance;
import fle.api.block.ItemSubstance;
import fle.api.tile.TileEntitySubstance;
import net.minecraft.item.ItemStack;

public class TileEntityRock extends TileEntitySubstance<SubstanceRock>
{
	@Override
	public Register<SubstanceRock> getRegister()
	{
		return SubstanceRock.getRocks();
	}
	
	@Override
	public void getDrops(BlockSubstance<SubstanceRock> block, ItemSubstance item, ArrayList<ItemStack> list,
			boolean isSilkTouching)
	{
		if(isSilkTouching)
		{
			list.add(item.provide(substance));
		}
		else
		{
			ItemStack stack = EnumItem.cobble_block.instance(1, substance);
			if(stack != null)
			{
				list.add(stack);
			}
		}
	}
}