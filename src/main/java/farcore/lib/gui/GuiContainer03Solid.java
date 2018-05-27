/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.gui;

import farcore.lib.container.Container04Solid;
import farcore.lib.solid.SolidSlot;
import nebula.client.gui.GuiContainer02TE;
import nebula.common.tile.IGuiTile;
import nebula.common.tile.TE00Base;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiContainer03Solid<T extends TE00Base & IGuiTile> extends GuiContainer02TE<T>
{
	public GuiContainer03Solid(Container04Solid<T> container)
	{
		super(container);
	}
	
	@Override
	protected void drawSlots()
	{
		super.drawSlots();
		for (SolidSlot slot : ((Container04Solid<?>) this.inventorySlots).solidSlots)
		{
			slot.renderSlot(this, this.guiLeft, this.guiTop);
		}
	}
}
