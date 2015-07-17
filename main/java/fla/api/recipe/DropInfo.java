package fla.api.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;

public class DropInfo 
{
	private boolean init = false;
	private final int dropSize;
	private int allWeight;
	
	public IItemChecker d;
	public Map<ItemStack, Integer> drops = new HashMap();
	
	private static final Random rand = new Random();

	public DropInfo(int size, IItemChecker dust, Map<ItemStack, Integer> drop)
	{
		this.d = dust;
		this.drops = drop;
		this.dropSize = size;
	}
	public DropInfo(IItemChecker dust, Map<ItemStack, Integer> drop)
	{
		this(4, dust, drop);
	}
	public DropInfo(ItemStack drop)
	{
		this.d = null;
		Map<ItemStack, Integer> map = new HashMap();
		map.put(drop, 1);
		this.drops = map;
		this.dropSize = 1;
	}
	
	void init()
	{
		for(ItemStack stack : drops.keySet())
		{
			allWeight += drops.get(stack);
		}
		init = true;
	}
	
	@Override
	public String toString() 
	{
		return "recipeinfo.dust." + d.toString() + "." + drops.toString();
	}
	
	public ArrayList<ItemStack> getDustDrop()
	{
		if(!init) init();

		ArrayList<ItemStack> ret = new ArrayList();
		if(drops != null)
		{
			for(int i = 0; i < dropSize; ++i)
			{
				int a = rand.nextInt(allWeight);
				int b = 0;
				Iterator<ItemStack> itr = drops.keySet().iterator();
				while (itr.hasNext()) 
				{
					ItemStack stack = itr.next();
					b += drops.get(stack);
					if(a > b - 1) 
					{
						continue;
					}
					else
					{
						ret.add(stack.copy());
						break;
					}
				}
			}
		}
		return ret;
	}
}
