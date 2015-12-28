package flapi.world;

import flapi.chem.base.Matter;

public interface IAirConditionProvider
{
	public Matter getAirLevel(BlockPos aPos);
	
	public int getPolluteLevel(BlockPos aPos);
	
	public void setPollute(BlockPos aPos, int pollute);
}