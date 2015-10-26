package fle.api.net;

import fle.api.te.ITEInWorld;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Net event handler with Tile Entity block container this.
 * Mark tile for client sync with this interface.
 * @see {@link fle.api.te.TEBase}
 * @author ueyudiud
 *
 */
public interface INetEventHandler extends INetEventEmmiter, INetEventListener, ITEInWorld
{
	
}
