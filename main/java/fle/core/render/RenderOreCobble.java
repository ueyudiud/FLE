package fle.core.render;

import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import fle.api.block.BlockFle;
import fle.api.world.BlockPos;
import fle.core.block.BlockOreCobble;

public class RenderOreCobble extends RenderBase
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
		IIcon ore;
		IIcon ore1;

		if(isItem())
		{
			rock = ((BlockFle) block).getIcon(0, meta, 0);
			ore = ((BlockFle) block).getIcon(1, meta, 0);
			ore1 = ((BlockFle) block).getIcon(2, meta, 0);
		}
		else
		{
			rock = ((BlockFle) block).getIcon(0, new BlockPos(world, x, y, z), 0);
			ore = ((BlockFle) block).getIcon(1, new BlockPos(world, x, y, z), 0);
			ore1 = ((BlockFle) block).getIcon(2, new BlockPos(world, x, y, z), 0);
		}

		setTexture(rock);
		renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		render.clearOverrideBlockTexture();
		((BlockOreCobble) block).renderFlag = 0;
		setTexture(ore);
		renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		render.clearOverrideBlockTexture();
		((BlockOreCobble) block).renderFlag = 1;
		setTexture(ore1);
		renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		((BlockOreCobble) block).renderFlag = 2;
	}
}
