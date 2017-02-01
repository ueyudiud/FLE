/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.client.render;

import static nebula.common.util.Maths.lerp;

import org.lwjgl.opengl.GL11;

import fle.api.tile.IDitchTile;
import fle.core.tile.ditchs.TEDitch;
import nebula.client.render.TESRBase;
import nebula.common.util.Direction;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRDitch extends TESRBase<TEDitch>
{
	private static final float WEIGHT1 = 8.0F / 16.0F;
	private static final float WEIGHT2 = 2.0F / 16.0F;
	private static final float P1 = 6.0F / 16.0F;
	private static final float P2 = 10.0F / 16.0F;
	private static final float P3 = 0.0F / 16.0F;
	private static final float P4 = 16.0F / 16.0F;
	
	@Override
	public void renderTileEntityAt(TEDitch tile, double x, double y, double z, float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GlStateManager.disableLighting();
		GL11.glTranslated(x, y, z);
		GlStateManager.enableBlend();
		float height0 = tile.getFlowHeight();
		float
		height1 = getFlowHeight(height0, tile, Direction.N),//--Z
		height2 = getFlowHeight(height0, tile, Direction.S),//++Z
		height3 = getFlowHeight(height0, tile, Direction.W),//--X
		height4 = getFlowHeight(height0, tile, Direction.E);//++X
		FluidTank tank = tile.getTank();
		if(tank.getFluid() != null)
		{
			FluidStack stack = tank.getFluid();
			float
			ha = lerp(height0, height1, WEIGHT2),
			hb = lerp(height0, height2, WEIGHT2),
			hc = lerp(height0, height3, WEIGHT2),
			hd = lerp(height0, height4, WEIGHT2),
			
			y1 = (ha + hc) * .125F + .25F,//x- z-
			y2 = (hb + hc) * .125F + .25F,//x- z+
			y3 = (hb + hd) * .125F + .25F,//x+ z+
			y4 = (hb + hc) * .125F + .25F,//x+ z-
			
			y5 = (height0 + height1) * .125F + .25F,
			y6 = (height0 + height2) * .125F + .25F,
			y7 = (height0 + height3) * .125F + .25F,
			y8 = (height0 + height4) * .125F + .25F;
			
			renderFluidFace(P1, P1, P2, P2, y1, y2, y3, y4, stack, STILL);
			switch(tile.getLinkState(Direction.N))
			{
			case 1 :
				renderFluidFace(P1, P3, P2, P1, y5, y1, y4, y5, stack, STILL);
				break;
			case 2 :
				renderFluidFace(P1, P3, P2, P1, y5, y1, y4, y5, stack, STILL);
				break;
			default: break;
			}
			switch(tile.getLinkState(Direction.S))
			{
			case 1 :
				renderFluidFace(P1, P2, P2, P4, y2, y6, y6, y3, stack, STILL);
				break;
			case 2 :
				renderFluidFace(P1, P2, P2, P4, y2, y6, y6, y3, stack, STILL);
				break;
			default: break;
			}
			switch(tile.getLinkState(Direction.W))
			{
			case 1 :
				renderFluidFace(P3, P1, P1, P2, y7, y7, y1, y2, stack, STILL);
				break;
			case 2 :
				renderFluidFace(P3, P1, P1, P2, y7, y7, y1, y2, stack, STILL);
				break;
			default: break;
			}
			switch(tile.getLinkState(Direction.E))
			{
			case 1 :
				renderFluidFace(P2, P1, P4, P2, y3, y4, y8, y8, stack, STILL);
				break;
			case 2 :
				renderFluidFace(P2, P1, P4, P2, y3, y4, y8, y8, stack, STILL);
				break;
			default: break;
			}
		}
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}
	
	private float getFlowHeight(float base, TEDitch tile, Direction side)
	{
		if(!tile.isLinked(side)) return base;
		TileEntity t1 = tile.getTE(side);
		if(t1 == null)
		{
			return base;
		}
		else if(t1 instanceof IDitchTile)
		{
			return ((IDitchTile) t1).getFlowHeight();
		}
		else if(t1.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite().of()) &&
				t1.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite().of()).getTankProperties().length > 0)
		{
			return Math.max(base - 0.0625F, base * .25F);
		}
		else return base;
	}
}