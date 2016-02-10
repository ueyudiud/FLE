package fle.resource.fluid;

import farcore.fluid.FluidBase;
import farcore.fluid.IFlashableFluid;
import farcore.substance.Substance;
import farcore.util.Util;
import flapi.util.Values;
import fle.init.Materials;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class FluidFlashable extends FluidBase implements IFlashableFluid
{
	protected int flashPoint;
	protected int explosionLevel;
	
	public FluidFlashable(Substance substance, Material material)
	{
		super("fle." + Util.oreDictFormat(substance.getName(), " ", false));
		setTemperature(298);
		setDensity((int) substance.density);
		setViscosity(substance.viscosity);
		new BlockFlashableFluid(this, material)
		.setCreativeTab(Values.tabFLEResource);
	}
	
	public FluidFlashable setTextureName(String name)
	{
		textureName = name;
		return this;
	}
	
	public FluidFlashable setFlashPoint(int flashPoint)
	{
		this.flashPoint = flashPoint;
		return this;
	}
	
	public FluidFlashable setExplosionLevel(int explosionLevel)
	{
		this.explosionLevel = explosionLevel;
		return this;
	}
	
	public final int getFlashPoint()
	{
		return flashPoint;
	}
	
	public final int getExplosionLevel()
	{
		return explosionLevel;
	}

	@Override
	public int getFlashPoint(World world, int x, int y, int z)
	{
		return getFlashPoint();
	}

	@Override
	public int getFlashPoint(FluidStack stack)
	{
		return getFlashPoint();
	}

	@Override
	public int getExplosionLevel(World world, int x, int y, int z)
	{
		return getExplosionLevel();
	}

	@Override
	public int getExplosionLevel(FluidStack stack)
	{
		return getExplosionLevel();
	}
}