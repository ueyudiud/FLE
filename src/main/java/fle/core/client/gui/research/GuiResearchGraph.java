/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.client.gui.research;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import fle.core.FLE;
import fle.core.common.gui.research.ContainerResearchGraph;
import fle.core.research.ResearchGraph.ResearchInstanceProxyClient;
import nebula.client.gui.GuiContainerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiResearchGraph extends GuiContainerBase
{
	public static final ResourceLocation LOCATION = new ResourceLocation(FLE.MODID, "textures/gui/research/researching1.png");
	
	private ResearchInstanceProxyClient instance;
	
	public GuiResearchGraph(ResearchInstanceProxyClient instance, EntityPlayer player, World world, BlockPos pos)
	{
		super(new ContainerResearchGraph(instance, player, world, pos, false), LOCATION, 250, 249);
		this.instance = instance;
	}
	
	@Override
	protected void drawOther(int mouseX, int mouseY)
	{
		super.drawOther(mouseX, mouseY);
		
		int[][] p = this.instance.getPositions();
		float[][] pos = new float[p.length][2];
		for (int i = 0; i < p.length; ++i)
		{
			if (i == this.instance.clickingNode)
			{
				pos[i][0] = mouseX - (this.guiLeft + 19 - 1);
				pos[i][1] = mouseY - (this.guiTop + 5 - 1);
			}
			else
			{
				pos[i][0] = 9 * p[i][0] + 3.5F;
				pos[i][1] = 9 * p[i][1] + 3.5F;
			}
		}
		int[][] links = this.instance.getLinks();
		GL11.glLineWidth(3.0F);
		GL11.glPointSize(5.0F);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_POINT_SMOOTH);
		GL11.glPushMatrix();
		GL11.glTranslatef(this.guiLeft + 19 - 1, this.guiTop + 5 - 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 0.0F, 1.0F);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		for (int[] link : links)
		{
			buffer.pos(pos[link[0]][0], pos[link[0]][1], this.zLevel).endVertex();
			buffer.pos(pos[link[1]][0], pos[link[1]][1], this.zLevel).endVertex();
		}
		tessellator.draw();
		GL11.glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
		buffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION);
		for (float[] po : pos)
		{
			buffer.pos(po[0], po[1], this.zLevel).endVertex();
		}
		tessellator.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		boolean flag = mouseButton == 0 && isPointInRegion(18, 4, 214, 214, mouseX, mouseY);
		if (!flag)
		{
			this.instance.clickingNode = -1;
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (Minecraft.getMinecraft().player.inventory.getItemStack() == null)
		{
			if (flag)
			{
				int x = (mouseX - this.guiLeft - 18 + 1) / 9;
				int y = (mouseY - this.guiTop - 4 + 1) / 9;
				int id1 = this.instance.getClickingNodeID(x, y);
				if (this.instance.clickingNode == -1)
				{
					this.instance.clickingNode = id1;
				}
				else
				{
					if (id1 == -1)
					{
						this.instance.moveNodePos(this.instance.clickingNode, x, y);
						this.instance.refreshGraph();
						this.instance.clickingNode = -1;
						if (this.instance.isFinished())
						{
							Minecraft.getMinecraft().player.closeScreen();
							return;
						}
					}
					else
						this.instance.clickingNode = -1;
				}
			}
		}
		else
			this.instance.clickingNode = -1;
	}
}
