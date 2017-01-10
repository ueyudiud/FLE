/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.client.render;

import javax.vecmath.Vector2f;

import org.lwjgl.opengl.GL11;

import farcore.lib.tesr.TESRBase;
import farcore.lib.util.Direction;
import farcore.util.Maths;
import fle.core.tile.ditchs.TEDitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
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
	private static final float POS1 = 7.0F / 16.0F;
	private static final float POS2 = 9.0F / 16.0F;
	
	@Override
	public void renderTileEntityAt(TEDitch tile, double x, double y, double z, float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GlStateManager.disableLighting();
		GL11.glTranslated(x, y, z);
		GlStateManager.enableBlend();
		float height0 = tile.getFlowHeight();
		float height1 = getFlowHeight(height0, tile, Direction.N);//--Z
		float height2 = getFlowHeight(height0, tile, Direction.S);//++Z
		float height3 = getFlowHeight(height0, tile, Direction.W);//--X
		float height4 = getFlowHeight(height0, tile, Direction.E);//++X
		float ha = Maths.lerp(height0, height1, WEIGHT2);
		float hb = Maths.lerp(height0, height2, WEIGHT2);
		float hc = Maths.lerp(height0, height3, WEIGHT2);
		float hd = Maths.lerp(height0, height4, WEIGHT2);
		FluidTank tank = tile.getTank();
		if(tank.getFluid() != null)
		{
			FluidStack stack = tank.getFluid();
			setColor(stack);
			TextureAtlasSprite icon = getTexture(stack.getFluid().getStill(stack));
			renderFluidFace(0F, 0F, 1F, 1F,
					(ha + hc) / 2F, (hb + hc) / 2F, (hb + hd) / 2F, (hb + hc) / 2F,
					stack, new Vector2f(1.0F, 0.0F));
		}
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}
	
	private float getFlowHeight(float base, TEDitch tile, Direction side)
	{
		return base;
		//		if(!tile.isLinked(side)) return base;
		//		TileEntity t1 = tile.getTE(side);
		//		if(t1 instanceof IDitchTile)
		//		{
		//			return ((IDitchTile) t1).getFlowHeight();
		//		}
		//		else if(t1.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite().of()) &&
		//				t1.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite().of()).getTankProperties().length > 0)
		//		{
		//			return Math.max(base - 0.0625F, 0F);
		//		}
		//		else if(t1 instanceof IFluidHandler)
		//		{
		//			return Math.max(base - 0.0625F, 0F);
		//		}
		//		else return base;
	}
}