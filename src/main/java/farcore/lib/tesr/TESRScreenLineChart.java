/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tesr;

import org.lwjgl.opengl.GL11;

import farcore.lib.tile.abstracts.TEScreenLineChart;
import nebula.client.render.ITESRScreenRender;
import nebula.client.render.TESRBase;
import nebula.common.util.Direction;
import nebula.common.util.Entities;
import nebula.common.util.Maths;
import nebula.common.util.Players;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRScreenLineChart extends TESRBase<TEScreenLineChart> implements ITESRScreenRender<TEScreenLineChart>
{
	@Override
	public void renderTileEntityAt(TEScreenLineChart chart, double x, double y, double z, float partialTicks, int destroyStage)
	{
		if (chart.getDistanceSq(Players.player()) < 256.0)
		{
			GL11.glPushMatrix();
			Direction direction = chart.getRotation();
			GL11.glTranslated(x + .5F, y, z + .5F);
			GL11.glRotatef(direction.of().getHorizontalAngle(), 0, -1, 0);
			GL11.glTranslatef(-.5F, 0, -.5F - 0.01F);
			GlStateManager.disableLighting();
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			renderScreen(chart, 2.0F * (0.01F + (float) MathHelper.fastInvSqrt(Entities.distanceSq(Players.player(), chart.pos()))), 1, 1, partialTicks);
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
			GlStateManager.enableLighting();
			GL11.glPopMatrix();
		}
	}
	
	@Override
	public void renderScreen(TEScreenLineChart chart, float lineScale, double width, double height, float partialTicks)
	{
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GL11.glScaled(width, height, 1);
		
		GL11.glLineWidth(lineScale);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.helper.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		for (int i = 1; i < 4; ++i)
		{
			this.helper.vertex_p(0.0, i * 0.25, 0.0).vertex_p(1.0, i * 0.25, 0.0);
		}
		this.helper.draw();
		
		GL11.glLineWidth(2.0F * lineScale);
		this.helper.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		this.helper.vertex_p(0.0, 0.0, 0.0).vertex_p(1.0, 0.0, 0.0);
		this.helper.vertex_p(1.0, 0.0, 0.0).vertex_p(1.0, 1.0, 0.0);
		this.helper.draw();
		
		GL11.glLineWidth(lineScale);
		GL11.glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
		this.helper.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
		int[] heights = chart.getLineHeight();
		int l = heights.length - 1;
		float delay = chart.getDelayTick(partialTicks);
		float mov = Maths.lerp(0.0F, 1.0F / l, delay);
		this.helper.vertex_p(0, chart.rescaleValue(Maths.lerp(heights[1], heights[0], delay)), 0);
		for (int i = 1; i < l; ++i)
		{
			this.helper.vertex_p((float) i / (float) l - mov, chart.rescaleValue(heights[i]), 0.0);
		}
		this.helper.vertex_p(1, chart.rescaleValue(Maths.lerp(heights[l], heights[l - 1], delay)), 0);
		this.helper.draw();
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}
}
