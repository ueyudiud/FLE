package flapi.chem;

import net.minecraftforge.fluids.FluidStack;
import flapi.chem.base.ScaleInput;

public class MeltingRecipe
{
	private final String name;
	public int energyRequire;
	public ScaleInput input;
	public FluidStack fluid;
	public float defaultSpeed;
	
	public MeltingRecipe(String name, ScaleInput input, int energy, float speed, FluidStack output)
	{
		this.name = name;
		this.input = input;
		this.energyRequire = energy;
		this.fluid = output;
		this.defaultSpeed = speed;
	}

	public String getName()
	{
		return name;
	}

	public float getSpeed()
	{
		return defaultSpeed;
	}
}