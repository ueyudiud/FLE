/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import nebula.Nebula;
import nebula.client.ClientOverride;
import nebula.client.util.Client;
import nebula.common.NebulaKeyHandler;
import nebula.common.gui.ContainerBase;
import nebula.common.gui.FluidSlot;
import nebula.common.gui.IGuiActionListener;
import nebula.common.network.packet.PacketFluidSlotClick;
import nebula.common.network.packet.PacketGuiAction;
import nebula.common.network.packet.PacketGuiTickUpdate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiContainerBase extends GuiContainer
{
	private ResourceLocation	location;
	protected Slot				lastClickSlot;
	protected ContainerBase		container;
	protected boolean			translated = false;
	
	public void setLocation(ResourceLocation location)
	{
		this.location = location;
	}
	
	public GuiContainerBase(ContainerBase inventorySlotsIn, ResourceLocation location)
	{
		this(inventorySlotsIn, location, 176, 166);
	}
	
	public GuiContainerBase(ContainerBase inventorySlotsIn, ResourceLocation location, int width, int height)
	{
		super(inventorySlotsIn);
		this.container = inventorySlotsIn;
		this.fontRendererObj = Client.getFontRender();
		this.xSize = width;
		this.ySize = height;
		this.location = location;
	}
	
	/**
	 * Send GUI action to both client and server side container.
	 * 
	 * @param type the type of action.
	 * @param code the action code.
	 * @param processOnClient if it is <code>true</code>, the action will also
	 *            handle on client.
	 * @see nebula.common.gui.IGuiActionListener
	 */
	protected void sendGuiData(int type, long code, boolean processOnClient)
	{
		Nebula.network.sendToServer(new PacketGuiAction((byte) type, code, this.container));
		if (processOnClient && (this.inventorySlots instanceof IGuiActionListener))
		{
			((IGuiActionListener) this.inventorySlots).onRecieveGUIAction((byte) type, code);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String name = getTitleName();
		if (name != null)
		{
			this.fontRendererObj.drawString(name, (this.xSize - this.fontRendererObj.getStringWidth(name)) / 2, 6, 0x404040);
		}
	}
	
	public float getZLevel()
	{
		return this.zLevel;
	}
	
	public boolean startTranslate()
	{
		if (!this.translated)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(this.guiLeft, this.guiTop, 0);
			return true;
		}
		return false;
	}
	
	public void endTranslate(boolean mark)
	{
		if (mark)
		{
			GL11.glPopMatrix();
			this.translated = false;
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindDefaultTexture();
		GL11.glPushMatrix();
		GL11.glTranslatef(this.guiLeft, this.guiTop, 0);
		this.translated = true;
		drawTexturedModalRect(0, 0, 0, 0, this.xSize, this.ySize);
		drawOtherSlots();
		GL11.glPopMatrix();
		this.translated = false;
		bindDefaultTexture();
		drawOther(mouseX, mouseY);
	}
	
	protected void drawOtherSlots()
	{
		this.zLevel = 200F;
		for (FluidSlot slot : ((ContainerBase) this.inventorySlots).getFluidSlots())
		{
			slot.renderSlot(this);
		}
		this.zLevel = 0F;
	}
	
	protected void drawOther(int mouseX, int mouseY)
	{
		
	}
	
	protected String getTitleName()
	{
		return null;
	}
	
	protected boolean isTouchingMode()
	{
		return this.mc.gameSettings.touchscreen;
	}
	
	/**
	 * Check is key press down. This method will be used only if
	 * {@link net.minecraft.client.gui.GuiScreen#allowUserInput} is enabled.
	 * <p>
	 * The key checking is using for {@link #keyTyped(char, int)} method to use,
	 * checking the key registered at {@link nebula.common.NebulaKeyHandler} is
	 * pressed down.
	 * 
	 * @param key the key register name.
	 * @param keycode the typed keycode.
	 * @see nebula.common.NebulaKeyHandler
	 * @return is key pressed down.
	 */
	protected boolean matchKey(String key, int keycode)
	{
		return NebulaKeyHandler.getBinding(key).getKeyCode() == keycode;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (!this.dragSplitting)
		{
			FluidSlot slot = getFluidSlotAtPosition(mouseX, mouseY);
			ItemStack stack = Minecraft.getMinecraft().player.inventory.getItemStack();
			if (stack == null && isTouchingMode() && this.lastClickSlot != null && this.lastClickSlot.canTakeStack(Minecraft.getMinecraft().player))
			{
				stack = this.lastClickSlot.getStack();
			}
			if (slot != null && (mouseButton == 0 || mouseButton == 1))
			{
				Nebula.network.sendToServer(new PacketFluidSlotClick((ContainerBase) this.inventorySlots, slot.slotNumber));
				slot.onSlotClick(Minecraft.getMinecraft().player, stack);
			}
		}
		this.lastClickSlot = null;
		if (isTouchingMode())
		{
			for (Slot slot : this.inventorySlots.inventorySlots)
			{
				if (isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseY))
				{
					this.lastClickSlot = slot;
				}
			}
		}
	}
	
	protected FluidSlot getFluidSlotAtPosition(int x, int y)
	{
		for (FluidSlot slot : ((ContainerBase) this.inventorySlots).getFluidSlots())
		{
			if (slot.isVisible() && isPointInRegion(slot.x, slot.y, slot.u, slot.v, x, y)) return slot;
		}
		return null;
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if (this.container instanceof ITickable)
		{
			((ITickable) this.container).update();
		}
		Nebula.network.sendToServer(new PacketGuiTickUpdate(this.container));
	}
	
	public void bindDefaultTexture()
	{
		bindTexture(this.location);
	}
	
	public TextureAtlasSprite getTexture(IBlockState state)
	{
		return this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
	}
	
	public TextureAtlasSprite getTexutre(ResourceLocation location)
	{
		return this.mc.getTextureMapBlocks().getAtlasSprite(location.toString());
	}
	
	public void bindTexture(ResourceLocation location)
	{
		this.mc.renderEngine.bindTexture(location);
	}
	
	/**
	 * @see #drawFluid(int, int, FluidTankInfo, int, int, boolean)
	 */
	public void drawFluid(int x, int y, FluidTankInfo tank, int width, int height)
	{
		drawFluid(x, y, tank, width, height, false);
	}
	
	/**
	 * Draw fluid icon to GUI.
	 * 
	 * @param x the start x position.
	 * @param y the start y position.
	 * @param info the render tank information.
	 * @param width the rendering width.
	 * @param height the rendering height.
	 * @param lay <tt>true</tt> for rendering fluid from left to right, and from
	 *            down to up else.
	 */
	public void drawFluid(int x, int y, FluidTankInfo info, int width, int height, boolean lay)
	{
		if (info.fluid == null) return;
		if (info.fluid.amount > 0)
		{
			TextureAtlasSprite fluidIcon = this.mc.getTextureMapBlocks().getAtlasSprite(info.fluid.getFluid().getStill(info.fluid).toString());
			if (fluidIcon != null)
			{
				bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				int color = info.fluid.getFluid().getColor(info.fluid);
				if (lay)
				{
					drawRepeated(fluidIcon, x, y, (double) (info.fluid.amount * width) / (double) info.capacity, height, this.zLevel, color);
				}
				else
				{
					drawRepeated(fluidIcon, x, y + height - (double) (info.fluid.amount * height) / (double) info.capacity, width, (double) (info.fluid.amount * height) / (double) info.capacity, this.zLevel, color);
				}
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				bindTexture(this.location);
			}
		}
	}
	
	/**
	 * Draw repeated icon to GUI.
	 * 
	 * @param icon the rendering icon.
	 * @param x the start x position.
	 * @param y the start y position.
	 * @param width the rendering width.
	 * @param height the rendering height.
	 * @param z the z level.
	 * @param color the rendering color.
	 */
	public void drawRepeated(TextureAtlasSprite icon, double x, double y, double width, double height, double z, int color)
	{
		double iconWidthStep = (icon.getMaxU() - icon.getMinU()) / 16.0;
		double iconHeightStep = (icon.getMaxV() - icon.getMinV()) / 16.0;
		float a = (color >>> 24 & 0xFF) / 255.0F;
		float r = (color >>> 16 & 0xFF) / 255.0F;
		float g = (color >>> 8 & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.0F;
		
		GL11.glColor4f(r, g, b, a);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		for (double cy = y; cy < y + height; cy += 16D)
		{
			double quadHeight = Math.min(16D, (height + y) - cy);
			double maxY = cy + quadHeight;
			double maxV = icon.getMinV() + iconHeightStep * quadHeight;
			for (double cx = x; cx < x + width; cx += 16D)
			{
				double quadWidth = Math.min(16D, (width + x) - cx);
				double maxX = cx + quadWidth;
				double maxU = icon.getMinU() + iconWidthStep * quadWidth;
				buffer.pos(cx, maxY, z).tex(icon.getMinU(), maxV).endVertex();
				buffer.pos(maxX, maxY, z).tex(maxU, maxV).endVertex();
				buffer.pos(maxX, cy, z).tex(maxU, icon.getMinV()).endVertex();
				buffer.pos(cx, cy, z).tex(icon.getMinU(), icon.getMinV()).endVertex();
			}
		}
		tessellator.draw();
		GL11.glColor4f(1F, 1F, 1F, 1F);
	}
	
	/**
	 * Draw item stack to GUI.
	 * 
	 * @param stack the rendered stack.
	 * @param x the start x position.
	 * @param y the start y position.
	 * @param renderOverlay should renderer render the overlay to GUI.
	 * @param altText the alt text.
	 * @param zLevel the z level.
	 */
	protected void drawItemStack(ItemStack stack, int x, int y, boolean renderOverlay, String altText, float zLevel)
	{
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		float oldZ = zLevel;
		this.zLevel = this.itemRender.zLevel = zLevel;
		FontRenderer font = null;
		if (stack != null) font = stack.getItem().getFontRenderer(stack);
		if (font == null) font = this.fontRendererObj;
		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		if (renderOverlay)
		{
			ClientOverride.renderItemOverlay(this.itemRender, font, stack, x, y, altText);
		}
		this.zLevel = oldZ;
		this.itemRender.zLevel = 0.0F;
	}
	
	/**
	 * Draw a progress bar from up to down.
	 * 
	 * @param x
	 * @param y
	 * @param u
	 * @param v
	 * @param w
	 * @param h
	 * @param p the progress current.
	 * @param mp the max progress.
	 */
	protected void drawProgressScaleUTD(int x, int y, int u, int v, int w, int h, int p, int mp)
	{
		int scale = (int) ((float) p / (float) mp * h);
		if (scale <= 0) return;
		if (scale > h)
		{
			scale = h;
		}
		drawTexturedModalRect(x, y, u, v, w, scale);
	}
	
	/**
	 * Draw a progress bar from down to up.
	 * 
	 * @param x
	 * @param y
	 * @param u
	 * @param v
	 * @param w
	 * @param h
	 * @param p the progress current.
	 * @param mp the max progress.
	 */
	protected void drawProgressScaleDTU(int x, int y, int u, int v, int w, int h, int p, int mp)
	{
		int scale = (int) ((float) p / (float) mp * h);
		if (scale <= 0) return;
		if (scale > h)
		{
			scale = h;
		}
		drawTexturedModalRect(x, y + h - scale, u, v + h - scale, w, scale);
	}
	
	/**
	 * Draw a progress bar from left to right.
	 * 
	 * @param x
	 * @param y
	 * @param u
	 * @param v
	 * @param w
	 * @param h
	 * @param p the progress current.
	 * @param mp the max progress.
	 */
	protected void drawProgressScaleLTR(int x, int y, int u, int v, int w, int h, int p, int mp)
	{
		int scale = (int) ((float) p / (float) mp * w);
		if (scale <= 0) return;
		if (scale > w)
		{
			scale = w;
		}
		drawTexturedModalRect(x, y, u, v, scale, h);
	}
	
	/**
	 * Draw a progress bar from right to left.
	 * 
	 * @param x
	 * @param y
	 * @param u
	 * @param v
	 * @param w
	 * @param h
	 * @param p the progress current.
	 * @param mp the max progress.
	 */
	protected void drawProgressScaleRTL(int x, int y, int u, int v, int w, int h, int p, int mp)
	{
		int scale = (int) ((float) p / (float) mp * w);
		if (scale <= 0) return;
		if (scale > w)
		{
			scale = w;
		}
		drawTexturedModalRect(x + w - scale, y, u + w - scale, v, scale, h);
	}
	
	/**
	 * Draw tool tip only if mouse hovered on specific area.
	 * 
	 * @param mouseX the mouse x position.
	 * @param mouseY the mouse y position.
	 * @param tooltip the tool tip.
	 * @param x the start x position for area.
	 * @param y the start y position for area.
	 * @param u the width for area.
	 * @param v the height for area.
	 */
	protected void drawAreaTooltip(int mouseX, int mouseY, String tooltip, int x, int y, int u, int v)
	{
		if (mouseX >= x && mouseX <= (x + u) && mouseY >= y && mouseY <= (y + v))
		{
			drawTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, tooltip);
		}
	}
	
	/**
	 * Draw tool tip.
	 * 
	 * @param x
	 * @param y
	 * @param tooltip
	 */
	protected void drawTooltip(int x, int y, String tooltip)
	{
		drawTooltip(x, y, Arrays.asList(tooltip));
	}
	
	protected void drawTooltip(int x, int y, List<String> tooltip)
	{
		if (!tooltip.isEmpty())
		{
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			Iterator iterator = tooltip.iterator();
			
			while (iterator.hasNext())
			{
				String s = (String) iterator.next();
				int l = this.fontRendererObj.getStringWidth(s);
				
				if (l > k)
				{
					k = l;
				}
			}
			
			int j2 = x + 12;
			int k2 = y - 12;
			int i1 = 8;
			
			if (tooltip.size() > 1)
			{
				i1 += 2 + (tooltip.size() - 1) * 10;
			}
			
			if (j2 + k > this.width)
			{
				j2 -= 28 + k;
			}
			
			if (k2 + i1 + 6 > this.height)
			{
				k2 = this.height - i1 - 6;
			}
			
			this.zLevel = 300.0F;
			this.itemRender.zLevel = 300.0F;
			int j1 = -267386864;
			drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
			drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
			drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
			drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
			int k1 = 1347420415;
			int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
			drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
			drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
			drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);
			
			for (int i2 = 0; i2 < tooltip.size(); ++i2)
			{
				String s1 = tooltip.get(i2);
				this.fontRendererObj.drawStringWithShadow(s1, j2, k2, -1);
				
				if (i2 == 0)
				{
					k2 += 2;
				}
				
				k2 += 10;
			}
			
			this.zLevel = 0.0F;
			this.itemRender.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
}
