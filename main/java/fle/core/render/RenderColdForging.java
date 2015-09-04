package fle.core.render;

import net.minecraft.init.Blocks;

public class RenderColdForging extends RenderBase
{
	@Override
	public void renderBlock()
	{
		setTexture(Blocks.stone);
		renderBlock(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);
	}
}