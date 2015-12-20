package fle.resource;

import java.util.HashMap;
import java.util.Map;

import flapi.collection.Register;
import flapi.plant.CropCard;
import flapi.plant.CropRegister;
import flapi.plant.ICropSeed;

public class FleCropRegister extends CropRegister
{
	private final Register<CropCard> cropRegister = new Register();
	private final Map<String, ICropSeed> seedMap = new HashMap();
	
	public FleCropRegister() 
	{
		
	}
	
	@Override
	public void registerCrop(CropCard crop) 
	{
		if(crop == null) throw new NullPointerException();
		if(cropRegister.contain(crop.getCropName())) throw new RuntimeException("FLE : Can not register same crop twice.");
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
	public Iterable<CropCard> getCrops() 
	{
		return cropRegister;
	}

	@Override
	public int getCropID(CropCard crop)
	{
		return cropRegister.serial(crop);
	}
}