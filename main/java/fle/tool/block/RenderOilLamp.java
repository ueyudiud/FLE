package fle.tool.block;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import flapi.enums.EnumWorldNBT;
import flapi.util.FleValue;
import flapi.world.BlockPos;
import fle.FLE;
import fle.core.render.RenderBase;

public class RenderOilLamp extends RenderBase
{
	double p0 = 0D,
			p1 = 0.03125D,
			p2 = 0.25D,
			p3 = 0.3D,
			p4 = 0.7D,
			p5 = 0.4D,
			p6 = 0.6D,
			p7 = 0.49325D,
			p8 = 0.50625D;
	
	@Override
	public void renderBlock() 
	{
		BlockPos aPos = new BlockPos(world, x, y, z);
		boolean hasWick = false;
		FluidStack aStack = null;
		if(aPos.getBlockTile() instanceof TileEntityOilLamp)
		{
			TileEntityOilLamp tile = (TileEntityOilLamp) aPos.getBlockTile();
			setTexture(tile.getRockIcon());
			hasWick = tile.hasWick;
			aStack = tile.fluid.getFluid();
		}
		renderBlock(p3, p0, p3, p4, p1, p4);
		renderBlock(p3, p1, p3, p3 + p1, p2, p4);
		renderBlock(p3, p1, p3, p4, p2, p3 + p1);
		renderBlock(p3, p1, p4 - p1, p4, p2, p4);
		renderBlock(p4 - p1, p1, p3, p4, p2, p4);
		dir = ForgeDirection.VALID_DIRECTIONS[FLE.fle.getWorldManager().getData(aPos, EnumWorldNBT.Facing)];
		switch(dir)
		{
		case SOUTH:
			renderBlock(p5, p2 - p1, 0D, p6, p2, p3);
			break;
		case WEST:
			renderBlock(p4, p2 - p1, p5, 1D, p2, p6);
			break;
		case NORTH:
			renderBlock(p5, p2 - p1, p4, p6, p2, 1D);
			break;
		case EAST:
			renderBlock(0D, p2 - p1, p5, p3, p2, p6);
			break;
		default:
			break;
		}
		if(aStack == null ? false : aStack.amount != 0)
		{
			double bar = (double) aStack.amount / (double) FleValue.CAPACITY[4];
			setTexture(aStack.getFluid().getIcon());
			renderFluidBlock(p3 + p1, p1, p3 + p1, p4 - p1, bar * (p2 - p1) + p1, p4 - p1);
		}
		if(hasWick)
		{
			setTexture(((BlockOilLamp) block).getWickIcon());
			renderBlock(p7, p1, p7, p8, p5, p8);
		}
	}
}