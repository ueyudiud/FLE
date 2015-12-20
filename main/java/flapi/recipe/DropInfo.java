package flapi.recipe;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;
import flapi.collection.ArrayStandardStackList;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;

public class DropInfo 
{
	private final int minSize;
	private final int maxSize;
	private final float chance;
	
	public IStackList<Stack<ItemStack>, ItemStack> drops;
	
	private static final Random rand = new Random();

	public DropInfo(int aSize1, int aSize2, float aChance, Map<ItemStack, Integer> aDrop)
	{
		drops = new ArrayStandardStackList(aDrop);
		minSize = Math.min(aSize1, aSize2);
		maxSize = Math.max(aSize1, aSize2);
		chance = aChance;
	}
	public DropInfo(int aSize1, int aSize2, Map<ItemStack, Integer> aDrop)
	{
		this(aSize1, aSize2, 1.0F, aDrop);
	}
	public DropInfo(float chance, Map<ItemStack, Integer> aDrop)
	{
		this(1, 1, chance, aDrop);
	}
	public DropInfo(Map<ItemStack, Integer> aDrop)
	{
		this(4, 4, aDrop);
	}
	public DropInfo(int aSize1, int aSize2, ItemStack aDrop)
	{
		this.drops = new ArrayStandardStackList(new Stack(aDrop));
		minSize = Math.min(aSize1, aSize2);
		maxSize = Math.max(aSize1, aSize2);
		chance = 1.0F;
	}
	public DropInfo(int aSize, ItemStack aDrop)
	{
		this(aSize, aSize, aDrop);
	}
	public DropInfo(ItemStack aDrop)
	{
		this(1, aDrop);
	}
	
	DropInfo()
	{
		maxSize = minSize = 0;
		chance = 0.0F;
	}
	
	@Override
	public String toString() 
	{
		return "recipe.drop." + drops.toString();
	}
	
	public ArrayList<ItemStack> getDrops()
	{
		ArrayList<ItemStack> ret = new ArrayList();
		if(drops != null && rand.nextDouble() < chance)
		{
			int size;
			if(maxSize != minSize) size = rand.nextInt(maxSize - minSize) + minSize;
			else size = minSize;
			for(int i = 0; i < size; ++i)
			{
				ret.add(drops.randomGet().copy());
			}
		}
		return ret;
	}
	
	public float getDrop()
	{
		return (float) (maxSize + minSize) / 2F;
	}
	
	public static DropInfo empty()
	{
		return new DropInfo();
	}
}