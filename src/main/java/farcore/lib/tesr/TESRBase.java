package farcore.lib.tesr;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRBase<T extends TileEntity> extends TileEntitySpecialRenderer<T>
{
	protected boolean enableColor = true;
	protected float red = 1.0F;
	protected float green = 1.0F;
	protected float blue = 1.0F;
	protected float alpha = 1.0F;
	
	protected TextureAtlasSprite getTexture(IBlockState state)
	{
		return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getTexture(state);
	}

	protected TextureAtlasSprite getTexture(ResourceLocation location)
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
	}

	protected void resetColor()
	{
		colorV(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	protected void colorV(float r, float g, float b, float a)
	{
		this.red = r;
		this.green = g;
		this.blue = b;
		this.alpha = a;
	}
	
	protected void face(Tessellator tessellator,
			double x, double y, double z,
			double uOfX, double uOfY, double uOfZ,
			double vOfX, double vOfY, double vOfZ,
			float u1, float v1, float u2, float v2,
			TextureAtlasSprite icon)
	{
		float ut1 = icon.getInterpolatedU(u1 * 16F);
		float ut2 = icon.getInterpolatedU(u2 * 16F);
		float vt1 = icon.getInterpolatedU(v1 * 16F);
		float vt2 = icon.getInterpolatedU(v2 * 16F);
		face(tessellator, x, y, z, uOfX, uOfY, uOfZ, vOfX, vOfY, vOfZ, ut1, vt1, ut2, vt2);
	}
	
	protected void face(Tessellator tessellator,
			double x, double y, double z,
			double uOfX, double uOfY, double uOfZ,
			double vOfX, double vOfY, double vOfZ,
			double u1, double v1, double u2, double v2)
	{
		VertexBuffer buffer = tessellator.getBuffer();
		if(enableColor)
		{
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		}
		else
		{
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		}
		double d0 = u1;
		double d1 = v1;
		double d2 = u2;
		double d3 = v2;
		double d5 = x;
		double d6 = x + uOfX;
		double d7 = x + uOfX + vOfX;
		double d8 = x + vOfX;
		double d9 = z;
		double d10 = z + uOfZ;
		double d11 = z + uOfZ + vOfZ;
		double d12 = z + vOfZ;
		double d13 = y;
		double d14 = y + uOfY;
		double d15 = y + uOfY + vOfY;
		double d16 = y + vOfY;
		vertex(buffer, d5, d13, d9, d2, d1);
		vertex(buffer, d6, d14, d10, d2, d3);
		vertex(buffer, d7, d15, d11, d0, d3);
		vertex(buffer, d8, d16, d12, d0, d1);
		vertex(buffer, d8, d16, d12, d0, d1);
		vertex(buffer, d7, d15, d11, d0, d3);
		vertex(buffer, d6, d14, d10, d2, d3);
		vertex(buffer, d5, d13, d9, d2, d1);
		tessellator.draw();
	}

	protected void vertex(VertexBuffer buffer, double x, double y, double z, double u, double v)
	{
		if(enableColor)
		{
			buffer.pos(x, y, z).tex(u, v).color(red, green, blue, alpha).endVertex();
		}
		else
		{
			buffer.pos(x, y, z).tex(u, v).endVertex();
		}
	}

	protected void vertex(VertexBuffer buffer, double x, double y, double z, double u, double v, float r, float g, float b, float a)
	{
		buffer.pos(x, y, z).tex(u, v).color(r, g, b, a).endVertex();
	}
}