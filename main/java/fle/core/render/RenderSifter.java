package fle.core.render;

import net.minecraft.init.Blocks;

public class RenderSifter extends RenderBase
{
	double d1 = 0.0D;
	double d2 = 0.25D;
	double d3 = 0.375D;
	//double d4 = 0.5D;
	double d5 = 1 - d3;
	double d6 = 1 - d2;
	double d7 = 1 - d1;
	double d8 = 0.2D;
	double d9 = d8 * 2;
	double d10 = 0.25;
	double d11 = 0.265125;
	
	@Override
	public void renderBlock()
	{
		setTexture(Blocks.planks);
		renderBlock(d1, d1, d1, d2, d9, d2);
		renderBlock(d6, d1, d1, d7, d9, d2);
		renderBlock(d6, d1, d6, d7, d9, d7);
		renderBlock(d1, d1, d6, d2, d9, d7);

		renderBlock(d2, d8, d1, d6, d9, d2);
		renderBlock(d1, d8, d2, d2, d9, d6);
		renderBlock(d6, d8, d2, d7, d9, d6);
		renderBlock(d2, d8, d6, d6, d9, d7);

		setTexture(block, 3);
		renderBlock(d2, d10, d2, d6, d11, d6);
	}
}