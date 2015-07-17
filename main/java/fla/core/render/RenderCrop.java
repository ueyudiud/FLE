package fla.core.render;

import fla.api.crop.CropRenderType;
import fla.core.FlaBlocks;
import fla.core.block.BlockFlaCrop;
import fla.core.tileentity.TileEntityCrops;

public class RenderCrop extends RenderBase
{
	@Override
	public void renderBlock() 
	{
		if(isItem()) return;
		TileEntityCrops tile = (TileEntityCrops) world.getTileEntity(x, y, z);
		setTexture(block);
		CropRenderType type = CropRenderType.CROSS;
		if(tile != null)
			if(tile.getCrop() != null)
			{
				type = tile.getCrop().getRenderType();
				setTexture(((BlockFlaCrop) FlaBlocks.crops).getIcon(tile.getCrop(), tile.getStage()));
			}
		double p0 = 0.0625D;
		double p1 = 0.0D;
		double p2 = 1.0D;
		double p3 = 0.375D;
		double p4 = 0.625D;
		switch(type)
		{
		case CROSS:
		{
			renderSide(p2, p2 - p0, p2, p2, p1 - p0, p2, p1, p1 - p0, p1, p1, p2 - p0, p1);
			renderSide(p1, p2 - p0, p2, p1, p1 - p0, p2, p2, p1 - p0, p1, p2, p2 - p0, p1);
		}
			break;
		case O_LIKE:
		{
			renderSide(p1, p2 - p0, p3, p1, p1 - p0, p3, p2, p1 - p0, p3, p2, p2 - p0, p3);
			renderSide(p3, p2 - p0, p1, p3, p1 - p0, p1, p3, p1 - p0, p2, p3, p2 - p0, p2);
			renderSide(p1, p2 - p0, p4, p1, p1 - p0, p4, p2, p1 - p0, p4, p2, p2 - p0, p4);
			renderSide(p4, p2 - p0, p1, p4, p1 - p0, p1, p4, p1 - p0, p2, p4, p2 - p0, p2);
		}
			break;
		default:
			break;
		
		}
	}
}