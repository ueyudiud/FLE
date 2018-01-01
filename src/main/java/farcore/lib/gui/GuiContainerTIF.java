/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.gui;

import farcore.lib.container.ContainerTIF;
import farcore.lib.solid.SolidSlot;
import nebula.client.gui.GuiContainerTileInventory;
import nebula.common.gui.ContainerBase;
import nebula.common.gui.FluidSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public class GuiContainerTIF<T extends TileEntity & IInventory> extends GuiContainerTileInventory<T>
{
	public GuiContainerTIF(ContainerTIF<T> inventorySlotsIn, ResourceLocation location)
	{
		super(inventorySlotsIn, location);
	}
	
	public GuiContainerTIF(ContainerTIF<T> inventorySlotsIn, ResourceLocation location, int width, int height)
	{
		super(inventorySlotsIn, location, width, height);
	}
	
	@Override
	protected void drawOtherSlots()
	{
		this.zLevel = 200F;
		for (FluidSlot slot : ((ContainerBase) this.inventorySlots).getFluidSlots())
		{
			slot.renderSlot(this);
		}
		for (SolidSlot slot : ((ContainerTIF<?>) this.inventorySlots).getSolidSlots())
		{
			slot.renderSlot(this);
		}
		this.zLevel = 0F;
	}
}