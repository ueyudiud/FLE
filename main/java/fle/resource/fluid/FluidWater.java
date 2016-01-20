package fle.resource.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

import farcore.block.fluid.BlockFluidBase;
import farcore.block.fluid.FluidBase;
import farcore.substance.PhaseDiagram.Phase;
import flapi.util.FleValue;

public class FluidWater extends FluidBase
{
	public FluidWater(String fluidName, String still, String flowing)
	{
		super(fluidName, Phase.LIQUID,
				new ResourceLocation(FleValue.TEXTURE_FILE, still),
				new ResourceLocation(FleValue.TEXTURE_FILE, flowing));
		BlockFluidBase block;
		setBlock(block = new BlockFluidBase(this, Material.water));
		block.setCreativeTab(CreativeTabs.tabDecorations);
		block.addDefaultReaction();
		block.setQuantaPerBlock(16);
	}
	
	public FluidWater(String fluidName)
	{
		this(fluidName,
				fluidName.toLowerCase().replace(' ', '_').trim() + "_still",
				fluidName.toLowerCase().replace(' ', '_').trim() + "_flowing");
	}
}