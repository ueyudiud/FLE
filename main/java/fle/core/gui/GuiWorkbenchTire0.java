package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import fle.api.FleValue;
import fle.api.gui.GuiContainerBase;
import fle.core.inventory.InventoryWorkbenchTire0;

public class GuiWorkbenchTire0 extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/crafting_base.png");

	public GuiWorkbenchTire0(World aWorld, int x, int y, int z, EntityPlayer aPlayer)
	{
		super(new ContainerWorkbenchTire0(aWorld, x, y, z, aPlayer.inventory));
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		InventoryWorkbenchTire0 inv = (InventoryWorkbenchTire0) ((ContainerWorkbenchTire0) container).inv;
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			if(inv.isSlotAccess(i))
				drawTexturedModalRect(aXOffset + 29 + 18 * (i % 3), aYOffset + 16 + 18 * (i / 3), 176, 0, 18, 18);
		}
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return ((ContainerWorkbenchTire0) container).inv.getInventoryName();
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}
}