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
		switch (tintIndex)
		{
		//0 for base.
		case 1 : return ItemTool.getMaterial(stack, "head").RGB;
		//2 for handle color, but it seems useless.
		//case 2 : return ItemTool.getMaterial(stack, "handle").RGB;
		//3 for base override.
		case 4 : return ItemTool.getMaterial(stack, "tie").RGB;
		case 5 : return ItemTool.getMaterial(stack, "rust").RGB;
		case 6 : return ItemTool.getMaterial(stack, "inlay").RGB;
		//7 for extended override.
		default: return 0xFFFFFFFF;
		}
	};
}
