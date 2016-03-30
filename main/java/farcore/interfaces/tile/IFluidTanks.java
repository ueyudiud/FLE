package farcore.interfaces.tile;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface IFluidTanks
{
	int getTankSize();
	
	IFluidTank getTank(int index);
	
	void setFluidStackInTank(int index, FluidStack stack);
}