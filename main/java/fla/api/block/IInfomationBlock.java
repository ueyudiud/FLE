package fla.api.block;

import fla.api.util.InfoBuilder;
import fla.api.world.BlockPos;

public interface IInfomationBlock 
{
	public InfoBuilder<BlockPos> getInformation();
}
