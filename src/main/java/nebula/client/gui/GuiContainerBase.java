package nebula.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import nebula.Nebula;
import nebula.client.ClientOverride;
import nebula.client.util.Client;
import nebula.common.gui.ContainerBase;
import nebula.common.gui.FSlot;
import nebula.common.gui.IGUIActionListener;
import nebula.common.network.packet.PacketFluidSlotClick;
import nebula.common.network.packet.PacketGuiAction;
import nebula.common.network.packet.PacketGuiTickUpdate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiContainerBase extends GuiContainer
{
	private ResourceLocation location;
	protected Slot lastClickSlot;
	protected ContainerBase container;
	
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
	
	protected void sendGuiData(int type, long code, boolean processOnClient)
	{
		Nebula.network.sendToServer(new PacketGuiAction((byte) type, code, this.container));
		if (processOnClient && (this.inventorySlots instanceof IGUIActionListener))
		{
			((IGUIActionListener) this.inventorySlots).onRecieveGUIAction((byte) type, code);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String name = getTitleName();
		if(name != null)
		{
			this.fontRendererObj.drawString(name, (this.xSize - this.fontRendererObj.getStringWidth(name)) / 2, 6, 0x404040);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindDefaultTexture();
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		drawOtherSlots();
		bindDefaultTexture();
		drawOther(mouseX, mouseY);
	}
	
	protected void drawOtherSlots()
	{
		for(FSlot slot : ((ContainerBase) this.inventorySlots).getFluidSlots())
		{
			slot.renderSlot(this);
		}
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
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(!this.dragSplitting)
		{
			FSlot slot = getFluidSlotAtPosition(mouseX, mouseY);
			ItemStack stack = Minecraft.getMinecraft().player.inventory.getItemStack();
			if(stack == null && isTouchingMode() && this.lastClickSlot != null &&
					this.lastClickSlot.canTakeStack(Minecraft.getMinecraft().player))
			{
				stack = this.lastClickSlot.getStack();
			}
			if(slot != null && (mouseButton == 0 || mouseButton == 1))
			{
				Nebula.network.sendToServer(new PacketFluidSlotClick((ContainerBase) this.inventorySlots, slot.slotNumber));
				slot.onSlotClick(Minecraft.getMinecraft().player, stack);
			}
		}
		this.lastClickSlot = null;
		if(isTouchingMode())
		{
			for(Slot slot : this.inventorySlots.inventorySlots)
			{
				if(isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseY))
				{
					this.lastClickSlot = slot;
				}
			}
		}
	}
	
	protected FSlot getFluidSlotAtPosition(int x, int y)
	{
		for(FSlot slot : ((ContainerBase) this.inventorySlots).getFluidSlots())
		{
			if(slot.isVisible() && isPointInRegion(slot.x, slot.y, slot.u, slot.v, x, y))
				return slot;
		}
		return null;
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		Nebula.network.sendToServer(new PacketGuiTickUpdate((ContainerBase) this.inventorySlots));
	}
	
	protected void bindDefaultTexture()
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
	
	public void drawFluid(int x, int y, FluidTankInfo tank, int width, int height)
	{
		drawFluid(x, y, tank, width, height, false);
	}
	
	public void drawFluid(int x, int y, FluidTankInfo info, int width, int height, boolean lay)
	{
		if(info.fluid == null) return;
		if (info.fluid.amount > 0)
		{
			TextureAtlasSprite fluidIcon =
					this.mc.getTextureMapBlocks().getAtlasSprite(info.fluid.getFluid().getStill(info.fluid).toString());
			if (fluidIcon != null)
			{
				bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				int color = info.fluid.getFluid().getColor(info.fluid);
				if(lay)
				{
					drawRepeated(fluidIcon, this.guiLeft + x, this.guiTop + y, (double) (info.fluid.amount * width) / (double)info.capacity, height, this.zLevel, color);
				}
				else
				{
					drawRepeated(fluidIcon, this.guiLeft + x, this.guiTop + y + height - (double) (info.fluid.amount * height) / (double) info.capacity, width, (double) (info.fluid.amount * height) / (double) info.capacity, this.zLevel, color);
				}
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.mc.renderEngine.bindTexture(this.location);
			}
		}
	}
	
	protected void drawRepeated(TextureAtlasSprite icon, double x, double y, double width, double height, double z, int color)
	{
		double iconWidthStep = ((double)icon.getMaxU() - (double)icon.getMinU()) / 16D;
		double iconHeightStep = ((double)icon.getMaxV() - (double)icon.getMinV()) / 16D;
		int a = color >>> 24 & 0xFF;
		int r = color >>> 16 & 0xFF;
		int g = color >>> 8 & 0xFF;
		int b = color & 0xFF;
		
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
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
				buffer.pos(cx,   maxY, z).tex(icon.getMinU(), maxV          ).color(r, g, b, a);
				buffer.pos(maxX, maxY, z).tex(maxU,           maxV          ).color(r, g, b, a);
				buffer.pos(maxX, cy,   z).tex(maxU,           icon.getMinV()).color(r, g, b, a);
				buffer.pos(cx,   cy,   z).tex(icon.getMinU(), icon.getMinV()).color(r, g, b, a);
			}
		}
		tessellator.draw();
	}
	
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
			ClientOverride.renderCustomItemOverlayIntoGUI(this.itemRender, font, stack, x, y, altText);
		}
		this.zLevel = oldZ;
		this.itemRender.zLevel = 0.0F;
	}
}