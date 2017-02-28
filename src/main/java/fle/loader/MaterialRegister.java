/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader;

import farcore.lib.material.IMaterialRegister;

/**
 * @author ueyudiud
 */
public enum MaterialRegister implements IMaterialRegister
{
	INSTANCE;
	
	@Override
	public void registerMaterials()
	{
		Crops.init();
		Trees.init();
	}
}