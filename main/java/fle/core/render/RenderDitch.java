package fle.core.render;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import fle.FLE;
import fle.api.te.IDitchTile;
import fle.core.te.TileEntityDitch;
import fle.core.util.DitchInfo;

public class RenderDitch extends RenderBase
{
	private static ForgeDirection dirs[] = {ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
	
	private static final byte stop = 0;
	private static final byte connect = 1;
	private static final byte fill = 2;

	double f = 0.03125;
	double f1 = 0.0;
	double f2 = 1.0;
	double f3 = 0.375;
	double f4 = 0.625;
	double f5 = f3 - f;
	double f6 = f4 + f;
	double f7 = 0.25;
	double f8 = 0.5;
	double f9 = f7 - f;
	double f10 = -0.375F;
	double f11 = 1.375F;
	double f12 = -0.625F;
	double f13 = 1.625F;
	double f14 = -0.5F;
	
	@Override
	public void renderBlock()
	{
		byte[] bs;
		Fluid fluid = null;
		double[] ds = null;
		int fluidColor = 0xFFFFFF;
		
		if(isItem())
		{
			bs = new byte[]{connect, stop, connect, stop};
			fluid = null;
			ds = null;
			ItemStack renderItem = DitchInfo.register.get(meta).getBlock();
			setTexture(Block.getBlockFromItem(renderItem.getItem()), renderItem.getItemDamage());
		}
		else
		{
			block.setBlockBoundsBasedOnState(world, x, y, z);
			if(world.getTileEntity(x, y, z) instanceof TileEntityDitch)
			{
				TileEntityDitch tile = ((TileEntityDitch) world.getTileEntity(x, y, z));
				try
				{
					bs = new byte[4];
					for(int i = 0; i < dirs.length; ++i)
						bs[i] = tile.getType(dirs[i]);
				}
				catch(Throwable e)
				{
					e.printStackTrace();
					bs = new byte[]{stop, stop, stop, stop};
				}
				try
				{
					ItemStack renderItem = tile.getRenderItem();
					setTexture(Block.getBlockFromItem(renderItem.getItem()), renderItem.getItemDamage());
					int color = Block.getBlockFromItem(renderItem.getItem()).getBlockColor();
					setColor(color);
				}
				catch(Throwable e)
				{
					e.printStackTrace();
					setTexture(Blocks.stone);
					setColor(0xFFFFFF);
				}
				try
				{
					fluid = tile.getFluidStackInTank(0) != null ? tile.getFluidStackInTank(0).getFluid() : null;
					if(fluid != null)
					{
						fluidColor = tile.getFluidStackInTank(0).getFluid().getColor(tile.getFluidStackInTank(0));
					}
					if(fluid != null)
					{
						ds = new double[5];
						int level = tile.getWaterLevel();
						ds[4] = (double) level / 256.0D;
						if(ds[4] > 1D) ds[4] = 1D;
						for(int i = 0; i < dirs.length; ++i)
						{
							if(tile.getBlockPos().toPos(dirs[i]).getBlockTile() instanceof IDitchTile)
							{
								ds[i] = (double) (level * 2 + ((IDitchTile) tile.getBlockPos().toPos(dirs[i]).getBlockTile()).getWaterLevel()) / 768D;
							}
							else
							{
								ds[i] = (double) (level * 2) / (256D * 3);
							}
							if(ds[i] > 1D) ds[i] = 1D;
						}
					}
				}
				catch(Throwable e)
				{
					e.printStackTrace();
					fluid = null;
					ds = null;
				}
			}
			else
			{
				bs = new byte[]{stop, stop, stop, stop};
				fluid = null;
				ds = null;
			}
		}

		renderBlock(f3, f9, f3, f4, f7, f4);
		renderBlock(f5, f9, f5, f3, f8, f3);
		renderBlock(f5, f9, f4, f3, f8, f6);
		renderBlock(f4, f9, f4, f6, f8, f6);
		renderBlock(f4, f9, f5, f6, f8, f3);

		switch(bs[0])
		{
		case fill : 
			renderBlock(f5, f9, f10, f3, f8, f5);
			renderBlock(f4, f9, f10, f6, f8, f5);
			renderBlock(f3, f9, f10, f4, f7, f3);
		break;
		case connect : 
			renderBlock(f5, f9, f1, f3, f8, f5);
			renderBlock(f4, f9, f1, f6, f8, f5);
			renderBlock(f3, f9, f1, f4, f7, f3);
		break;
		case stop : 
			renderBlock(f3, f9, f5, f4, f8, f3);
		break;
		}
		switch(bs[1])
		{
		case fill : 
			renderBlock(f6, f9, f5, f11, f8, f3);
			renderBlock(f6, f9, f4, f11, f8, f6);
			renderBlock(f4, f9, f3, f11, f7, f4);
		break;
		case connect : 
			renderBlock(f6, f9, f5, f2, f8, f3);
			renderBlock(f6, f9, f4, f2, f8, f6);
			renderBlock(f4, f9, f3, f2, f7, f4);
		break;
		case stop : 
			renderBlock(f4, f9, f3, f6, f8, f4);
		break;
		}
		switch(bs[2])
		{
		case fill : 
			renderBlock(f5, f9, f6, f3, f8, f11);
			renderBlock(f4, f9, f6, f6, f8, f11);
			renderBlock(f3, f9, f4, f4, f7, f11);
		break;
		case connect : 
			renderBlock(f5, f9, f6, f3, f8, f2);
			renderBlock(f4, f9, f6, f6, f8, f2);
			renderBlock(f3, f9, f4, f4, f7, f2);
		break;
		case stop : 
			renderBlock(f3, f9, f4, f4, f8, f6);
		break;
		}
		switch(bs[3])
		{
		case fill : 
			renderBlock(f10, f9, f5, f5, f8, f3);
			renderBlock(f10, f9, f4, f5, f8, f6);
			renderBlock(f10, f9, f3, f3, f7, f4);
		break;
		case connect : 
			renderBlock(f1, f9, f5, f5, f8, f3);
			renderBlock(f1, f9, f4, f5, f8, f6);
			renderBlock(f1, f9, f3, f3, f7, f4);
		break;
		case stop : 
			renderBlock(f5, f9, f3, f3, f8, f4);
		break;
		}

		if(fluid != null)
		{
			setTexture(fluid.getFlowingIcon());
			renderFluidBlock(f3, f7, f3, f4, f7 + (f8 - f7) * ds[4], f4);
			switch(bs[0])
			{
			case fill : 
				renderFluidBlock(f3, f7, f10, f4, f7 + (f8 - f7) * ds[0] / 1.5D, f1);
				renderFluidBlock(f3, f14, f12, f4, f7 + (f8 - f7) * ds[0] / 3D, f10);
			case connect : renderFluidBlock(f3, f7, f1, f4, f7 + (f8 - f7) * ds[0], f3);
			}
			switch(bs[1])
			{
			case fill : 
				renderFluidBlock(f2, f7, f3, f11, f7 + (f8 - f7) * ds[1] / 1.5D, f4);
				renderFluidBlock(f11, f14, f3, f13, f7 + (f8 - f7) * ds[1] / 3D, f4);
			case connect : renderFluidBlock(f4, f7, f3, f2, f7 + (f8 - f7) * ds[1], f4);
			}
			switch(bs[2])
			{
			case fill : 
				renderFluidBlock(f3, f7, f2, f4, f7 + (f8 - f7) * ds[2] / 1.5D, f11);
				renderFluidBlock(f3, f14, f11, f4, f7 + (f8 - f7) * ds[2] / 3D, f13);
			case connect : renderFluidBlock(f3, f7, f4, f4, f7 + (f8 - f7) * ds[2], f2);
			}
			switch(bs[3])
			{
			case fill : 
				renderFluidBlock(f10, f7, f3, f1, f7 + (f8 - f7) * ds[3] / 1.5D, f4);
				renderFluidBlock(f12, f14, f3, f10, f7 + (f8 - f7) * ds[3] / 3D, f4);
			case connect : renderFluidBlock(f1, f7, f3, f3, f7 + (f8 - f7) * ds[3], f4);
			}
		}
	}
}