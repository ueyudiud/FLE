package fle.core.block;

import farcore.block.BlockStandardFluid;
import farcore.enums.EnumBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlockWater extends BlockStandardFluid
{
	public BlockWater()
	{
		super(FluidRegistry.WATER, Material.water);
		this.slipperiness = 0.75F;
		setQuantaPerBlock(16);
		EnumBlock.water.setBlock(this);
	}
}