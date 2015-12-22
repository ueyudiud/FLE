package flapi.fluid;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FluidJsonStack
{
	@SerializedName("fluidName")
	@Expose
	public String fluid;
	@SerializedName("amount")
	@Expose
	public int amount;
	
	public FluidJsonStack()
	{
		
	}
	
	public FluidJsonStack(FluidStack input)
	{
		this.fluid = input.getFluid().getName();
		this.amount = input.amount;
	}

	public FluidStack getFluid()
	{
		return new FluidStack(FluidRegistry.getFluid(fluid), amount);
	}
	
	public boolean hasStack()
	{
		return fluid != null ? FluidRegistry.isFluidRegistered(fluid) : false;
	}
}