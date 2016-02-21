package flapi.net;

import flapi.te.interfaces.IObjectInWorld;

/**
 * Net event handler with Tile Entity block container this.
 * Mark tile for client sync with this interface.
 * @see {@link fle.farcore.block.TEBase}
 * @author ueyudiud
 *
 */
public interface INetEventHandler extends INetEventEmmiter, INetEventListener, IObjectInWorld
{
	
}
