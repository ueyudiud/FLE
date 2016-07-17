package farcore.data;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public enum EnumBlock
{
	crop,
	sapling,
	water,
	fire;

	static
	{
		water.block = Blocks.water;
		fire.block = Blocks.fire;
	}

	public Block block;

	public void set(Block block)
	{
		this.block = block;
	}
}