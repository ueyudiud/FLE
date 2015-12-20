package fle.tool.item.behavior;

import net.minecraft.item.ItemStack;
import flapi.item.interfaces.ICastingTool;
import fle.core.item.behavior.BehaviorBase;

public class BehaviorCastingTool extends BehaviorBase implements ICastingTool
{
	@Override
	public boolean isCastingTool(ItemStack aStack)
	{
		return true;
	}
}