package farcore.block;

import net.minecraft.block.state.IBlockState;

public interface IHarvestCheck
{
	boolean canBeHarvested(IBlockState state, String tool, int toolLevel);
}