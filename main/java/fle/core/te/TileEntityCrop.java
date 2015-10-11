package fle.core.te;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.crop.CropCard;
import fle.api.crop.ICropTile;
import fle.api.crop.IFertilableBlock;
import fle.api.crop.IFertilableBlock.FertitleLevel;
import fle.api.te.TEBase;
import fle.core.net.FlePackets.CoderCropUpdate;
import fle.core.util.WorldUtil;

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
		this.cushion = c;
	}
	
	public boolean isWild = false;
	private int cushion;
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

	@Override
	public void updateEntity() 
	{
		super.updateEntity();
		if(card != null)
		{
			if(buffer < 0) buffer = 0D;
			if(age >= 256) return;
			++cushion;
			if(cushion > 20)
			{
 				cushion = 0;
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
				sendToNearBy(new CoderCropUpdate(this), 256.0F);
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
		return WorldUtil.getTempretureLevel(worldObj, xCoord, yCoord, zCoord);
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
		cushion = c;
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