package fle.core.item.behavior;

import net.minecraft.item.ItemStack;
import fle.api.item.ICastingTool;

public class BehaviorCastingTool extends BehaviorBase implements ICastingTool
{
	@Override
	public boolean isCastingTool(ItemStack aStack)
	{
		return true;
	}
}