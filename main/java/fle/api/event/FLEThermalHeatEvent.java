package fle.api.event;

import net.minecraftforge.event.world.BlockEvent;
import fle.api.world.BlockPos;

public class FLEThermalHeatEvent extends BlockEvent
{
	public final boolean isEmmit;
	public final double heat;
	private double newHeat;
	
	public FLEThermalHeatEvent(BlockPos pos, double aHeat, boolean aEmmit)
	{
		super(pos.x, pos.y, pos.z, pos.world(), pos.getBlock(), pos.getBlockMeta());
		newHeat = heat = aHeat;
		isEmmit = aEmmit;
	}
	
	public void setHeat(double heat)
	{
		newHeat = heat;
	}
	
	public double getHeat()
	{
		return newHeat;
	}
}