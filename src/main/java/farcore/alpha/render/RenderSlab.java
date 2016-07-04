package farcore.alpha.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.render.RenderBase;

@SideOnly(Side.CLIENT)
public class RenderSlab extends RenderBase
{
	@Override
	public void renderBlock()
	{
		setTexture(block);
		float maxX = 1.0F;
		float maxY = 1.0F;
		float maxZ = 1.0F;
		float minX = 0.0F;
		float minY = 0.0F;
		float minZ = 0.0F;
		switch (meta)
		{
		case 0 :
			maxY = 0.5F;
			break;
		case 1 :
			minY = 0.5F;
			break;
		case 2 :
			maxZ = 0.5F;
			break;
		case 3 :
			minZ = 0.5F;
			break;
		case 4 :
			maxX = 0.5F;
			break;
		case 5 : 
			minX = 0.5F;
			break;
		}
		renderBlock(minX, minY, minZ, maxX, maxY, maxZ);
	}
}