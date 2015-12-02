package fle.api.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import fle.api.util.IChemCondition;
import fle.api.util.SizeUtil;
import fle.api.util.SizeUtil.I;
import fle.api.util.WeightHelper;
import fle.api.util.WeightHelper.Stack;

public abstract class IReactionHandler
{
	protected IReactionHandler()
	{
		
	}
	
	public abstract boolean match(IChemCondition condition, Map<Matter, Integer> atomMap);
	
	public abstract void onInput(IChemCondition condition, Map<Matter, Integer> atomMap);
	
	public abstract void onOutput(IChemCondition condition, Map<Matter, Integer> atomMap);
	
	public abstract int getEnergyRequire(IChemCondition condition, Map<Matter, Integer> atomMap);
	
	protected I<Matter>[] make(Map<Matter, Integer> ms)
	{
		I<Matter>[] ret = new I[ms.size()];
		int i = 0;
		for(Entry<Matter, Integer> e : ms.entrySet()) ret[i++] = SizeUtil.info(e.getKey(), e.getValue());
		return ret;
	}
}