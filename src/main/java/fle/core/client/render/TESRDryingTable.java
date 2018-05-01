/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.client.render;

import fle.core.tile.wooden.TEDryingTable;
import nebula.client.render.TESRBase;
import nebula.common.stack.IS;
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
		if (te != null && te.getStackInSlot(0) != null)
		{
			renderItem(IS.copy(te.getStackInSlot(0), 1), x + .5, y + 0.25, z + .5);
		}
	}
}
