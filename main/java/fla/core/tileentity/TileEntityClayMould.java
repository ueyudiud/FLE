package fla.core.tileentity;

import net.minecraftforge.common.util.ForgeDirection;
import fla.api.energy.heat.IHeatTileEntity;
import fla.api.util.InfoBuilder;
import fla.api.world.BlockPos;
import fla.core.tileentity.base.TileEntityBase;

public class TileEntityClayMould extends TileEntityBase implements IHeatTileEntity
{
	@Override
	public InfoBuilder<BlockPos> getInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canEmmit(ForgeDirection d, int pkg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canReseave(ForgeDirection d, int pkg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getHeatCorrect(ForgeDirection d) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void catchHeat(ForgeDirection d, int pkg) {
		// TODO Auto-generated method stub
		
	}
}