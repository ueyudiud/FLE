package fle.core.render;

import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import fle.core.block.BlockOre;

public class RenderRock extends RenderBase
{
	@Override
	public void renderBlock() 
	{
		init();
		boolean breaking = render.overrideBlockTexture != null;
		if(breaking)
		{
			renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			return;
		}
		IIcon rock;

		if(isItem())
		{
			rock = block.getIcon(0, meta);
		}
		else
		{
			rock = block.getIcon(world, x, y, z, 0);
		}
		setTexture(rock);
		renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		render.clearOverrideBlockTexture();
	}
}