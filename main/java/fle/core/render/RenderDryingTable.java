package fle.core.render;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.world.BlockPos;
import fle.core.init.IB;

public class RenderDryingTable extends RenderBase
{
	@Override
	public void renderBlock() 
	{
        setTexture(block.getIcon(0, 0));
		double d1 = 0.325D;
		double d2 = 0.375D;
		double d3 = 0.625D;
		double d4 = 0.675D;
		double d5;
		if(isItem()) d5 = 0.8D;
		else
		{
			Block tBlock = world.getBlock(x, y + 1, z);
			if(tBlock == IB.woodMachine1)
			{
				int value =  FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 0);
				if((value == 0 ? world.getBlockMetadata(x, y, z) : value) == 0) d5 = 1.0D;
				else d5 = 0.8D;
			}
			else if(tBlock.isSideSolid(world, x, y, z, ForgeDirection.DOWN))
				d5 = 1.0D;
			else d5 = 0.8D;
		}
		double d6 = 0.0D;
		double d7 = 1.0D;
		double d8 = 0.275D;
		double d9 = 0.725D;
		renderBlock(d1, d6, d1, d2, d5, d2);
		renderBlock(d1, d6, d3, d2, d5, d4);
		renderBlock(d3, d6, d1, d4, d5, d2);
		renderBlock(d3, d6, d3, d4, d5, d4);
		
		renderBlock(d6, d3, d8, d7, d4, d1);
		renderBlock(d8, d3, d6, d1, d4, d7);
		renderBlock(d4, d3, d6, d9, d4, d7);
		renderBlock(d6, d3, d4, d7, d4, d9);
		
		renderBlock(d6, d1, d8, d7, d2, d1);
		renderBlock(d8, d1, d6, d1, d2, d7);
		renderBlock(d4, d1, d6, d9, d2, d7);
		renderBlock(d6, d1, d4, d7, d2, d9);
	}
}