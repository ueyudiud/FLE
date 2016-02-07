package farcore.block.interfaces;

import java.util.Set;

import farcore.world.BlockPos;

public interface IHarvestCheck
{
	Set<String> getAccessTools(BlockPos pos);
	
	int getToolLevel(BlockPos pos, String tool);
}