/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.client.render;

import fle.core.tile.wooden.TEDryingTable;
import nebula.client.render.TESRBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRDryingTable extends TESRBase<TEDryingTable>
{
	@Override
	public void renderTileEntityAt(TEDryingTable te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		if (te != null && te.getStack() != null)
		{
			ItemStack stack = te.getStack().copy();
			stack.stackSize = 1;
			renderItem(stack, x + .5, y + 0.25, z + .5);
		}
	}
}
