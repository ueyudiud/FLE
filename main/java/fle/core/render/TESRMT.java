package fle.core.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

import org.lwjgl.opengl.GL11;

import fle.core.te.tank.TileEntityMultiTank;

public class TESRMT extends TESRBase<TileEntityMultiTank>
{
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
    }
    
	@Override
	public void renderTileEntityAt(TileEntityMultiTank tile, double x, double y, double z)
	{
		if (!tile.isMultiTank())
            return;
		if(!tile.isMainTile() || tile.isInvalid())
			return;
		
 		FluidTankInfo tank = tile.getMainTank().getInfo();

		if(tank.fluid != null)
		{
	        float vOffset = tile.width / 2f;
	        float yOffset = tile.height / 2f;
	        float vScale = tile.height - 1;
	        float hScale = tile.width - 1;
            preGL();
            GL11.glTranslatef((float) x + vOffset + 0.5F, (float) y + yOffset + 0.01f, (float) z + vOffset + 0.5F);
            GL11.glScalef(hScale, vScale, hScale);

            //GL11.glScalef(0.999f, 1, 0.999f);
            int[] displayLists = getLiquidDisplayLists(tank.fluid.getFluid());
            if (displayLists != null)
            {
                GL11.glPushMatrix();

                float cap = tank.capacity;
                float level = (float) Math.min(tank.fluid.amount, cap) / cap;

                bindTexture(getFluidSheet(tank.fluid.getFluid()));
                setColorForTank(tank.fluid);
                GL11.glCallList(displayLists[(int) (level * (float) (DISPLAY_STAGES - 1))]);
                GL11.glPopMatrix();
            }

            postGL();
		}
	}
}