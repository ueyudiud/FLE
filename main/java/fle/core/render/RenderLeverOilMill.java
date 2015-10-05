package fle.core.render;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.world.BlockPos;
import fle.core.te.TileEntityOilMill;

public class RenderLeverOilMill extends RenderBase
{
	double d1 = 0.0D;
	double d2 = 0.25D;
	double d3 = 0.375D;
	double d4 = 0.5D;
	double d5 = 1 - d3;
	double d6 = 1 - d2;
	double d7 = 1 - d1;
	double d8 = 0.2D;
	double d9 = d8 * 2;
	double d10 = 0.0625;
	double d11 = 0.7;
	double d12 = 1.0;
	double a = 0.0625;
	
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
		
		setTexture(Blocks.stone);
		renderBlock(d2, d10, d2, d6, d11, d2 + d10);
		renderBlock(d2, d10, d6 - d10, d6, d11, d6);
		renderBlock(d2, d10, d2, d2 + d10, d11, d6);
		renderBlock(d6 - d10, d10, d2, d6, d11, d6);
		
		renderBlock(d2 + d10, d10, d2 + d10, d6 - d10, d5, d6 - d10);
		
		float height = 0;
		if(isItem()) dir = ForgeDirection.SOUTH;
		else
		{
			if(world.getTileEntity(x, y, z) instanceof TileEntityOilMill)
			{
				dir = ((TileEntityOilMill) world.getTileEntity(x, y, z)).getDirction(new BlockPos(world, x, y, z));
				height = (float) ((TileEntityOilMill) world.getTileEntity(x, y, z)).getCache(64) / 512F;
			}
			else
			{
				dir = ForgeDirection.NORTH;
			}
		}
		
		setTexture(Blocks.planks);
		switch(dir)
		{
		case DOWN:;
		case UP:;
		case NORTH :;
		renderBlock(d3, d9, d1, d5, d12, d2);
		break;
		case SOUTH :;
		renderBlock(d3, d9, d6, d5, d12, d7);
		break;
		case WEST :;
		renderBlock(d1, d9, d3, d2, d12, d5);
		break;
		case EAST :;
		renderBlock(d6, d9, d3, d7, d12, d5);
		break;
		default: break;
		}
		double offset = Math.tan((0.125 - height * 2) * Math.PI) * 0.4;
		setTexture(Blocks.stone);
		renderBlock(d2 + d10, d5 + offset, d2 + d10, d6 - d10, d5 + a + offset, d6 - d10);
		setTexture(block.getIcon(0, 4));
		renderBlock(d4 - a / 4, d11 + offset, d4 - a / 4, d4 + a / 4, (d11 + d12) / 2 + offset, d4 + a / 4);
		RenderBox box = new RenderBox(a, 1.5F, a);
		switch(dir)
		{
		case DOWN:;
		case UP:;
		case NORTH :;
		box.setTranslate(d4 - a / 2, (d11 + d12) / 2, (d1 + d2) / 2);
		box.setRotation(0, 0.25 + 0.0625 - height, 0);
		box.setOffset(0, 0, -d4);
		break;
		case SOUTH :;
		box.setTranslate(d4 - a / 2, (d11 + d12) / 2, (d6 + d7) / 2);
		box.setRotation(0, 0.75 - 0.0625 + height, 0);
		box.setOffset(0, 0, -d4);
		break;
		case WEST :;
		box.setTranslate((d1 + d2) / 2, (d11 + d12) / 2, d4 - a / 2);
		box.setRotation(0, 0, 0.75 - 0.0625 + height);
		box.setOffset(0, 0, -d4);
		break;
		case EAST :;
		box.setTranslate((d6 + d7) / 2, (d11 + d12) / 2, d4 - a / 2);
		box.setRotation(0, 0, 0.25 + 0.0625 - height);
		box.setOffset(0, 0, -d4);
		break;
		default:;
		box = new RenderBox(a, 1.0F, a);
		break;
		}
		box.setIcon(Blocks.log, 0);
		box.render();
	}
}