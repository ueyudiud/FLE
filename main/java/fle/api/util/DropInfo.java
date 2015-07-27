package fle.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;

public class DropInfo 
{
	private boolean init = false;
	private final int minSize;
	private final int maxSize;
	
	public WeightHelper<ItemStack> drops;
	
	private static final Random rand = new Random();

	public DropInfo(int aSize1, int aSize2, Map<ItemStack, Integer> aDrop)
	{
		drops = new WeightHelper(aDrop);
		minSize = Math.min(aSize1, aSize2);
		maxSize = Math.max(aSize1, aSize2);
	}
	public DropInfo(Map<ItemStack, Integer> aDrop)
	{
		this(4, 4, aDrop);
	}
	public DropInfo(int aSize1, int aSize2, ItemStack aDrop)
	{
		this.drops = new WeightHelper(aDrop);
		minSize = Math.min(aSize1, aSize2);
		maxSize = Math.max(aSize1, aSize2);
	}
	public DropInfo(int aSize, ItemStack aDrop)
	{
		this(aSize, aSize, aDrop);
	}
	public DropInfo(ItemStack aDrop)
	{
		this(1, aDrop);
	}
	
	@Override
	public String toString() 
	{
		return "recipe.drop." + drops.toString();
	}
	
	public ArrayList<ItemStack> getDrops()
	{
		ArrayList<ItemStack> ret = new ArrayList();
		if(drops != null)
		{
			int size;
			if(maxSize != minSize) size = rand.nextInt(maxSize - minSize) + minSize;
			else size = minSize;
			for(int i = 0; i < size; ++i)
			{
				ret.add(drops.randomGet());
			}
		}
		return ret;
	}
}