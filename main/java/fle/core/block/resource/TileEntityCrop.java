package fle.core.block.resource;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.TEBase;
import flapi.plant.CropCard;
import flapi.plant.ICropTile;
import flapi.plant.IFertilableBlock;
import flapi.plant.IFertilableBlock.FertitleLevel;
import fle.FLE;
import fle.core.net.FleCropUpdatePacket;
import fle.core.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityCrop extends TEBase implements ICropTile
{
	public TileEntityCrop copy()
	{
		return new TileEntityCrop(card, age, buffer, cushion);
	}
	
	public TileEntityCrop(){}
	
	private TileEntityCrop(CropCard card, int a, double b, int c) 
	{
		this.card = card;
		this.age = a;
		this.buffer = b;
		this.cushion = (short) c;
	}
	
	public boolean isWild = false;
	private short cushion = 16;
	private double buffer;
	private CropCard card;
	private int age;
	
	public void setupCrop(CropCard card)
	{
		this.card = card;
		this.age = 0;
		this.buffer = 0D;
		this.cushion = 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		age = nbt.getShort("Age");
		card = FLE.fle.getCropRegister().getCropFromName(nbt.getString("CropName"));
		buffer = nbt.getDouble("Buffer");
		isWild = nbt.getBoolean("Wild");
	}
	
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		nbt.setShort("Age", (short) age);
		nbt.setDouble("Buffer", buffer);
		if(card != null)
			nbt.setString("CropName", card.getCropName());
		nbt.setBoolean("Wild", isWild);
	}
	
	double tempCache = -1;
	
	@Override
	public void updateEntity() 
	{
		if(card != null)
		{
			if(buffer < 0) buffer = 0D;
			if(age >= 256) return;
			if(cushion++ % 20 == 0)
			{
				if(cushion > 100)
				{
					cushion = 0;
					tempCache = WorldUtil.getTempretureLevel(worldObj, xCoord, yCoord, zCoord);
				}
				if(card.canCropGrow(this))
				{
					double d = MathHelper.randomFloatClamp(worldObj.rand, 0.8F, 1.2F) * Math.log10(card.getGrowSpeed(this));
					if(d > 0)
						buffer += d;
					if(buffer > card.getGrowTick())
					{
						++age;
						buffer = 0;
					}
				}
				sendToNearBy(new FleCropUpdatePacket(this), 256.0F);
				markRenderForUpdate();
			}
		}
	}

	public ArrayList<ItemStack> getCropHarvestDrop() 
	{
		ArrayList<ItemStack> list = new ArrayList();
		if(card != null)
		{
			list.addAll(card.getSeedDropsInfo(this).getDrops());
			if(card.canHarvestCrop(this))
			{
				list.addAll(card.getHarvestDropsInfo(this).getDrops());
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
		return 10D - Math.log(FLE.fle.getAirConditionProvider().getPolluteLevel(getBlockPos()) + 1D);
	}

	@Override
	public double getWaterLevel()
	{
		int base = 500;
		if(worldObj.getBlock(xCoord, yCoord - 1, zCoord) instanceof IFertilableBlock)
			base = ((IFertilableBlock) worldObj.getBlock(xCoord, yCoord - 1, zCoord)).getWaterLevel(worldObj, xCoord, yCoord - 1, zCoord);
		return base;
	}

	@Override
	public double getTempretureLevel() 
	{
		return tempCache;
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

	public double getCropBuf() 
	{
		return buffer;
	}

	public int getCropCushion() 
	{
		return cushion;
	}

	@SideOnly(Side.CLIENT)
	public void setCropAge(int a) 
	{
		age = a;
	}

	@SideOnly(Side.CLIENT)
	public void setCropBuf(double b) 
	{
		buffer = b;
	}

	@SideOnly(Side.CLIENT)
	public void setCropCushion(int c) 
	{
		cushion = (short) c;
	}

	@Override
	public FertitleLevel getFertitleLevel()
	{
		Block block = getBlockPos().toPos(ForgeDirection.DOWN).getBlock();
		if(block instanceof IFertilableBlock)
			return ((IFertilableBlock) block).getFertileLevel(worldObj, xCoord, yCoord - 1, zCoord);
		return new FertitleLevel(0, 0, 0, 0);
	}
}