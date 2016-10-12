package farcore.lib.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import farcore.FarCore;
import farcore.lib.net.gui.PacketFluidSlotClick;
import farcore.lib.net.gui.PacketGuiTickUpdate;
import farcore.util.U;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
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
	
	public GuiContainerBase(ContainerBase inventorySlotsIn, ResourceLocation location)
	{
		this(inventorySlotsIn, location, 176, 166);
	}
	public GuiContainerBase(ContainerBase inventorySlotsIn, ResourceLocation location, int width, int height)
	{
		super(inventorySlotsIn);
		fontRendererObj = U.Client.getFontRender();
		xSize = width;
		ySize = height;
		this.location = location;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String name = getTitleName();
		if(name != null)
		{
			fontRendererObj.drawString(name, (xSize - fontRendererObj.getStringWidth(name)) / 2, 6, 0x404040);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindDefaultTexture();
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawOtherSlots();
		bindDefaultTexture();
		drawOther(mouseX, mouseY);
	}
	
	protected void drawOtherSlots()
	{
		for(FSlot slot : ((ContainerBase) inventorySlots).fluidSlots)
		{
			slot.renderSlot(this);
		}
	}

	private void drawOther(int mouseX, int mouseY)
	{
		
	}
	
	protected String getTitleName()
	{
		return null;
	}

	protected boolean isTouchingMode()
	{
		return mc.gameSettings.touchscreen;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(!dragSplitting)
		{
			FSlot slot = getFluidSlotAtPosition(mouseX, mouseY);
			ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getItemStack();
			if(stack == null && isTouchingMode() && lastClickSlot != null &&
					lastClickSlot.canTakeStack(Minecraft.getMinecraft().thePlayer))
			{
				stack = lastClickSlot.getStack();
			}
			if(slot != null && (mouseButton == 0 || mouseButton == 1))
			{
				FarCore.network.sendToServer(new PacketFluidSlotClick((ContainerBase) inventorySlots, slot.slotNumber));
				slot.onSlotClick(Minecraft.getMinecraft().thePlayer, stack);
			}
		}
		lastClickSlot = null;
		if(isTouchingMode())
		{
			for(Slot slot : inventorySlots.inventorySlots)
			{
				if(isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY))
				{
					lastClickSlot = slot;
				}
			}
		}
	}
	
	protected FSlot getFluidSlotAtPosition(int x, int y)
	{
		for(FSlot slot : ((ContainerBase) inventorySlots).fluidSlots)
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
		FarCore.network.sendToServer(new PacketGuiTickUpdate((ContainerBase) inventorySlots));
	}

	protected void bindDefaultTexture()
	{
		bindTexture(location);
	}

	public TextureAtlasSprite getTexture(IBlockState state)
	{
		return mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
	}
	
	public TextureAtlasSprite getTexutre(ResourceLocation location)
	{
		return mc.getTextureMapBlocks().getAtlasSprite(location.toString());
	}

	public void bindTexture(ResourceLocation location)
	{
		mc.renderEngine.bindTexture(location);
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
					mc.getTextureMapBlocks().getAtlasSprite(info.fluid.getFluid().getStill(info.fluid).toString());
			if (fluidIcon != null)
			{
				bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				int color = info.fluid.getFluid().getColor(info.fluid);
				if(lay)
				{
					drawRepeated(fluidIcon, guiLeft + x, guiTop + y, (double) (info.fluid.amount * width) / (double)info.capacity, height, zLevel, color);
				}
				else
				{
					drawRepeated(fluidIcon, guiLeft + x, guiTop + y + height - (double) (info.fluid.amount * height) / (double) info.capacity, width, (double) (info.fluid.amount * height) / (double) info.capacity, zLevel, color);
				}
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				mc.renderEngine.bindTexture(location);
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
}