/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
	rock,
	ore,
	carved_rock,
	circuit;
	
	static
	{
		water.block = Blocks.WATER;
		fire.block = Blocks.FIRE;
	}
	
	public Block block;
	public Function<Object[], IBlockState> stateApplier;
	
	public void set(Block block)
	{
		this.block = block;
	}
	
	/**
	 * Create a new block state, return null if argument is invalid.
	 * @param objects
	 * @return
	 */
	@Nullable
	public IBlockState apply(Object...objects)
	{
		try
		{
			return this.stateApplier == null ? this.block.getDefaultState() : this.stateApplier.apply(objects);
		}
		catch (Exception exception)
		{
			return null;
		}
	}
}