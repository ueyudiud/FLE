/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.client.render;

import org.lwjgl.opengl.GL11;

import fle.core.tile.tanks.TESmallRockyTank;
import nebula.client.render.TESRBase;
import nebula.common.util.Maths;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRSmallRockyTank extends TESRBase<TESmallRockyTank>
{
	private static final float a = 0.25F;
	private static final float b = 0.75F;
	
	@Override
	public void renderTileEntityAt(TESmallRockyTank te, double x, double y, double z, float partialTicks,
			int destroyStage)
	{
		this.helper.setIconCoordScale(1.0F);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		GlStateManager.disableLighting();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		//Render fluid.
		{
			GlStateManager.enableBlend();
			IFluidTank tank = te.getFluidTank();
			if (tank.getFluid() != null)
			{
				float height = Maths.lerp(a, 1 - 6.25E-2F, (float) tank.getFluidAmount() / (float) tank.getCapacity());
				renderFluidFace(a, a, b, b, height, height, height, height, tank.getFluid(), null);
			}
			GlStateManager.disableBlend();
		}
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}
}