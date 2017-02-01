package nebula.client.gui;

import nebula.common.gui.ContainerTileInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiContainerTileInventory<T extends TileEntity & IInventory> extends GuiContainerBase
{
	protected T tile;

	public GuiContainerTileInventory(ContainerTileInventory<T> inventorySlotsIn, ResourceLocation location)
	{
		super(inventorySlotsIn, location);
		tile = inventorySlotsIn.tile;
	}
	public GuiContainerTileInventory(ContainerTileInventory<T> inventorySlotsIn, ResourceLocation location, int width, int height)
	{
		super(inventorySlotsIn, location, width, height);
		tile = inventorySlotsIn.tile;
	}
}