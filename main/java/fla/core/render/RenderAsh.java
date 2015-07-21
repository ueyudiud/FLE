package fla.core.render;

public class RenderAsh extends RenderBase
{
	@Override
	public void renderBlock() 
	{
		init();
		setTexture(block);
		double height = (double) (meta + 1) / 16D;
		renderBlock(0D, 0D, 0D, 1D, height, 1D);
		if(!isItem())
		{
			block.setBlockBoundsBasedOnState(world, x, y, z);
		}
	}
}
