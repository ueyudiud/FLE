package fla.api.energy.heat;

import net.minecraftforge.common.util.ForgeDirection;
import fla.api.world.BlockPos;

public interface IHeatManager 
{
	public int getHeat(BlockPos pos);
	
	public int getHeatAsk(BlockPos pos);
	
	public void emmitHeat(BlockPos pos, ForgeDirection to, int pkg);
}
