/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.crop;

import farcore.lib.material.Mat;

class CropVoid extends Crop
{
	CropVoid()
	{
		super(Mat.VOID, "Void", 1, -1, 0, 0, 0);
	}
	
	@Override
	public CropOrder getOrder()
	{
		return CropOrder.ORDER;
	}
	
	@Override
	public String getRegisteredName()
	{
		return Mat.VOID.name;
	}
	
	@Override
	public long tickUpdate(ICropAccess access)
	{
		return Integer.MAX_VALUE;
	}
}
