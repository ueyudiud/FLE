package fle.core.te.tank;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;

public class TileEntityMultiTankIO extends TileEntityMultiTank
{
	public TileEntityMultiTankIO()
	{
		super("MultiTankIO", 2);
	}
	
	@Override
	protected FluidTank getTank()
	{
		return isMainTile() ? tank : mainTile != null ? mainTile.tank : new FluidTank(0);
	}
	
	@Override
	protected boolean canBeMainTile()
	{
		return false;
	}
	
	@Override
	public void onNeibourChange(boolean flag)
	{
		if(flag && isMultiTank())
		{
			mainTile.onNeibourChange(false);
		}
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return true;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}
}