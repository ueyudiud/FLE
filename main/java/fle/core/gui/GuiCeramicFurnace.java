package fle.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.gui.GuiContainerBase;
import flapi.gui.GuiIconButton;
import flapi.gui.GuiIconButton.ButtonSize;
import flapi.util.FleValue;
import flapi.world.BlockPos;
import fle.core.init.Lang;

@SideOnly(Side.CLIENT)
public class GuiCeramicFurnace extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/ceramic_furnace.png");
	
	protected ContainerCeramicFurnace container;

	public GuiCeramicFurnace(World aWorld, int x, int y, int z, EntityPlayer aPlayer)
	{
		super(new ContainerCeramicFurnace(new BlockPos(aWorld, x, y, z), aPlayer.inventory));
		container = (ContainerCeramicFurnace) super.container;
	}
	
	@Override
	protected void drawUnderFluidAndSolid(int xO, int yO)
	{
		if(container.tileCFC != null)
		{
			drawTexturedModalRect(xO + 57, yO + 29, 176, 150, 20, 10);
			drawTexturedModalRect(xO + 119, yO + 25, 176, 68, 10, 32);
		}
	}

	@Override
	protected void drawOther(int xO, int yO, int aMouseXPosition,
			int aMouseYPosition)
	{
		if(container.tileCFC != null)
		{
			drawTexturedModalRect(xO + 120, yO + 26, 0, 166, 8, 30);
		}
		if(container.tileCFF != null)
		{
			drawTexturedModalRect(xO + 21, yO + 40, 176, 33, 73, 35);
			if(container.tileCFF.isBurning())
			{
				int progress = container.tileCFF.getBurnProgress(14);
				drawTexturedModalRect(xO + 50, yO + 41 + 14 - progress, 212, 68 + 14 - progress, 32, progress);
			}
		}
		if(container.tileCFI != null && container.tileCFC != null)
		{
			drawTexturedModalRect(xO + 38, yO + 10, 176, 0, 58, 33);
		}
		if(container.tileCFO != null)
		{
			drawTexturedModalRect(xO + 129, yO + 25, 186, 68, 26, 50);
		}
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		if(container.tileCFC != null)
		{
			buttonList.add(new GuiIconButton(1, xoffset + 119, yoffset + 15, ButtonSize.Small, GuiIconButton.buttonLocate, 80, 8));
		}
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		sendToContainer(0, guibutton.id);
		
		super.actionPerformed(guibutton);
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return Lang.inventory_ceramicFurnace;
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}
}