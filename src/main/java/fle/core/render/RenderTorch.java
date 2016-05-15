package fle.core.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.Direction;
import farcore.lib.render.RenderBase;
import fle.load.BlockItems;

@SideOnly(Side.CLIENT)
public class RenderTorch extends RenderBase
{
	private RenderBox box_U;
	private RenderBox box_N;
	private RenderBox box_S;
	private RenderBox box_W;
	private RenderBox box_E;
	
	public RenderTorch()
	{
		box_U = new RenderBox(7, 0, 7, 9, 10, 9, PIXEL_LENGTH)
				.setCustomIconOffset(7, 6, 7, 6, 7, 14, 7, 6, 7, 6, 7, 6)
				.initRender();
		box_S = new RenderBox(7, 0, 7, 9, 10, 9, PIXEL_LENGTH)
				.setCustomIconOffset(7, 6, 7, 6, 7, 14, 7, 6, 7, 6, 7, 6)
				.setTranslate(1, .5, -.475)
				.setRotation(.75, .075)
				.initRender();
		box_N = new RenderBox(7, 0, 7, 9, 10, 9, PIXEL_LENGTH)
				.setCustomIconOffset(7, 6, 7, 6, 7, 14, 7, 6, 7, 6, 7, 6)
				.setTranslate(0, .5, 1.475)
				.setRotation(.25, .075)
				.initRender();
		box_W = new RenderBox(7, 0, 7, 9, 10, 9, PIXEL_LENGTH)
				.setCustomIconOffset(7, 6, 7, 6, 7, 14, 7, 6, 7, 6, 7, 6)
				.setTranslate(1.475, .5, 1)
				.setRotation(.5, .075)
				.initRender();
		box_E = new RenderBox(7, 0, 7, 9, 10, 9, PIXEL_LENGTH)
				.setCustomIconOffset(7, 6, 7, 6, 7, 14, 7, 6, 7, 6, 7, 6)
				.setTranslate(-.475, .5, 0)
				.setRotation(.0, .075)
				.initRender();
	}
	
	@Override
	public void renderBlock()
	{
		switch (meta)
		{
		case 1 :
			box_U.setIcon(BlockItems.torch);
			box_U.render();
			break;
		case 2 :
			box_N.setIcon(BlockItems.torch);
			box_N.render();
			break;
		case 3 :
			box_S.setIcon(BlockItems.torch);
			box_S.render();
			break;
		case 4 :
			box_W.setIcon(BlockItems.torch);
			box_W.render();
			break;
		case 5 :
			box_E.setIcon(BlockItems.torch);
			box_E.render();
			break;
		default:
			break;
		}
	}
}