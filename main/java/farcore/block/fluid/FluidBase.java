package farcore.block.fluid;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import farcore.substance.PhaseDiagram.Phase;
import farcore.substance.Substance;

public class FluidBase extends Fluid
{
	public Phase phase;
	public Substance substance;

	public FluidBase(String fluidName, Phase phase, ResourceLocation still,
			ResourceLocation flowing)
	{
		super("fle." + fluidName, still, flowing);
		FluidRegistry.registerFluid(this);
		this.phase = phase;
	}
	
	@Override
	public FluidBase setBlock(Block block)
	{
		super.setBlock(block);
		return this;
	}
	
	public FluidBase setSubstance(Substance substance)
	{
		this.substance = substance;
		return this;
	}
	
	@Override
	public FluidBase setTemperature(int temperature)
	{
		super.setTemperature(temperature);
		return this;
	}
	
	public Substance getSubstance()
	{
		return substance;
	}
	
	public Phase getPhase()
	{
		return phase;
	}
	
	@Override
	public int getLuminosity(FluidStack stack)
	{
		return substance != null ? substance.getLuminosity(phase) / 15 : super.getLuminosity();
	}
	
	@Override
	public int getLuminosity(World world, BlockPos pos)
	{
		return substance != null ? substance.getLuminosity(phase) / 15 : super.getLuminosity();
	}
	
	@Override
	public int getViscosity(FluidStack stack)
	{
		return substance != null ? substance.viscosity : super.getViscosity();
	}
	
	@Override
	public int getViscosity(World world, BlockPos pos)
	{
		return substance != null ? substance.viscosity : super.getViscosity();
	}
	
	@Override
	public int getDensity(FluidStack stack)
	{
		return substance != null ? (int) substance.density : super.getDensity();
	}
	
	@Override
	public int getDensity(World world, BlockPos pos)
	{
		return substance != null ? (int) substance.density : super.getDensity();
	}
	
	@Override
	public int getColor()
	{
		return substance.getRGBa(phase);
	}
}