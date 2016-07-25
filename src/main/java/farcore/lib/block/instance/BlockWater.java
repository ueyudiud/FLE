package farcore.lib.block.instance;

import farcore.data.EnumBlock;
import farcore.lib.block.BlockStandardFluid;
import farcore.lib.fluid.FluidBase;
import net.minecraft.block.material.Material;

public class BlockWater extends BlockStandardFluid
{
	public BlockWater(FluidBase fluid)
	{
		super(fluid, Material.WATER);
		EnumBlock.water.set(this);
	}
}