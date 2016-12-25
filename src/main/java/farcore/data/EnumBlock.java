package farcore.data;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

/**
 * To get block which is added by non-multi-generated.
 * @author ueyudiud
 *
 */
public enum EnumBlock
{
	crop,
	sapling,
	water,
	fire,
	ice,
	ore,
	carved_rock,
	circuit;
	
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