package fle.core.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.render.RenderBase;
import fle.core.FLE;
import fle.load.BlockItems;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

@SideOnly(Side.CLIENT)
public class RenderDryingTable extends RenderBase
{
	double d1 = 0.325D,
			d2 = 0.375D,
			d3 = 0.625D,
			d4 = 0.675D,
			d6 = 0.0D,
			d7 = 1.0D,
			d8 = 0.275D,
			d9 = 0.725D;
	
	@Override
	public void renderBlock() 
	{
        setTexture(block.getIcon(0, 0));
		double d5;
		if(isItem()) d5 = 0.8D;
		else
		{
			Block tBlock = world.getBlock(x, y + 1, z);
			if(tBlock == BlockItems.machineIIAlpha && world.getBlockMetadata(x, y + 1, z) == 0)
			{
				d5 = 1.0D;
			}
			else if(tBlock.isSideSolid(world, x, y + 1, z, ForgeDirection.DOWN))
			{
				d5 = 1.0D;
			}
			else d5 = 0.8D;
		}
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