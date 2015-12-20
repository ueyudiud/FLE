package flapi.te.interfaces;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface IFluidTanks
{
	int getSizeTank();
	
	IFluidTank getTank(int index);
	
	FluidStack getFluidStackInTank(int index);
	
	void setFluidStackInTank(int index, FluidStack aStack);
	
	FluidStack drainTank(int index, int maxDrain, boolean doDrain);
	
	int fillTank(int index, FluidStack resource, boolean doFill);
}
