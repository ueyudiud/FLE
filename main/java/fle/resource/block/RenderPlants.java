package fle.resource.block;

import fle.core.render.RenderBase;

public class RenderPlants extends RenderBase
{
	double p0 = 0.0625D,
			p1 = 0.0D,
			p2 = 1.0D,
			p3 = 0.375D,
			p4 = 0.625D;
	
	@Override
	public void renderBlock()
	{
		boolean breaking = render.overrideBlockTexture != null;
		if(breaking)
		{
			renderSide(p2, p2 - p0, p2, p2, p1 - p0, p2, p1, p1 - p0, p1, p1, p2 - p0, p1);
			renderSide(p1, p2 - p0, p2, p1, p1 - p0, p2, p2, p1 - p0, p1, p2, p2 - p0, p1);
			return;
		}
		setTexture(block.getIcon(world, x, y, z, 0));
		setColor(block.colorMultiplier(world, x, y, z));
		renderSide(p2, p2 - p0, p2, p2, p1 - p0, p2, p1, p1 - p0, p1, p1, p2 - p0, p1);
		renderSide(p1, p2 - p0, p2, p1, p1 - p0, p2, p2, p1 - p0, p1, p2, p2 - p0, p1);
	}
}