package farcore.lib.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import farcore.lib.stack.AbstractStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ToolDestoryDropRecipes
{
	private static final Map<AbstractStack, Map<IBlockConditionMacher, DropHandler>> map = new HashMap();
	
	public static void add(AbstractStack tool, IBlockConditionMacher macher, DropHandler handler)
	{
		if(!map.containsKey(tool))
		{
			map.put(tool, new HashMap());
		}
		map.get(tool).put(macher, handler);
	}
	
	public static DropHandler match(ItemStack stack, World world, int x, int y, int z, Block block, int meta)
	{
		if(world.getBlock(x, y, z) == block)
		{
			meta = block.getDamageValue(world, x, y, z);
		}
		for(Entry<AbstractStack, Map<IBlockConditionMacher, DropHandler>> entry1 : map.entrySet())
		{
			if(entry1.getKey().contain(stack))
			{
				for(Entry<IBlockConditionMacher, DropHandler> entry2 : entry1.getValue().entrySet())
				{
					if(entry2.getKey().match(world, x, y, z, block, meta))
					{
						return entry2.getValue();
					}
				}
			}
		}
		return null;
	}
}