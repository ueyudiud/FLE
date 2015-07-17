package fla.api.crop;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import fla.api.recipe.DropInfo;

public class CropExample implements CropCard
{
	private int level;
	private int maturation;
	private String name;
	private DropInfo seed;
	private DropInfo harvest;
	
	public CropExample(String name, int level, int maturation, ItemStack seed, DropInfo harvestInfo) 
	{
		this.name = name;
		this.level = level;
		this.maturation = maturation;
		this.seed = new DropInfo(seed);
		this.harvest = harvestInfo;
	}

	@Override
	public String getCropName() 
	{
		return name;
	}
	
	@Override
	public int getCropLevel()
	{
		return level;
	}

	@Override
	public double getGrowSpeed(ICropTile tile) 
	{
		double airLevel = tile.getAirLevel();
		double waterLevel = tile.getWaterLevel();
		double tempretureLevel = tile.getTempretureLevel();
		double d1 = (airLevel + waterLevel + tempretureLevel) * (1D - 0.05D * getCropLevel());
		double d2 = Math.pow(waterLevel - tempretureLevel, 2D);
		double d3 = Math.pow(airLevel - tempretureLevel, 2D);
		double d4 = (double) tile.getLightValue() / 15D;
		return ((d1 + d3 - d2) * d4);
	}

	@Override
	public int getGrowTick() 
	{
		return 1000;
	}

	@Override
	public int getMaturation() 
	{
		return maturation;
	}

	@Override
	public boolean canHarvestCrop(ICropTile tile) 
	{
		return tile.getStage() >= maturation;
	}

	@Override
	public boolean canCropGrow(ICropTile tile) 
	{
		return tile.getStage() < maturation;
	}

	@Override
	public DropInfo getSeedDropsInfo(ICropTile tile) 
	{
		return seed;
	}

	@Override
	public DropInfo getHarvestDropsInfo(ICropTile tile) 
	{
		return harvest;
	}

	
	public String textureName;
	
	public CropExample setTextureName(String name)
	{
		textureName = name;
		return this;
	}
	
	@Override
	public String getCropTextureName() 
	{
		return textureName == null ? "minecraft:MISSING_ICON_CROP_" + getCropName() : textureName;
	}

	@Override
	public CropRenderType getRenderType() 
	{
		return CropRenderType.CROSS;
	}
}