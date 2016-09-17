package farcore.data;

import farcore.lib.item.ItemMulti;
import farcore.lib.item.ItemTool;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class ColorMultiplier
{
	public static final IItemColor MULTI_ITEM_MATERIAL_COLOR =
			(ItemStack stack, int tintIndex) ->
	{
		switch (tintIndex)
		{
		//0 for base.
		case 1 : return ItemMulti.getMaterial(stack).RGB;
		//2 for override.
		default: return 0xFFFFFFFF;
		}
	};
	public static final IItemColor TOOL_ITEM_MATERIAL_COLOR =
			(ItemStack stack, int tintIndex) ->
	{
		return ItemTool.getColor(stack, tintIndex);
	};
}
