package farcore.interfaces.tile;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;

public interface IFluidTanks
{
	int getTankSize();
	
	IFluidTank getTank(int index);
	
	default FluidStack getFluidStackInTank(int index)
	{
		IFluidTank tank;
		return (tank = getTank(index)) == null ? null :
			tank.getFluid();
	}
	
	void setFluidStackInTank(int index, FluidStack stack);
}