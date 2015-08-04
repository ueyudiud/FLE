package fle.core.render;

import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import fle.core.block.BlockOre;

public class RenderOre extends RenderBase
{
	@Override
	public void renderBlock() 
	{
		init();
		boolean breaking = render.overrideBlockTexture != null;
		if(breaking)
		{
			init();
			renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			return;
		}
		IIcon rock;
		IIcon ore;

		if(isItem())
		{
			rock = Blocks.stone.getIcon(0, 0);
			ore = block.getIcon(0, meta);
		}
		else
		{
			rock = BlockOre.getOreBase(world, x, y, z).getIcon(0, BlockOre.getOreBaseMeta(world, x, y, z));
			ore = block.getIcon(world, x, y, z, 0);
		}
		setTexture(rock);
		renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		render.clearOverrideBlockTexture();
		setTexture(ore);
		renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}
}
