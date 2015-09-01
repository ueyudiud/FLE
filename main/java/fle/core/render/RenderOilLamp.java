package fle.core.render;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.enums.EnumWorldNBT;
import fle.api.fluid.FluidBase;
import fle.api.world.BlockPos;
import fle.core.block.BlockOilLamp;
import fle.core.te.TileEntityOilLamp;

public class RenderOilLamp extends RenderBase
{
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