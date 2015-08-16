package fle.core.render;

import fle.api.enums.EnumCropRender;
import fle.core.block.BlockFleCrop;
import fle.core.init.IB;
import fle.core.te.TileEntityCrop;

public class RenderCrop extends RenderBase
{
	@Override
	public void renderBlock() 
	{
		if(isItem()) return;
		TileEntityCrop tile = (TileEntityCrop) world.getTileEntity(x, y, z);
		setTexture(block);
		EnumCropRender type = EnumCropRender.CROSS;
		if(tile != null)
			if(tile.getCrop() != null)
			{
				type = tile.getCrop().getRenderType();
				setTexture(((BlockFleCrop) IB.crop).getIcon(tile.getCrop(), tile.getStage()));
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