package fla.api.crop;

import java.util.Iterator;

import fla.api.util.Registry;

public class CropRegistry 
{
	private static Registry<CropCard> registry = new Registry();
	
	public static void registerCrop(CropCard crop)
	{
		registry.register(crop, crop.getCropName());
	}
	
	public static CropCard getCropFromName(String name) 
	{
		return registry.get(name);
	}
	
	public static Iterator<CropCard> getCrops()
	{
		return registry.iterator();
	}
	
	private CropRegistry() {}

	public static int getCropID(CropCard crop)
	{
		return registry.serial(crop);
	}
}
