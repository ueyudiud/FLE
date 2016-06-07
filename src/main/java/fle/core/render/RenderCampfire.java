package fle.core.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.render.RenderBase;
import fle.load.Icons;

@SideOnly(Side.CLIENT)
public class RenderCampfire extends RenderBase
{
	private RenderBox[] boxs;
	
	public RenderCampfire()
	{
		boxs = new RenderBox[6];
		boxs[0] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(0.33, .17, -.1)
				.setRotation(0.0D, -.2D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
		boxs[1] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(-.1275, .15, .17)
				.setRotation(0.12D, -.13D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
		boxs[2] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(0, .18, .975)
				.setRotation(0.35D, -.18D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
		boxs[3] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(.5, .125, 1.2)
				.setRotation(0.5D, -.14D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
		boxs[4] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(1.1, .125, 0.75)
				.setRotation(0.666D, -.16D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
		boxs[5] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(0.9, .125, 0.08)
				.setRotation(0.833D, -.16D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
	}
	
	@Override
	public void renderBlock()
	{
		boxs = new RenderBox[6];
		boxs[0] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(0.33, .17, -.1)
				.setRotation(0.0D, -.2D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
		boxs[1] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(-.1275, .15, .17)
				.setRotation(0.12D, -.13D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
		boxs[2] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(0, .18, .975)
				.setRotation(0.35D, -.18D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
		boxs[3] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(.5, .125, 1.2)
				.setRotation(0.5D, -.14D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
		boxs[4] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(1.1, .125, 0.75)
				.setRotation(0.666D, -.16D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
		boxs[5] = new RenderBox(0D, 0D, 0D, .25D, .75D, .25D)
				.setTranslate(0.9, .125, 0.08)
				.setRotation(0.833D, -.16D, 0D)
				.setCustomIconOffset(0D, 0D, 0D, 0D, 0D, 0D)
				.initRender();
		for(RenderBox box : boxs)
		{
			box.setIcon(Icons.firewood_top, Icons.firewood_side);
			box.render();
		}
	}
}