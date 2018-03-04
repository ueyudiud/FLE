/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.tile.delegate;

import nebula.common.block.delegated.ITileDelegateTE;
import nebula.common.tile.TEBase;

/**
 * @author ueyudiud
 */
public class TEDelegate<T extends TEDelegate<T>> extends TEBase
{
	//XXX
	protected ITileDelegateTE<?, T> delegate;
	
	public void updateServer()
	{
		this.delegate.onServerUpdate((T) this);
	}
	
	public void updateClient()
	{
		this.delegate.onClientUpdate((T) this);
	}
}
