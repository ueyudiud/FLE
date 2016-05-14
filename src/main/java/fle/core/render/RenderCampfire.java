package fle.core.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.render.RenderBase;
import fle.load.Icons;

@SideOnly(Side.CLIENT)
public class RenderCampfire extends RenderBase
{
	RenderBox[] boxs;
	
	public RenderCampfire()
	{
		boxs = new RenderBox[1];
		boxs[0] = new RenderBox(-.25D, -.5D, -.25D, .25D, .5D, .25D)
				.setOffset(.75, .5, .75)
				.setRotation(.25, .5)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
	}
	
	@Override
	public void renderBlock()
	{
		boxs = new RenderBox[1];
		boxs[0] = new RenderBox(-.25D, -.5D, -.25D, .25D, .5D, .25D)
				.setOffset(.75, .5, .75)
				.setRotation(.1, -.05)
				.setCustomIconOffset(.25D, .5D, .25D, .25D, .25D, .5D)
				.initRender();
		boxs[0].setIcon(Icons.firewood_top, Icons.firewood_side);
		for(RenderBox box : boxs)
		{
			box.render();
		}
	}
}