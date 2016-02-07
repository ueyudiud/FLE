package farcore.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.layer.GenLayer;
import scala.collection.generic.BitOperations.Int;

public final class DropInfo
{
	public static final DropInfo NULL = new DropInfo(0, 0, new int[0], new ItemStack[0]);

	public static DropInfo of(ItemStack stack)
	{
		if(stack == null) return NULL;
		return new DropInfo(1, 1, new int[]{1}, stack);
	}
	public static DropInfo of(ItemStack...stack)
	{
		if(stack == null || stack.length == 0) return NULL;
		if(stack.length == 1) return of(stack[0]);
		int[] is = new int[stack.length];
		Arrays.fill(is, 1);
		return new DropInfo(1, 1, is, stack);
	}
	
	public static DropBuilder builder()
	{
		return new DropBuilder();
	}
	
	public static class DropBuilder
	{
		private int max;
		private int min;
		private List<Integer> weights = new ArrayList();
		private List<ItemStack> drops = new ArrayList();
		
		DropBuilder() {}
		
		public DropBuilder size(int max, int min)
		{
			this.max = max;
			this.min = min;
			return this;
		}
		
		public DropBuilder max(int max)
		{
			this.max = max;
			return this;
		}
		
		public DropBuilder min(int min)
		{
			this.min = min;
			return this;
		}
		
		public DropBuilder size(int size)
		{
			this.max = this.min = size;
			return this;
		}
		
		public DropBuilder add(ItemStack stack, int weight)
		{
			weights.add(weights.size(), weight);
			drops.add(drops.size(), stack != null ? stack.copy() : null);
			return this;
		}
		
		public DropInfo build()
		{
			return new DropInfo(min, max, 
					Util.toInts(weights.toArray(new Integer[weights.size()])), 
					drops.toArray(new ItemStack[drops.size()]));
		}
	}

	private final int allWeight;
	private final int max;
	private final int min;
	private final int[] list;
	private final ItemStack[] drops;
	
	private final long seed1;
	private long seed2;
	
	public DropInfo(int min, int max, int[] list, ItemStack...stacks)
	{
		this.min = min;
		this.max = max;
		this.list = list;
		int a = 0;
		for(int i : list)
			a += i;
		this.allWeight = a;
		this.drops = stacks;
		
		this.seed1 = Util.nextSeed();
		this.seed2 = Util.nextSeed() * 84728294L + seed1;
	}
	
	public List<ItemStack> drops()
	{
		int l = max == min ? min : (next() % (max - min)) + min;
		ArrayList<ItemStack> list = new ArrayList();
		while(--l > 0)
		{
			list.add(nextDrop());
		}
		return list;
	}
	
	public ItemStack nextDrop()
	{
		if(drops == null || drops.length == 0) return null;
		int i = next() % allWeight;
		int j = 0;
		while(i >= 0) i -= list[j++];
		return drops[j - 1] == null ? null : drops[j - 1].copy();
	}
	
	public ItemStack[] getDrops()
	{
		return drops;
	}
	
	private int next()
	{
		seed2 <<= 27;
		seed2 += 396719401732L;
		seed2 ^= seed1;
		return (int) ((((seed2 + 1442695040888963407L) * seed2 + 6364136223846793005L) * seed2 + 8375091749179L) & 0x7FFFFFFF);
	}
}