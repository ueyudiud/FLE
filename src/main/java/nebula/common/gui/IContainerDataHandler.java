/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.gui;

import net.minecraft.inventory.IContainerListener;

/**
 * @author ueyudiud
 */
public interface IContainerDataHandler
{
	void addListener(IContainerListener listener);
	
	void detectAndSendChanges();
	
	default <T> void updateValue(Class<T> type, int id, T value)
	{
		
	}
}