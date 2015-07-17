package fla.api.crop;

import fla.api.recipe.DropInfo;

public interface CropCard
{
	public String getCropName();
	
	public String getCropTextureName();
	
	public CropRenderType getRenderType();
	
	public int getCropLevel();
	
	/**
	 * 0 for min, 16384 for max.
	 * @return
	 */
	public double getGrowSpeed(ICropTile tile);
	
	public int getGrowTick();
	
	public int getMaturation();
	
	public boolean canHarvestCrop(ICropTile tile);
	
	public boolean canCropGrow(ICropTile tile);
	
	public DropInfo getSeedDropsInfo(ICropTile tile);
	
	public DropInfo getHarvestDropsInfo(ICropTile tile);
}