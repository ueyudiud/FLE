/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.client.render;

import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import nebula.client.render.TESRBase;
import nebula.common.util.Maths;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRFluidFase<T extends TileEntity> extends TESRBase<T>
{
	float						minX, minY, minZ, maxX, maxY, maxZ;
	Function<T, FluidTankInfo>	func;
	
	public TESRFluidFase(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Function<T, FluidTankInfo> func)
	{
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
		this.func = func;
	}
	
	@Override
	public void renderTileEntityAt(T tile, double x, double y, double z, float partialTicks, int destroyStage)
	{
		this.helper.setIconCoordScale(1.0F);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GlStateManager.disableLighting();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		// Render fluid.
		{
			GlStateManager.enableBlend();
			FluidTankInfo tank = this.func.apply(tile);
			if (tank.fluid != null)
			{
				float height = Maths.lerp(this.minY, this.maxY, (float) tank.fluid.amount / (float) tank.capacity);
				renderFluidFace(this.minX, this.minZ, this.maxX, this.maxZ, height, height, height, height, tank.fluid, null);
			}
			GlStateManager.disableBlend();
		}
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}
}
