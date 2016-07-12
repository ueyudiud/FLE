package farcore.data;

import net.minecraft.block.Block;

public enum EnumBlock
{
	crop,
	sapling;

	public Block block;
	
	public void set(Block block)
	{
		this.block = block;
	}
}