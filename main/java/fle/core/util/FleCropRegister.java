package fle.core.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fle.api.crop.CropCard;
import fle.api.crop.CropRegister;
import fle.api.crop.ICropSeed;
import fle.api.util.Register;

public class FleCropRegister extends CropRegister
{
	private Register<CropCard> cropRegister = new Register();
	private Map<String, ICropSeed> seedMap = new HashMap();
	
	public FleCropRegister() 
	{
		
	}
	
	@Override
	public void registerCrop(CropCard crop) 
	{
		if(crop == null) throw new NullPointerException();
		cropRegister.register(crop, crop.getCropName());
	}

	@Override
	public void registerCrop(CropCard crop, ICropSeed seed) 
	{
		registerCrop(crop);
		if(seed == null) throw new NullPointerException();
		seedMap.put(crop.getCropName(), seed);
	}

	@Override
	public CropCard getCropFromName(String name) 
	{
		return name == null || name == "" ? null : cropRegister.get(name);
	}

	@Override
	public ICropSeed getCropFromSeed(CropCard crop) 
	{
		return crop == null ? null : seedMap.get(crop.getCropName());
	}

	@Override
	public Iterator<CropCard> getCrops() 
	{
		return cropRegister.iterator();
	}

	@Override
	public int getCropID(CropCard crop)
	{
		return cropRegister.serial(crop);
	}
}