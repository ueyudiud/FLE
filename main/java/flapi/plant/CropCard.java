package flapi.plant;

import flapi.enums.EnumCropRender;
import flapi.recipe.DropInfo;

/**
 * 
 * @author ueyudiud
 *
 */
public abstract class CropCard extends PlantCard
{
	@Override
	public final String getPlantName()
	{
		return getCropName();
	}
	
	@Override
	public final String getPlantTextureName()
	{
		return "";
	}
	
	public abstract String getCropName();
	
	public abstract String getCropTextureName();
	
	public abstract EnumCropRender getRenderType();
	
	public abstract int getCropLevel();
	
	/**
	 * 0 for min, 16384 for max.
	 * @return
	 */
	public abstract double getGrowSpeed(ICropTile tile);
	
	public abstract int getGrowTick();
	
	public abstract int getMaturation();
	
	public abstract boolean canHarvestCrop(ICropTile tile);
	
	public abstract boolean canCropGrow(ICropTile tile);
	
	public abstract DropInfo getSeedDropsInfo(ICropTile tile);
	
	public abstract DropInfo getHarvestDropsInfo(ICropTile tile);
	
	@Override
	public boolean shouldUseBiomeColor()
	{
		return false;
	}
	
	/**
	 * Do not drop with resource plant.
	 */
	@Override
	public final DropInfo getDropInfo()
	{
		return null;
	}
}