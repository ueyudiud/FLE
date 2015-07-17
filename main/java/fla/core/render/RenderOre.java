package fla.core.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.IIcon;
import fla.core.block.BlockBaseOre;

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
		}
		else
		{
			IIcon rock;
			IIcon ore;
			if(isItem())
			{
				rock = ((BlockBaseOre) block).getBlockTextureFromSide(true, meta);
				ore = ((BlockBaseOre) block).getBlockTextureFromSide(false, meta);
			}
			else
			{
				rock = ((BlockBaseOre) block).getBlockTextureFromSide(true, world, x, y, z);
				ore = ((BlockBaseOre) block).getBlockTextureFromSide(false, world, x, y, z);
			}
			setTexture(rock);
			renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
			render.clearOverrideBlockTexture();
			setTexture(ore);
			renderBlock(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		}
	}

}
