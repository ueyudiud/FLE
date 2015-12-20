package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.gui.GuiContainerBase;
import flapi.util.FleValue;
import fle.core.init.Lang;

@SideOnly(Side.CLIENT)
public class GuiItemBagable extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE + ":textures/gui/box_4.png");
	
	public GuiItemBagable(EntityPlayer aPlayer)
	{
		super(new ContainerItemBagable(aPlayer.inventory, aPlayer.inventory.currentItem));
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return Lang.inventory_bag;
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}
}