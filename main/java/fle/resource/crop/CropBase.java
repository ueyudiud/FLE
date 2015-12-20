package fle.resource.crop;

import java.util.Map;

import net.minecraft.item.ItemStack;
import flapi.enums.EnumCropRender;
import flapi.plant.CropCard;
import flapi.plant.ICropTile;
import flapi.plant.IFertilableBlock.FertitleLevel;
import flapi.recipe.DropInfo;
import flapi.util.FleValue;
import fle.FLE;
import fle.core.init.Config;

public abstract class CropBase extends CropCard
{
	private static final DropInfo info = DropInfo.empty();
	private int level;
	private int maturation;
	private int growTick = 1000;
	private String name;
	private String textureName;
	private EnumCropRender type = EnumCropRender.CROSS;
	private DropInfo seed;
	private DropInfo plant;
	
	protected CropBase(String name) 
	{
		this.name = name;
		setGrowTick();
		FLE.fle.getCropRegister().registerCrop(this);
	}
	
	public CropBase setTextureName(String aName)
	{
		this.textureName = aName;
		return this;
	}
	
	public CropBase setCropRender(EnumCropRender type)
	{
		this.type = type;
		return this;
	}
	
	public CropBase setCropLevel(int aLevel)
	{
		this.level = aLevel;
		return this;
	}

	public CropBase setMaturationStage(int aStage)
	{
		this.maturation = aStage;
		return this;
	}

	@Deprecated
	public CropBase setGrowTick(int aTick)
	{
		return this;
	}

	public CropBase setGrowTick()
	{
		this.growTick = Config.getInteger("p@" + getCropName());
		return this;
	}
	
	public CropBase setSeed(ItemStack stack)
	{
		this.seed = new DropInfo(stack);
		return this;
	}
	
	public CropBase setHaverstDrop(int size, Map<ItemStack, Integer> stacks)
	{
		this.plant = new DropInfo(size, size, stacks);
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
		return FleValue.TEXTURE_FILE + ":" + textureName;
	}
	
	@Override
	public EnumCropRender getRenderType() 
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
		FertitleLevel lv = tile.getFertitleLevel();
		double d5 = getFertitleNeed(tile.getStage(), lv);
		return (int) ((d1 + d2 + d3) * d4 * 10D) * (1D + d5) * Math.pow(0.95D, level);
	}
	
	protected double getFertitleNeed(int stage, FertitleLevel fl)
	{
		return (double) fl.P / 16D;
	}

	@Override
	public int getMaturation() 
	{
		return maturation;
	}

	@Override
	public boolean canHarvestCrop(ICropTile tile)
	{
		return tile.getStage() >= maturation - 1;
	}

	@Override
	public boolean canCropGrow(ICropTile tile) 
	{
		return tile.getStage() < getMaturation() - 1;
	}

	@Override
	public DropInfo getSeedDropsInfo(ICropTile tile)
	{
		return tile.getStage() == 0 ? seed : info;
	}

	@Override
	public DropInfo getHarvestDropsInfo(ICropTile tile) 
	{
		return plant;
	}
}