package fla.core.render;

import net.minecraft.init.Blocks;
import fla.api.util.FlaValue;
import fla.api.world.BlockPos;
import fla.core.block.BlockOilLamp;
import fla.core.tileentity.TileEntityOilLamp;

public class RenderOilLamp extends RenderBase
{
	@Override
	public void renderBlock() 
	{
		init();
		if(isItem()) return;
		TileEntityOilLamp tile = (TileEntityOilLamp) world.getTileEntity(x, y, z);
		setTexture(Blocks.stone);
		double p0 = 0D;
		double p1 = 0.03125D;
		double p2 = 0.25D;
		double p3 = 0.3D;
		double p4 = 0.7D;
		double p5 = 0.4D;
		double p6 = 0.6D;
		double p7 = 0.49325D;
		double p8 = 0.50625D;
		renderBlock(p3, p0, p3, p4, p1, p4);
		renderBlock(p3, p1, p3, p3 + p1, p2, p4);
		renderBlock(p3, p1, p3, p4, p2, p3 + p1);
		renderBlock(p3, p1, p4 - p1, p4, p2, p4);
		renderBlock(p4 - p1, p1, p3, p4, p2, p4);
		
		if(tile != null)
		{
			dir = tile.getBlockDirection(new BlockPos(world, x, y, z));
			switch(dir)
			{
			case SOUTH:
				renderBlock(p5, p2 - p1, 0D, p6, p2, p3);
				break;
			case WEST:
				renderBlock(0D, p2 - p1, p5, p3, p2, p6);
				break;
			case NORTH:
				renderBlock(p4, p2 - p1, p5, 1D, p2, p6);
				break;
			case EAST:
				renderBlock(p5, p2 - p1, p4, p6, p2, 1D);
				break;
			default:
				break;
			}
			double bar = (double) tile.getContainFluid() / (double) FlaValue.CAPACITY_OIL_LAMP;
			if(bar != 0D)
			{
				setTexture(tile.getFuelType().getIcon());
				renderFluidBlock(p3 + p1, p1, p3 + p1, p4 - p1, bar * (p2 - p1) + p1, p4 - p1);
			}
			if(tile.hasWick)
			{
				setTexture(((BlockOilLamp) block).getWickIcon());
				renderBlock(p7, p1, p7, p8, p5, p8);
			}
		}
	}
}
