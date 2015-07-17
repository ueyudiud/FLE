package fla.core.tileentity;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import fla.api.crop.CropCard;
import fla.api.crop.CropRegistry;
import fla.api.crop.ICropTile;
import fla.api.recipe.IItemChecker.OreChecker;
import fla.core.Fla;
import fla.core.world.FWM;

public class TileEntityCrops extends TileEntityBase implements ICropTile
{
	public TileEntityCrops copy()
	{
		return new TileEntityCrops(card, age, buffer, cushion);
	}
	
	public TileEntityCrops(){}
	
	private TileEntityCrops(CropCard card, int a, double b, int c) 
	{
		this.card = card;
		this.age = a;
		this.buffer = b;
		this.cushion = c;
	}
	
	private int cushion;
	private double buffer;
	private CropCard card;
	private int age;
	
	public void setupCrop(CropCard card)
	{
		this.card = card;
		this.age = 0;
		this.buffer = 0;
		this.cushion = 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		age = nbt.getShort("Age");
		card = CropRegistry.getCropFromName(nbt.getString("CropName"));
		buffer = nbt.getDouble("Buffer");
	}
	
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		nbt.setShort("Age", (short) age);
		nbt.setDouble("Buffer", buffer);
		nbt.setString("CropName", card.getCropName());
	}

	@Override
	public void updateEntity() 
	{
		if(card != null)
		{
			if(age >= 256) return;
			++cushion;
			if(cushion > 20)
			{
 				cushion = 0;
				if(card.canCropGrow(this))
				{
					double d = MathHelper.randomFloatClamp(worldObj.rand, 0.8F, 1.2F) * Math.log10(card.getGrowSpeed(this));
					buffer += d;
					if(buffer > card.getGrowTick())
					{
						++age;
						buffer = 0;
					}
					else
					{
						++buffer;
					}
				}
				Fla.fla.nwm.get().updateTileNBT(this);
			}
		}
	}

	public ArrayList<ItemStack> getCropHarvestDrop() 
	{
		ArrayList<ItemStack> list = new ArrayList();
		if(card != null)
		{
			list.addAll(card.getSeedDropsInfo(this).getDustDrop());
			if(card.canHarvestCrop(this))
			{
				list.addAll(card.getHarvestDropsInfo(this).getDustDrop());
			}
		}
		return list;
	}
	
	@Override
	public int getStage()
	{
		return age;
	}

	@Override
	public boolean isBlockUnder(Block target) 
	{
		return worldObj.getBlock(xCoord, yCoord - 1, zCoord).isReplaceableOreGen(worldObj, xCoord, yCoord - 1, zCoord, target);
	}

	@Override
	public double getAirLevel() 
	{
		return FWM.getAirLevel(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public double getWaterLevel() 
	{
		return FWM.getWaterLevel(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public double getTempretureLevel() 
	{
		return FWM.getTempretureLevel(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public int getLightValue() 
	{
		return worldObj.getBlockLightValue(xCoord, yCoord + 1, zCoord);
	}

	public CropCard getCrop() 
	{
		return card;
	}

	public int getCropAge() 
	{
		return age;
	}
}