package fle.core.render;

import fle.api.energy.IThermalTileEntity;
import fle.core.te.TileEntityThermalWire;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderThermalWire extends RenderBase
{
	float f1 = 0.5F;
	
	@Override
	public void renderBlock()
	{
		setTexture(block);
		setColor(block.colorMultiplier(world, x, y, z));
		float f2 = f1 - getSize();
		float f3 = f1 + getSize();
		renderBlock(f2, f2, f2, f3, f3, f3);
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(!canConnect(dir)) continue;
			double x1 = dir.offsetX == -1 ? 0F : dir.offsetX == 1 ? f3 : f2;
			double y1 = dir.offsetY == -1 ? 0F : dir.offsetY == 1 ? f3 : f2;
			double z1 = dir.offsetZ == -1 ? 0F : dir.offsetZ == 1 ? f3 : f2;
			double x2 = dir.offsetX == -1 ? f2 : dir.offsetX == 1 ? 1F : f3;
			double y2 = dir.offsetY == -1 ? f2 : dir.offsetY == 1 ? 1F : f3;
			double z2 = dir.offsetZ == -1 ? f2 : dir.offsetZ == 1 ? 1F : f3;
			renderBlock(x1, y1, z1, x2, y2, z2);
		}
	}
	
	private boolean canConnect(ForgeDirection dir)
	{
		return world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) instanceof IThermalTileEntity;
	}
	
	private float getSize()
	{
		return 0.25F;
	}
}