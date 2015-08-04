package fle.api.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidRegistry.FluidRegisterEvent;
import net.minecraftforge.fluids.IFluidTank;

public class FleFluidTank extends FluidTank
{
	private Fluid fluid;

	private FleFluidTank(){super(-1);}
	public FleFluidTank(int aCapacity) 
	{
		this(null, aCapacity);
	}
	public FleFluidTank(Fluid aFluidCanFill, int aCapacity) 
	{
		super(aCapacity);
		fluid = aFluidCanFill;
	}

    public void setFluid(FluidStack aFluid)
    {
    	if(fluid == null)
    	{
    		super.setFluid(aFluid);
    		return;
    	}
        if(aFluid.isFluidEqual(new FluidStack(fluid, 1))) super.setFluid(aFluid);
    }
    
    public int fill(FluidStack aResource, boolean aFill)
    {
    	if(fluid == null)
    	{
    		return super.fill(aResource, aFill);
    	}
    	if(aResource.isFluidEqual(new FluidStack(fluid, 1))) return super.fill(aResource, aFill);
    	return 0;
    }
    
    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) 
    {
    	return super.readFromNBT(nbt);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) 
    {
    	return super.writeToNBT(nbt);
    }
}