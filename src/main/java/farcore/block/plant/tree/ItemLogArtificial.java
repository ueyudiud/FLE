package farcore.block.plant.tree;

import java.util.HashMap;
import java.util.Map;

import farcore.block.ItemBlockBase;
import farcore.enums.EnumItem;
import farcore.enums.EnumItem.IInfomationable;
import farcore.lib.substance.SubstanceWood;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemLogArtificial extends ItemBlockBase implements IInfomationable
{
	public static Map<String, BlockLogArtificial> map;
	
	public ItemLogArtificial(Block block)
	{
		super(block);
	}

	@Override
	public ItemStack provide(int size, Object... objects)
	{
		if(objects.length == 1)
		{
			Block block;
			if(objects[0] instanceof SubstanceWood)
			{
				block = map.get(((SubstanceWood) objects[0]).getName());
			}
			else
			{
				block = map.get(objects[0]);
			}
			if(block != null)
			{
				return new ItemStack(block, size);
			}
		}
		return null;
	}
}