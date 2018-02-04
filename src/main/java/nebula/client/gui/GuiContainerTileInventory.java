/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.gui;

import java.util.Optional;

import nebula.common.gui.ContainerTileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiContainerTileInventory<T extends TileEntity & IInventory> extends GuiContainerBase
{
	private Optional<String>	tileName;
	protected EntityPlayer		opener;
	protected T					tile;
	
	public GuiContainerTileInventory(ContainerTileInventory<T> inventorySlotsIn, ResourceLocation location)
	{
		super(inventorySlotsIn, location);
		this.tile = inventorySlotsIn.tile;
		this.opener = inventorySlotsIn.getOpener();
	}
	
	public GuiContainerTileInventory(ContainerTileInventory<T> inventorySlotsIn, ResourceLocation location, int width, int height)
	{
		super(inventorySlotsIn, location, width, height);
		this.tile = inventorySlotsIn.tile;
		this.opener = inventorySlotsIn.getOpener();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (renderInventoryText())
		{
			this.fontRendererObj.drawString(this.opener.inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
		}
	}
	
	protected boolean renderInventoryText()
	{
		return true;
	}
	
	@Override
	protected String getTitleName()
	{
		if (this.tileName == null)
		{
			this.tileName = Optional.ofNullable(this.tile.getDisplayName().getUnformattedText());
		}
		return this.tileName.isPresent() ? this.tileName.get() : null;
	}
}
