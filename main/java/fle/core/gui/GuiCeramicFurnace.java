package fle.core.gui;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.collection.ArrayStandardStackList;
import flapi.collection.abs.Stack;
import flapi.gui.GuiContainerBase;
import flapi.gui.GuiIconButton;
import flapi.gui.GuiIconButton.ButtonSize;
import flapi.material.IMolecular;
import flapi.material.Matter;
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
			drawCrucible(xO, yO);
			drawTexturedModalRect(xO + 58, yO + 30, 8, 166, 18, 8);
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
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		if(container.tileCFC != null)
		{
			Map<IMolecular, Integer> map = container.tileCFC.getContainerMap();
			if(!map.isEmpty()) 
			{
				String str = "";
				for(Entry<IMolecular, Integer> entry : map.entrySet())
				{
					if(entry.getKey() == null || entry.getValue() <= 0) continue;
					str += entry.getKey().getChemName() + "x" + entry.getValue() + " ";
				}
				drawAreaTooltip(par1, par2, str, 58 + xoffset, 30 + yoffset, 18, 8);
			}
			if(container.tileCFC.getFluidStackInTank(0) != null)
			{
				drawAreaTooltip(par1, par2, container.tileCFC.getFluidStackInTank(0).getLocalizedName() + " " + FleValue.format_L.format_c(container.tileCFC.getFluidStackInTank(0).amount), xoffset + 120, yoffset + 26, 8, 30);
			}
		}
	}

	private void drawCrucible(int aXOffset, int aYOffset)
	{
		if(container.tileCFC.getContainerMap().isEmpty()) return;
		ArrayStandardStackList<IMolecular> wh = new ArrayStandardStackList(container.tileCFC.getContainerMap());
		int a0 = 0;
		for(Stack<IMolecular> stack : wh)
		{
			int startPos = a0;
			int progress = (int) (18F * (double) wh.scale(stack.get()));
			
			drawFleRect(aXOffset + 58 + startPos, aYOffset + 30, progress, 8, ((Matter) stack.get()).getColor());
			a0 = a0 + progress;
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
			buttonList.add(new GuiIconButton(0, xoffset + 119, yoffset + 62, ButtonSize.Small, GuiIconButton.buttonLocate, 96, 0));
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