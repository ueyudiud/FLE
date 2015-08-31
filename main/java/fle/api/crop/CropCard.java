package fle.api.crop;

import net.minecraftforge.common.EnumPlantType;
import fle.api.enums.EnumCropRender;
import fle.api.util.DropInfo;

public abstract class CropCard
{
	public abstract String getCropName();
	
	public abstract String getCropTextureName();
	
	public abstract EnumPlantType getPlantType();
	
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
}