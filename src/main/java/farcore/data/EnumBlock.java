package farcore.data;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public enum EnumBlock
{
	crop,
	sapling,
	water,
	fire,
	ice;
	
	static
	{
		water.block = Blocks.WATER;
		fire.block = Blocks.FIRE;
	}
	
	public Block block;
	
	public void set(Block block)
	{
		this.block = block;
	}
}