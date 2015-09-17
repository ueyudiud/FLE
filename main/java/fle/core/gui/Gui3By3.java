package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleValue;
import fle.api.gui.GuiContainerBase;

@SideOnly(Side.CLIENT)
public class Gui3By3 extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/box_9.png");
	
	protected IInventory inv;
	
	public Gui3By3(EntityPlayer player, IInventory inv)
	{
		super(new Container3By3(player.inventory, inv));
		this.inv = inv;
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		
	}

	@Override
	public boolean hasCustomName()
	{
		return inv.hasCustomInventoryName();
	}

	@Override
	public String getName()
	{
		return inv.getInventoryName();
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}
}