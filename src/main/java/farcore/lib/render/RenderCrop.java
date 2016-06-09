package farcore.lib.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.crop.CropCard.EnumRenderType;
import farcore.lib.crop.CropCard;
import farcore.lib.crop.ICropAccess;
import farcore.lib.crop.TileEntityCrop;
import net.minecraft.tileentity.TileEntity;

@SideOnly(Side.CLIENT)
public class RenderCrop extends RenderBase
{
	double p0 = 0.0625D,
			p1 = 0.0D,
			p2 = 1.0D,
			p3 = 0.375D,
			p4 = 0.625D;
	
	@Override
	public void renderBlock() 
	{
		if(isItem()) return;
		boolean breaking = render.overrideBlockTexture != null;
		TileEntity tile = world.getTileEntity(x, y, z);
		EnumRenderType type = EnumRenderType.cross;
		CropCard card;
		if(!breaking)
		{
			if(tile instanceof ICropAccess)
			{
				if((card = ((ICropAccess) tile).getCrop()) != null)
				{
					type = card.getRenderType();
					setTexture(card.getIcon((ICropAccess) tile));
				}
			}
		}
		else
		{
			icon = render.overrideBlockTexture;
		}
		switch(type)
		{
		case cross:
		{
			renderFace(p2, p2 - p0, p2, p2, p1 - p0, p2, p1, p1 - p0, p1, p1, p2 - p0, p1);
			renderFace(p1, p2 - p0, p2, p1, p1 - p0, p2, p2, p1 - p0, p1, p2, p2 - p0, p1);
		}
		break;
		case lattice:
		{
			renderFace(p1, p2 - p0, p3, p1, p1 - p0, p3, p2, p1 - p0, p3, p2, p2 - p0, p3);
			renderFace(p3, p2 - p0, p1, p3, p1 - p0, p1, p3, p1 - p0, p2, p3, p2 - p0, p2);
			renderFace(p1, p2 - p0, p4, p1, p1 - p0, p4, p2, p1 - p0, p4, p2, p2 - p0, p4);
			renderFace(p4, p2 - p0, p1, p4, p1 - p0, p1, p4, p1 - p0, p2, p4, p2 - p0, p2);
		}
		break;
		
		}
	}
}