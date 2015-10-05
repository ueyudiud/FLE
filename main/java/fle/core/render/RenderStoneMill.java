package fle.core.render;

import org.lwjgl.opengl.GL11;

import fle.core.te.TileEntityStoneMill;
import net.minecraft.init.Blocks;

public class RenderStoneMill extends RenderBase
{
	boolean init = false;
	double d1 = 0.0D;
	double d2 = 0.25D;
	double d3 = 0.375D;
	double d4 = 0.5D;
	double d5 = 1 - d3;
	double d6 = 1 - d2;
	double d7 = 1 - d1;
	double d8 = 0.2D;
	double d9 = d8 * 2;
	double d10 = d9 * 2;
	double d11 = 0.125D;
	double d12 = 1D - d11;
	double d13 = 0.0078125D;
	RenderBox rb = new RenderBox(d11, d9, d11, d12, d10, d12);
	
	@Override
	public void renderBlock()
	{
		if(!init)
		{
			rb.setIcon(block);
			rb.setTranslate(0.5F, 0.5F, 0.5F);
			rb.setOffset(-0.5F, -0.5F, -0.5F);
			init = true;
		}
		double r = 0.0D;
		if(!isItem())
		{
			if(world.getTileEntity(x, y, z) instanceof TileEntityStoneMill)
			{
				r = ((TileEntityStoneMill) world.getTileEntity(x, y, z)).getRotation();
			}
		}
		setTexture(Blocks.planks);
		renderBlock(d1, d1, d1, d2, d9, d2);
		renderBlock(d6, d1, d1, d7, d9, d2);
		renderBlock(d6, d1, d6, d7, d9, d7);
		renderBlock(d1, d1, d6, d2, d9, d7);

		renderBlock(d2, d8, d1, d6, d9, d2);
		renderBlock(d1, d8, d2, d2, d9, d6);
		renderBlock(d6, d8, d2, d7, d9, d6);
		renderBlock(d2, d8, d6, d6, d9, d7);

		renderBlock(d4, d8, d2, d5, d9, d4);
		renderBlock(d2, d8, d3, d4, d9, d4);
		renderBlock(d3, d8, d4, d4, d9, d6);
		renderBlock(d4, d8, d4, d6, d9, d5);
		
		rb.setRotation(r / Math.PI);
		rb.render();
	}
}