package fle.core.render;

public class RenderEmpty extends RenderBase
{
	@Override
	public void renderBlock()
	{
		if(!isItem())
			block.setBlockBoundsBasedOnState(world, x, y, z);
	}
}