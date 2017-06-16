/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.client.gui;

import fle.core.common.gui.container.Containernxn;
import fle.core.common.gui.container.EnumSlotsSize;
import nebula.client.gui.GuiContainerTileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiInvContainer<T extends TileEntity & IInventory> extends GuiContainerTileInventory<T>
{
	public GuiInvContainer(T tile, EntityPlayer player, EnumSlotsSize size)
	{
		this(new Containernxn<>(tile, player, size), size.location);
	}
	
	public GuiInvContainer(Containernxn<T> container, ResourceLocation location)
	{
		super(container, location);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.fontRendererObj.drawString(this.container.getOpener().inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
	}
}