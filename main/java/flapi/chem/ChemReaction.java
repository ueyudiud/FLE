package flapi.chem;

import java.util.ArrayList;
import java.util.List;

import flapi.chem.base.ContainInput;
import flapi.chem.base.IChemCondition;
import flapi.chem.base.IMatterInputHatch;
import flapi.chem.base.Matter;
import flapi.chem.base.MatterStack;
import flapi.chem.base.ScaleInput;
import flapi.collection.abs.Stack;

public class ChemReaction
{
	public static int matchAndInputRecipe(IChemCondition condition, IMatterInputHatch hatch, ScaleInput recipe)
	{
		int scale = match(condition, hatch, recipe);
		if(scale == -1) return -1;
		else
		{
			for(Stack<Matter> stack : recipe.input)
			{
				for(int i = 0; i < hatch.getMatterHatchSize(); ++i)
				{
					if(hatch.getMatter(i) != null && stack.get().equals(hatch.getMatter(i).getMatter()))
					{
						MatterStack stack1 = hatch.getMatter(i);
						int decr = scale * stack.size / stack1.getPart().resolution;
						hatch.decrMatter(i, decr);
						break;
					}
				}
			}
			return scale;
		}
	}
	public static int matchAndInputRecipe(IChemCondition condition, IMatterInputHatch hatch, ContainInput recipe, int perOutput)
	{
		if(!match(condition, hatch, recipe)) return -1;
		int size = 0;
		for(int i = 0; i < hatch.getMatterHatchSize(); ++i)
		{
			size += hatch.getMatter(i).getContain();
			hatch.setMatter(i, null);
		}
		return size;
	}

	public static int match(IChemCondition condition, IMatterInputHatch hatch, ScaleInput recipe)
	{
		if(!recipe.req.match(condition)) return -1;
		return match(hatch, recipe);
	}
	public static boolean match(IChemCondition condition, IMatterInputHatch hatch, ContainInput recipe)
	{
		if(!recipe.req.match(condition)) return false;
		return match(hatch, recipe);
	}
	
	/**
	 * 
	 * @param hatch
	 * @param recipe
	 * @return The scale of this recipe, return -1 if can't match the recipe.
	 */
	public static int match(IMatterInputHatch hatch, ScaleInput recipe)
	{
		if(hatch == null || recipe == null) return -1;
		List<MatterStack> list = new ArrayList();
		for(int i = 0; i < hatch.getMatterHatchSize(); ++i)
		{
			MatterStack stack = hatch.getMatter(i);
			if(stack != null)
				list.add(stack.copy());
		}
		int scale = 1;
		List<MatterStack> list1 = new ArrayList<MatterStack>(list);
		label :
		for(Stack<Matter> stack : recipe.input)
		{
			for(MatterStack stack1 : list)
			{
				if(stack.get().equals(stack1.getMatter()))
				{
					int resolution = stack1.getPart().resolution;
					int size = stack1.getContain();
					if(scale * stack.size() % resolution != 0)
						scale = minOver(scale * stack.size(), resolution) / stack.size();
					if(size < scale * stack.size()) return -1;
					list1.remove(stack1);
					continue label;
				}
			}
			return -1;
		}
		label:
		for(Stack<Matter> stack : recipe.input)
			for(MatterStack stack1 : list)
			{
				if(stack.get().equals(stack1.getMatter()))
				{
					if(stack1.getContain() % scale * stack.size() != 0)
						return -1;
					continue label;
				}
			}
		return list1.isEmpty() ? scale : -1;
	}
	
	public static boolean match(IMatterInputHatch hatch, ContainInput recipe)
	{
		if(hatch == null || recipe == null) return false;
		List<MatterStack> list = new ArrayList();
		for(int i = 0; i < hatch.getMatterHatchSize(); ++i)
		{
			MatterStack stack = hatch.getMatter(i);
			if(stack != null)
				list.add(stack.copy());
		}
		int maxScale = Integer.MAX_VALUE;
		int minScale = 0;
		List<Matter> list1 = new ArrayList<Matter>(recipe.input.keySet());
		for(MatterStack stack1 : list)
		{
			int[] i = recipe.getScaleRequire(stack1.getMatter());
			if(i == null) return false;
			if(maxScale * i[1] < stack1.getContain() || 
					minScale * i[0] > stack1.getContain()) return false;
			minScale = stack1.getContain() / i[0];
			maxScale = stack1.getContain() / i[1];
			list1.remove(stack1.getMatter());
		}
		for(MatterStack stack : list)
		{
			int[] i = recipe.getScaleRequire(stack.getMatter());
			if(maxScale * i[1] < stack.getContain() || 
					minScale * i[0] > stack.getContain()) return false;
		}
		return list1.isEmpty();
	}
	
	static int min(int...is)
	{
		int i = Integer.MAX_VALUE;
		for(int j : is)
			if(j < i)
				j = i;
		return i;
	}
	static int max(int...is)
	{
		int i = Integer.MIN_VALUE;
		for(int j : is)
			if(j > i)
				j = i;
		return i;
	}
	static int euclid(int a, int b)
	{
		if(a < b)
		{
            a ^= b;
            b ^= a;
            a ^= b;
        }
		if(0==b)
		{
			return a;
		}
		return euclid(b, a % b);
	}
	static int minOver(int a, int b)
	{
		return a == 0 || b == 0 ? 0 : a * b / euclid(a, b);
	}
}