package farcore.lib.recipe;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import farcore.lib.collection.Ety;
import farcore.lib.stack.AbstractStack;
import farcore.util.U;
import net.minecraft.item.ItemStack;

public class DropHandler
{	
	private static class DropInfo
	{
		ItemStack drop;
		int minSize;
		int maxSize;
		float chance;

		public ItemStack nextDrop(Random random)
		{
			return random.nextFloat() < chance ?
					minSize == -1 ?
							drop.copy() : minSize != maxSize ?
								U.Inventorys.sizeOf(drop, minSize + random.nextInt(maxSize - minSize)) :
									U.Inventorys.sizeOf(drop, minSize) : null;
		}
		public ItemStack maxDrop()
		{
			return minSize == -1 ? drop.copy() : U.Inventorys.sizeOf(drop, maxSize);
		}
	}

	public static final DropHandler EMPTY = new DropHandler(0F, 0, 0);

	public static Entry<DropInfo, Integer> info(AbstractStack stack)
	{
		return info(stack, 1);
	}
	public static Entry<DropInfo, Integer> info(AbstractStack stack, int weight)
	{
		return info(stack, 1.0F, weight);
	}
	public static Entry<DropInfo, Integer> info(AbstractStack stack, float chance, int weight)
	{
		return info(stack, -1, -1, chance, weight);
	}
	public static Entry<DropInfo, Integer> info(AbstractStack stack, int minSize, int maxSize, float chance, int weight)
	{
		return new Ety($(stack, minSize, maxSize, chance), weight);
	}

	private static DropInfo $(ItemStack stack)
	{
		return $(stack, -1, -1, 1.0F);
	}
	private static DropInfo $(AbstractStack stack, int minSize, int maxSize, float chance)
	{
		DropInfo info = new DropInfo();
		info.drop = stack.instance();
		info.maxSize = maxSize;
		info.minSize = minSize;
		info.chance = chance;
		return info;
	}
	private static DropInfo $(ItemStack stack, int minSize, int maxSize, float chance)
	{
		DropInfo info = new DropInfo();
		info.drop = stack.copy();
		info.maxSize = maxSize;
		info.minSize = minSize;
		info.chance = chance;
		return info;
	}
	
	private float chance;
	private int maxWeight;
	private int minWeight;
	private int allWeight;
	private DropInfo[] stacksList;
	private int[] weightList;

	public DropHandler(int size, Entry<?, Integer>...stacks)
	{
		this(size, size, stacks);
	}
	public DropHandler(int max, int min, Entry<?, Integer>...stacks)
	{
		this(1F, max, min, stacks);
	}
	public DropHandler(float chance, int max, int min, Entry<?, Integer>...stacks)
	{
		this.chance = chance;
		this.maxWeight = max;
		this.minWeight = min;
		int size = 0;
		stacksList = new DropInfo[stacks.length];
		weightList = new int[stacks.length];
		for(int i = 0; i < stacks.length; ++i)
		{
			Entry<?, Integer> entry = stacks[i];
			size += entry.getValue().intValue();
			Object contain = entry.getKey();
			if(contain instanceof ItemStack)
				stacksList[i] = $((ItemStack) contain);
			else if(contain instanceof AbstractStack)
				stacksList[i] = $(((AbstractStack) contain).instance());
			else if(contain instanceof DropInfo)
				stacksList[i] = (DropInfo) contain;
			else throw new IllegalArgumentException("Invalid drop type.");
			weightList[i] = entry.getValue().intValue();
		}
		allWeight = size;
	}
	
	public ArrayList<ItemStack> randomDrops(Random random)
	{
		int weight;
		if(minWeight == maxWeight)
		{
			weight = minWeight;
		}
		else
		{
			weight = minWeight + random.nextInt(maxWeight - minWeight);
		}
		ArrayList<ItemStack> ret = new ArrayList();
		if(random.nextFloat() < chance)
		{
			while(weight-- > 0)
			{
				ItemStack stack = randomDrop(random);
				if(stack != null)
				{
					ret.add(stack);
				}
			}
		}
		return ret;
	}
	
	public ItemStack randomDrop(Random random)
	{
		if(this == EMPTY) return null;
		int i = random.nextInt(allWeight);
		int j = 0;
		int k = 0;
		do
		{
			j += weightList[k]; 
			if(j > i)
			{
				return stacksList[k].nextDrop(random);
			}
		}
		while(++k < stacksList.length);
		return null;
	}
	
	private ArrayList<ItemStack> maxDrop;
	
	public ArrayList<ItemStack> maxDrop()
	{
		if(maxDrop == null)
		{
			maxDrop = new ArrayList<ItemStack>();
			for(DropInfo info : stacksList)
			{
				maxDrop.add(info.maxDrop());
			}
		}
		return new ArrayList(maxDrop);
	}
}