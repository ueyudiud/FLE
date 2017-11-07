/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.tile;

import fle.api.item.IIDKeyItem;
import nebula.common.world.ICoord;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public interface IIDOpenableTile extends ICoord
{
	static long EMPTY_UUID = -1;
	
	long getOpenUUID();
	
	boolean canOpenTileEntity(ItemStack stack, IIDKeyItem item);
}
