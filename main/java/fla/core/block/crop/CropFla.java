package fla.core.block.crop;

import java.util.Map;

import fla.api.crop.CropRenderType;

import net.minecraft.item.ItemStack;
import fla.api.crop.CropCard;
import fla.api.crop.CropRegistry;
import fla.api.crop.CropRenderType;
import fla.api.crop.ICropTile;
import fla.api.recipe.DropInfo;
import fla.api.util.FlaValue;

public abstract class CropFla implements CropCard
{
	private int level;
	private int maturation;
	private int growTick = 1000;
	private String name;
	private String textureName;
	private CropRenderType type = CropRenderType.CROSS;
	private DropInfo seed;
	private DropInfo plant;
	
	protected CropFla(String name) 
	{
		this.name = name;
		CropRegistry.registerCrop(this);
	}
	
	public CropFla setTextureName(String aName)
	{
		this.textureName = aName;
		return this;
	}
	
	public CropFla setCropRender(CropRenderType type)
	{
		this.type = type;
		return this;
	}
	
	public CropFla setCropLevel(int aLevel)
	{
		this.level = aLevel;
		return this;
	}

	public CropFla setMaturationStage(int aStage)
	{
		this.maturation = aStage;
		return this;
	}

	public CropFla setGrowTick(int aTick)
	{
		this.growTick = aTick;
		return this;
	}
	
	public CropFla setSeed(ItemStack stack)
	{
		this.seed = new DropInfo(stack);
		return this;
	}
	
	public CropFla setHaverstDrop(int size, Map<ItemStack, Integer> stacks)
	{
		this.plant = new DropInfo(size, null, stacks);
		return this;
	}

	@Override
	public int getGrowTick() 
	{
		return growTick;
	}
	
	@Override
	public final String getCropName() 
	{
		return name;
	}

	@Override
	public String getCropTextureName() 
	{
		return FlaValue.TEXT_FILE_NAME + ":" + textureName;
	}
	
	@Override
	public CropRenderType getRenderType() 
	{
		return type;
	}

	@Override
	public int getCropLevel() 
	{
		return level;
	}

	@Override
	public double getGrowSpeed(ICropTile tile) 
	{
		double d1 = tile.getAirLevel();
		double d2 = tile.getTempretureLevel();
		double d3 = tile.getWaterLevel();
		double d4 = (double) tile.getLightValue() / 7D;
		return (int) ((d1 + d2 * 100D + d3) * d4 * 10D);
	}

	@Override
	public int getMaturation() 
	{
		return maturation;
	}

	@Override
	public boolean canHarvestCrop(ICropTile tile)
	{
		return tile.getStage() <= maturation - 1;
	}

	@Override
	public boolean canCropGrow(ICropTile tile) 
	{
		return tile.getStage() < getMaturation() - 1;
	}

	@Override
	public DropInfo getSeedDropsInfo(ICropTile tile)
	{
		return seed;
	}

	@Override
	public DropInfo getHarvestDropsInfo(ICropTile tile) 
	{
		return plant;
	}
}