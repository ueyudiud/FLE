package fle.api.util;

import fle.api.material.Matter;
import fle.api.world.BlockPos;

public interface IAirConditionProvider
{
	public Matter getAirLevel(BlockPos aPos);
	
	public int getPolluteLevel(BlockPos aPos);
	
	public void setPollute(BlockPos aPos, int pollute);
}