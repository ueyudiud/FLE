package fle.core.render;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import flapi.util.FleLog;
import fle.api.te.IDitchTile;
import fle.core.block.TileEntityDitch;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTank;

public class TESRDitch extends TESRBase<TileEntityDitch>
{
	private static ForgeDirection dirs[] = {ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};

	private static final byte stop = 0,
			connect = 1,
			fill = 2;
	double f = 0.0625,
			f1 = 0.0,
			f2 = 1.0,
			f3 = 0.375,
			f4 = 0.625,
			f5 = f3 - f,
			f6 = f4 + f,
			f7 = 0.25,
			f8 = 0.5,
			f9 = f7 - f,
			f10 = -0.375F,
			f11 = 1.375F,
			f12 = -0.625F,
			f13 = 1.625F,
			f14 = -0.5F;
	
	@Override
	public void renderTileEntityAt(TileEntityDitch tile, double x,
			double y, double z)
	{
		byte[] bs;
		FluidTank fluid = null;
		double[] ds = null;
		int fluidColor = 0xFFFFFF;

		try
		{
			bs = new byte[4];
			for(int i = 0; i < dirs.length; ++i)
				bs[i] = tile.getType(dirs[i]);
			fluid = (FluidTank) tile.getTank(0);
			if(fluid != null && fluid.getFluidAmount() > 0)
			{
				fluidColor = fluid.getFluid().getFluid().getColor(fluid.getFluid());
			}
			if(fluid != null)
			{
				ds = new double[5];
				int level = tile.getWaterLevel();
				ds[4] = (double) level / 256.0D;
				if(ds[4] > 1D) ds[4] = 1D;
				for(int i = 0; i < dirs.length; ++i)
				{
					if(tile.getBlockPos().toPos(dirs[i]).getBlockTile() instanceof IDitchTile)
					{
						ds[i] = (double) (level * 2 + ((IDitchTile) tile.getBlockPos().toPos(dirs[i]).getBlockTile()).getWaterLevel()) / 768D;
					}
					else
					{
						ds[i] = (double) (level * 2) / (256D * 3);
					}
					if(ds[i] > 1D) ds[i] = 1D;
				}
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			fluid = null;
			ds = null;
			bs = new byte[]{stop, stop, stop, stop};
		}

		if(fluid.getFluidAmount() > 0)
		{
            //int[] displayLists = getLiquidDisplayLists(fluid.getFluid().getFluid());
	        float red = (fluidColor >> 16 & 255) / 255.0F;
	        float green = (fluidColor >> 8 & 255) / 255.0F;
	        float blue = (fluidColor & 255) / 255.0F;
			GL11.glColor4f(red, green, blue, 1.0F);
			renderFluidBlock(fluid, x + f3, y + f7, z + f3, x + f4, y + f7 + (f8 - f7) * ds[4], z + f4);
			switch(bs[0])
			{
			case fill : 
				renderFluidBlock(fluid, x + f3, y + f7, z + f10, x + f4, y + f7 + (f8 - f7) * ds[0] / 1.5D, z + f1);
				renderFluidBlock(fluid, x + f3, y + f14, z + f12, x + f4, y + f7 + (f8 - f7) * ds[0] / 3D, z + f10);
			case connect : renderFluidBlock(fluid, x + f3, y + f7, z + f1, x + f4, y + f7 + (f8 - f7) * ds[0], z + f3);
			}
			switch(bs[1])
			{
			case fill : 
				renderFluidBlock(fluid, x + f2, y + f7, z + f3, x + f11, y + f7 + (f8 - f7) * ds[1] / 1.5D, z + f4);
				renderFluidBlock(fluid, x + f11, y + f14, z + f3, x + f13, y + f7 + (f8 - f7) * ds[1] / 3D, z + f4);
			case connect : renderFluidBlock(fluid, x + f4, y + f7, z + f3, x + f2, y + f7 + (f8 - f7) * ds[1], z + f4);
			}
			switch(bs[2])
			{
			case fill : 
				renderFluidBlock(fluid, x + f3, y + f7, z + f2, x + f4, y + f7 + (f8 - f7) * ds[2] / 1.5D, z + f11);
				renderFluidBlock(fluid, x + f3, y + f14, z + f11, x + f4, y + f7 + (f8 - f7) * ds[2] / 3D, z + f13);
			case connect : renderFluidBlock(fluid, x + f3, y + f7, z + f4, x + f4, y + f7 + (f8 - f7) * ds[2], z + f2);
			}
			switch(bs[3])
			{
			case fill : 
				renderFluidBlock(fluid, x + f10, y + f7, z + f3, x + f1, y + f7 + (f8 - f7) * ds[3] / 1.5D, z + f4);
				renderFluidBlock(fluid, x + f12, y + f14, z + f3, x + f10, y + f7 + (f8 - f7) * ds[3] / 3D, z + f4);
			case connect : renderFluidBlock(fluid, x + f1, y + f7, z + f3, x + f3, y + f7 + (f8 - f7) * ds[3], z + f4);
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	private void renderFluidBlock(FluidTank fluid, double minX, double minY, double minZ,
			double maxX, double maxY, double maxZ)
	{
        preGL();
        GL11.glTranslatef((float) (minX + maxX) / 2, (float) (minY + maxY) / 2, (float) (minZ + maxZ) / 2);
        GL11.glScaled(maxX - minX, maxY - minY, maxZ - minZ);

        int[] displayLists = getLiquidDisplayLists(fluid.getFluid().getFluid());
        if (displayLists != null)
        {
            GL11.glPushMatrix();

            float cap = fluid.getCapacity();
            float level = (float) Math.min(fluid.getFluidAmount(), cap) / cap;

            bindTexture(getFluidSheet(fluid.getFluid().getFluid()));
            setColorForTank(fluid.getFluid());
            GL11.glCallList(displayLists[(int) (level * (float) (DISPLAY_STAGES - 1))]);
            GL11.glPopMatrix();
        }

        postGL();
	}
	
	private void preGL()
	{
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void postGL()
    {
        GL11.glPopAttrib();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}