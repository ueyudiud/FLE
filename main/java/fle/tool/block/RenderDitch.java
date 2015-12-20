package fle.tool.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import fle.core.render.RenderBase;
import fle.core.te.TileEntityDitch;
import fle.tool.DitchInfo;

public class RenderDitch extends RenderBase
{
	private static ForgeDirection dirs[] = {ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
	
	private static final byte stop = 0,
			connect = 1,
			fill = 2;

	double f = 0.0625,
			f1 = 0.0,
			f2 = 1.0,
			f3 = 0.375,
			f4 = 0.625,
			f5 = f3 - f,
			f6 = f4 + f,
			f7 = 0.25,
			f8 = 0.5,
			f9 = f7 - f,
			f10 = -0.375F,
			f11 = 1.375F,
			f12 = -0.625F,
			f13 = 1.625F,
			f14 = -0.5F;
	
	@Override
	public void renderBlock()
	{
		byte[] bs;
		
		if(isItem())
		{
			bs = new byte[]{connect, stop, connect, stop};
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
			}
			else
			{
				bs = new byte[]{stop, stop, stop, stop};
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
	}
}