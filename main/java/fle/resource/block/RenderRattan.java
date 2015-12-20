package fle.resource.block;

import fle.core.render.RenderBase;

public class RenderRattan extends RenderBase
{
	@Override
	public void renderBlock()
	{
		boolean breaking = render.overrideBlockTexture != null;
		if(breaking)
		{
			renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			return;
		}
		setTexture(block, meta);
		if(meta == 0)
		{
			block.setBlockBoundsBasedOnState(world, x, y, z);
			renderBlock(0.0625F, 0.0F, 0.0625F, 0.9325F, 0.9325F, 0.9325F);
		}
		else
		{
			if(!isItem())
			{
				block.setBlockBoundsBasedOnState(world, x, y, z);
				renderStandardBlock(block, x, y, z);
			}
			else
			{
				renderBlock(0.0F, 0.0F, 0.0F, 0.0625F, 1.0F, 1.0F);
			}
		}
	}
}