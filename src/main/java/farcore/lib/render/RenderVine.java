package farcore.lib.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVine extends RenderBase
{
	@Override
	public void renderBlock()
	{
		if(render.hasOverrideBlockTexture())
		{
			icon = render.overrideBlockTexture;
			renderFace(1D, 0D, 1D, 0D, 0D, 0D, 0D, 1D, 0D, 1D, 1D, 1D);
			renderFace(0D, 1D, 1D, 1D, 1D, 0D, 1D, 0D, 0D, 0D, 0D, 1D);
		}
		else
		{
			icon = block.getIcon(world, x, y, z, 0);
			renderFace(1D, 1D, 1D, 1D, 0D, 1D, 0D, 0D, 0D, 0D, 1D, 0D);
			renderFace(1D, 1D, 0D, 1D, 0D, 0D, 0D, 0D, 1D, 0D, 1D, 1D);
		}
	}
}