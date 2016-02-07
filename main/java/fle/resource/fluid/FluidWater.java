package fle.resource.fluid;

import cpw.mods.fml.common.registry.GameRegistry;
import farcore.FarCore;
import farcore.fluid.BlockFluidBase;
import farcore.fluid.FluidBase;
import farcore.substance.Substance;
import farcore.util.Util;
import flapi.util.Values;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

public class FluidWater extends FluidBase
{
	public FluidWater(Substance water, Material material)
	{
		super("fle." + Util.oreDictFormat(water.getName(), " ", false));
		setTemperature(298);
		setDensity((int) water.density);
		setViscosity(water.viscosity);
		new BlockWater(this, material)
		.setCreativeTab(Values.tabFLEResource);
	}
	
	public FluidWater setTextureName(String name)
	{
		textureName = name;
		return this;
	}
}