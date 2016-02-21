package flapi.plant;

public abstract class CropRegister 
{
	public abstract void registerCrop(CropCard crop);
	
	public abstract void registerCrop(CropCard crop, ICropSeed seed);
	
	public abstract CropCard getCropFromName(String name);

	public abstract ICropSeed getCropFromSeed(CropCard crop);
	
	public abstract Iterable<CropCard> getCrops();

	public abstract int getCropID(CropCard crop);
}