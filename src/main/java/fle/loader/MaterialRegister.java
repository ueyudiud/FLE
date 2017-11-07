/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader;

/**
 * @author ueyudiud
 */
public enum MaterialRegister implements Runnable
{
	INSTANCE;
	
	@Override
	public void run()
	{
		Crops.init();
		Trees.init();
	}
}
