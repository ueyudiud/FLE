package fle.core.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.FleAPI;
import flapi.gui.GuiContainerBase;
import flapi.util.FleValue;

@SideOnly(Side.CLIENT)
public class GuiPlayerCrafting extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/crafting_player.png");
	
	public GuiPlayerCrafting(EntityPlayer player)
	{
		super(new ContainerPlayerCrafting(player.inventory));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 0x404040);
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}
	
	@Override
	public String getName()
	{
		return FleAPI.langManager.translateToLocal("container.crafting", new Object[0]);
	}
}