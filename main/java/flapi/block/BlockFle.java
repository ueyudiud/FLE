package flapi.block;

import farcore.block.BlockBase;
import flapi.util.Values;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockFle extends BlockBase
{
	protected BlockFle(String unlocalized, Material Material)
	{
		super(unlocalized, Material);
	}
	
	protected BlockFle(Class<? extends ItemBlock> clazz, String unlocalized, Material Material)
	{
		super(clazz, unlocalized, Material);
	}
}