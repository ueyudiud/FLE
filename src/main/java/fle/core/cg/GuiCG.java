package fle.core.cg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import farcore.asm.ClientOverride;
import farcore.lib.collection.Ety;
import farcore.lib.stack.AbstractStack;
import farcore.lib.stack.ArrayStack;
import farcore.lib.stack.BaseStack;
import farcore.lib.stack.OreStack;
import farcore.util.U;
import fle.api.cg.CraftGuideRegister;
import fle.api.cg.IDisplayable;
import fle.api.cg.IGuiRecipeHanler;
import fle.api.cg.IRecipeDisplayHelper;
import fle.api.cg.renderelement.IPage;
import fle.api.cg.renderelement.ITransferBox;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCG extends GuiScreen implements IGuiRecipeHanler, IRecipeDisplayHelper
{
	private final List<IDisplayable> allowanceDisplayables;
	private final RenderItem itemRender;
	private final Map<IDisplayable, Integer> cacheID = new HashMap();
	private final List<Entry<IDisplayable, List<IPage>>> cache = new ArrayList();
	/** The X size of the inventory window in pixels. */
	protected int xSize = 176;
	/** The Y size of the inventory window in pixels. */
	protected int ySize = 166;
	/** Starting X position for the Gui. Inconsistent use for Gui backgrounds. */
	protected int guiLeft;
	/** Starting Y position for the Gui. Inconsistent use for Gui backgrounds. */
	protected int guiTop;
	private List<IPage> cachePages;
	private Random random;
	private int tick;
	private int printID;
	private IDisplayable selectDisplayable;
	private IPage selectPage;
	private ITransferBox renderingBox;
	
	public GuiCG(List<IDisplayable> allowanceDisplayables)
	{
		itemRender = mc.getRenderItem();
		this.allowanceDisplayables = allowanceDisplayables;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		++tick;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		int i = guiLeft;
		int j = guiTop;
		GL11.glPushMatrix();
		GlStateManager.translate(i, j, 0.0F);
		List<ITransferBox> boxs = selectDisplayable.getTransferBoxs(selectPage);
		for(printID = 0; printID < boxs.size(); ++printID)
		{
			boxs.get(printID).renderTransferBoxBackground(this);
		}
		GL11.glPopMatrix();
		
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		super.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glPushMatrix();
		GlStateManager.translate(i, j, 0.0F);
		GlStateManager.enableRescaleNormal();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		for(printID = 0; printID < boxs.size(); ++printID)
		{
			boxs.get(printID).renderTransferBoxFrontground(this);
		}
		GL11.glPopMatrix();
		RenderHelper.enableGUIStandardItemLighting();
	}

	private <T> T randTickSelect(Collection<T> collection)
	{
		long i = (tick / 20) * 375917294782L + printID * 475917393219L;
		random.setSeed(i);
		return U.L.random(collection, random);
	}

	@Override
	public void bindTexture(ResourceLocation location)
	{
		mc.renderEngine.bindTexture(location);
	}
	
	@Override
	public void drawItemStack(int x, int y, AbstractStack stack)
	{
		drawItemStack(x, y, stack, true);
	}

	@Override
	public void drawItemStack(int x, int y, AbstractStack stack, boolean randomDisplayStack)
	{
		if(stack == null || stack.instance() == null) return;
		drawItemStack(x, y, randomDisplayStack ? randTickSelect(stack.display()) : stack.instance(), getDefaultRenderText(stack));
	}

	@Override
	public void drawItemStack(int x, int y, ItemStack stack)
	{
		drawItemStack(x, y, stack, getDefaultRenderText(stack));
	}

	private String getDefaultRenderText(ItemStack stack)
	{
		return stack == null ? "N" :
			stack.stackSize != 1 ? "" + stack.stackSize : null;
	}
	
	private String getDefaultRenderText(AbstractStack stack)
	{
		return stack == null ? "N" :
			stack instanceof OreStack ? "O" :
				stack instanceof ArrayStack ? "A" :
					stack instanceof BaseStack ? (((BaseStack) stack).isWildcardValue ? "*" : null) : null;
	}
	
	private void drawItemStack(int x, int y, ItemStack stack, String text)
	{
		if(stack == null) return;
		RenderHelper.enableStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		zLevel = 200.0F;
		itemRender.zLevel = 200.0F;
		itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		if(text != null)
		{
			FontRenderer font = null;
			if (stack != null)
			{
				font = stack.getItem().getFontRenderer(stack);
			}
			if (font == null)
			{
				font = U.Client.getFontRender();
			}
			ClientOverride.renderCustomItemOverlayIntoGUI(itemRender, font, stack, x, y, text);
		}
		zLevel = 0.0F;
		itemRender.zLevel = 0.0F;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
	}

	@Override
	public void drawString(int x, int y, String text)
	{
		drawString(x, y, text, 0xFF000000);
	}

	@Override
	public void drawString(int x, int y, String text, int rgba)
	{
		drawString(U.Client.getFontRender(), x, y, text, rgba);
	}

	@Override
	public void drawString(FontRenderer renderer, int x, int y, String text, int rgba)
	{
		renderer.drawString(text, x, y, rgba);
	}
	
	@Override
	public void drawTooltip(int x, int y, List<String> tooltips)
	{
		drawTooltip(U.Client.getFontRender(), x, y, tooltips);
	}
	
	@Override
	public void drawTooltip(FontRenderer renderer, int x, int y, List<String> tooltips)
	{
		drawHoveringText(tooltips, x, y, renderer);
	}
	
	@Override
	public void drawFluidStack(int x, int y, int w, int h, Fluid stack)
	{
	}
	
	@Override
	public void drawFluidStack(int x, int y, int w, int h, float scale, FluidStack stack)
	{
	}
	
	@Override
	public void drawFluidStack(int x, int y, IFluidTank stack)
	{
	}
	
	@Override
	public void drawFluidStack(int x, int y, ItemStack stack)
	{
	}
	
	@Override
	public void drawTextureRect(int x, int y, int u, int v, int w, int h)
	{
		drawTexturedModalRect(x, y, u, v, w, h);
	}
	
	@Override
	public void drawColorRect(int x, int y, int u, int v, int rgba)
	{
		drawRect(x, y, x + u, y + v, rgba << 8 | rgba >> 24);
	}
	
	@Override
	public void drawLine(int x1, int y1, int x2, int y2, int rgba)
	{
		float r = (rgba >> 24 & 0xFF) / 255F;
		float g = (rgba >> 16 & 0xFF) / 255F;
		float b = (rgba >>  8 & 0xFF) / 255F;
		float a = (rgba       & 0xFF) / 255F;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(r, g, b, a);
		vertexbuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(x1, y1, 0.0D).endVertex();
		vertexbuffer.pos(x2, y2, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	@Override
	public void openGUI(Object mod, int id)
	{
		mc.thePlayer.openGui(mod, id, mc.theWorld, (int) mc.thePlayer.posX, (int) mc.thePlayer.posY, (int) mc.thePlayer.posZ);
	}
	
	@Override
	public void closeGUI()
	{
		mc.thePlayer.closeScreen();
	}
	
	@Override
	public void transferPage(IDisplayable displayable)
	{
		cache.clear();
		cache.add(0, new Ety(displayable, displayable.getAllowancePages()));
		selectDisplayable = displayable;
		initPage();
	}
	
	@Override
	public void transferByObject(String transferKey, Object transferItem, boolean detectedInAll)
	{
		cache.clear();
		Entry<IDisplayable, List<IPage>> entry;
		List<IPage> list;
		if(detectedInAll)
		{
			for(IDisplayable displayable : CraftGuideRegister.DISPLAYABLES)
			{
				list = displayable.getPages(transferKey, transferItem);
				if(list == null || list.isEmpty())
				{
					continue;
				}
				entry = new Ety(displayable, list);
				cache.add(entry);
			}
		}
		else
		{
			for(IDisplayable displayable : allowanceDisplayables)
			{
				list = displayable.getPages(transferKey, transferItem);
				if(list == null || list.isEmpty())
				{
					continue;
				}
				entry = new Ety(displayable, list);
				cache.add(entry);
			}
		}
		initPage();
	}
	
	protected void initPage()
	{
		if(selectDisplayable == null)
		{
			selectDisplayable = cache.get(0).getKey();
		}
		cachePages = cache.get(cacheID.get(selectDisplayable)).getValue();
		selectPage = cachePages.get(0);
	}
}