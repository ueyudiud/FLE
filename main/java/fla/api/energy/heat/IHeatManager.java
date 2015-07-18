package fla.api.energy.heat;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.world.BlockPos;

public interface IHeatManager 
{
	public int getHeat(BlockPos pos);
	
	public int getHeatAsk(BlockPos pos);
	
	public void emmitHeat(World world, BlockPos pos, ForgeDirection to, int pkg);
}
