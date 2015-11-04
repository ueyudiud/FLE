package fle.core.block.plant;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumPlantType;
import fle.FLE;
import fle.api.plant.PlantCard;
import fle.api.util.DropInfo;
import fle.core.block.crop.CropBase;

public abstract class PlantBase extends PlantCard
{
	private String name;
	private String textureName;
	
	protected PlantBase(String name) 
	{
		this.name = name;
		FLE.fle.getPlantRegister().registerPlant(this);
	}
	
	@Override
	public final String getPlantName()
	{
		return name;
	}
	
	public PlantBase setTextureName(String aName)
	{
		this.textureName = aName;
		return this;
	}
	
	@Override
	public String getPlantTextureName() 
	{
		return textureName;
	}
	
	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Plains;
	}
	
	protected void setUseBiomeColor()
	{
		flag = true;
	}
	
	boolean flag = false;
	
	@Override
	public boolean shouldUseBiomeColor()
	{
		return flag;
	}
	
	protected DropInfo plant;

	public PlantBase setHaverstDrop(int size, Map<ItemStack, Integer> stacks)
	{
		plant = new DropInfo(size, size, stacks);
		return this;
	}
	public PlantBase setHaverstDrop(ItemStack stacks)
	{
		plant = new DropInfo(stacks);
		return this;
	}
	
	@Override
	public DropInfo getDropInfo()
	{
		return plant;
	}
}