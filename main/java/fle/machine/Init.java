package fle.machine;

import farcore.block.item.ItemBlockBase;
import fle.init.Blocks;
import fle.machine.block.BlockCrucible;

public class Init
{
	public void preLoad()
	{
		Blocks.crucible = new BlockCrucible(ItemBlockBase.class, "crucible");
	}

	public void load()
	{
		
	}

	public void postLoad()
	{
		
	}

	public void completeLoad()
	{
		
	}
}