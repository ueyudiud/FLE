package farcore.lib.crop;

import java.util.List;
import java.util.Random;

import farcore.block.BlockStandardFluid;
import farcore.enums.EnumBlock;
import farcore.interfaces.IPlantedableBlock;
import farcore.interfaces.tile.IDebugableTile;
import farcore.lib.tile.TileEntityAgeUpdatable;
import farcore.lib.world.biome.BiomeBase;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.TempCategory;
import net.minecraftforge.fluids.BlockFluidBase;

public class TileEntityCrop extends TileEntityAgeUpdatable
implements ICropAccess, IDebugableTile
{
	private static final float absorbEffiency = 0.2F;
	private int waterLevel = 6400;
	private boolean isWild = false;
	private int growBuffer;
	private int stage;
	private CropCard card = CropCard.VOID;
	private CropInfo info;
	
	public TileEntityCrop()
	{
		
	}
	
	public void initCrop(CropCard crop)
	{
		initCrop(0, crop.instanceDNA(rand), crop);
		isWild = true;
	}
	public void initCrop(int generations, String dna, CropCard crop)
	{
		this.card = crop;
		this.info = new CropInfo();
		this.info.DNA = dna;
		this.info.generations = generations;
		crop.decodeDNA(this, dna);
	}
	
	@Override
	protected boolean init()
	{
		if(card != null && info != null)
		{
			return super.init();
		}
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("water", waterLevel);
		nbt.setBoolean("isWild", isWild);
		nbt.setInteger("growBuf", growBuffer);
		nbt.setInteger("stage", stage);
		nbt.setString("crop", card.name());
		if(info != null)
		{
			info.writeToNBT(nbt);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		waterLevel = nbt.getInteger("water");
		isWild = nbt.getBoolean("isWild");
		growBuffer = nbt.getInteger("growBuf");
		stage = nbt.getInteger("stage");
		card = CropManager.getCrop(nbt.getString("crop"));
		info = new CropInfo();
		info.readFromNBT(nbt);
	}
	
	@Override
	protected void writeDescriptionsToNBT1(NBTTagCompound nbt)
	{
		super.writeDescriptionsToNBT1(nbt);
		nbt.setInteger("w", waterLevel);
		nbt.setBoolean("wi", isWild);
		nbt.setInteger("g", growBuffer);
		nbt.setInteger("s", stage);
		nbt.setString("c", card.name());
		info.writeToNBT(nbt);
	}
	
	@Override
	protected void readDescriptionsFromNBT1(NBTTagCompound nbt)
	{
		super.readDescriptionsFromNBT1(nbt);
		waterLevel = nbt.getInteger("w");
		isWild = nbt.getBoolean("wi");
		growBuffer = nbt.getInteger("g");
		stage = nbt.getInteger("s");
		card = CropManager.getCrop(nbt.getString("c"));
		info = new CropInfo();
		info.readFromNBT(nbt);
	}
	
	@Override
	protected long tickTime()
	{
		return card.tickUpdate(this);
	}
	
	@Override
	protected void updateServer2()
	{
		card.onUpdate(this);
		super.updateServer2();
	}
	
	@Override
	public String getDNA()
	{
		return info.DNA;
	}

	@Override
	public CropCard getCrop()
	{
		return card;
	}

	@Override
	public CropInfo getInfo()
	{
		return info;
	}

	@Override
	public World getWorld()
	{
		return worldObj;
	}

	@Override
	public BiomeBase getBiome()
	{
		return (BiomeBase) worldObj.getBiomeGenForCoords(xCoord, zCoord);
	}

	@Override
	public boolean isWild()
	{
		return isWild;
	}
	
	@Override
	public Random rng()
	{
		return rand;
	}
	
	@Override
	public int getStage()
	{
		return stage;
	}

	@Override
	public void setStage(int stage)
	{
		this.stage = stage;
	}

	
	@Override
	public void grow(int amt)
	{
		if(stage + 1 < card.getMaxStage())
		{
			int i = card.getGrowReq(this);
			growBuffer += amt;
			if(growBuffer > i)
			{
				growBuffer -= i;
				stage++;
				markUpdate();
			}
		}
	}
	
	@Override
	protected int updateRange()
	{
		return 64;
	}

	
	@Override
	public boolean isBlock(int offsetX, int offsetY, int offsetZ, Block block, int meta)
	{
		return U.Worlds.isBlock(worldObj, xCoord + offsetX, yCoord + offsetY, zCoord + offsetZ, block, meta, true);
	}
	

	@Override
	public boolean isBlockNearby(Block block, int meta)
	{
		return U.Worlds.isBlockNearby(worldObj, xCoord, yCoord - 1, zCoord, block, meta, true);
	}

	@Override
	public int countWater(int rangeXZ, int rangeY, boolean checkSea)
	{
		int c = 0;
		int x = xCoord + rangeXZ, y = yCoord - rangeY, z = zCoord + rangeXZ;
		for(int i = xCoord - rangeXZ; i < x; ++i)
			for(int k = zCoord - rangeXZ; k < z; ++k)
			{
				if(!checkSea && worldObj.getBiomeGenForCoords(i, k).getTempCategory() == TempCategory.OCEAN)
					continue;
				for(int j = yCoord - 1; j >= y; --j)
				{
					Block block = worldObj.getBlock(i, j, k);
					if(block == EnumBlock.water.block())
					{
						c += ((BlockFluidBase) block).getQuantaValue(worldObj, i, j, k);
					}
				}
			}
		return c;
	}
	
	@Override
	public void absorbWater(int rangeXZ, int rangeY, int amount, boolean checkSea)
	{
		Block blockRaw = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
		int c = 0;
		if(blockRaw instanceof IPlantedableBlock)
		{
			IPlantedableBlock block = (IPlantedableBlock) blockRaw;
			if(block.getWaterLevel(worldObj, xCoord, yCoord - 1, zCoord, checkSea) > 0)
			{
				c += block.absorbWater(worldObj, xCoord, yCoord - 1, zCoord, amount, checkSea);
			}
		}
		if(c != amount)
		{
			int x = xCoord + rangeXZ, y = yCoord - rangeY, z = zCoord + rangeXZ;
			label:
			for(int i = xCoord - rangeXZ; i < x; ++i)
				for(int k = zCoord - rangeXZ; k < z; ++k)
				{
					if(!checkSea && worldObj.getBiomeGenForCoords(i, k).getTempCategory() == TempCategory.OCEAN)
						continue;
					for(int j = yCoord - 1; j >= y; --j)
					{
						Block block = worldObj.getBlock(i, j, k);
						if(block == EnumBlock.water.block())
						{
							int value = ((BlockFluidBase) block).getQuantaValue(worldObj, i, j, k);
							((BlockStandardFluid) block).setQunataValue(worldObj, x, y, z, value - 1);
							c += Math.min(1000 * absorbEffiency, amount - c);
							if(c == amount) break label;
							break;
						}
					}
				}
		}
		waterLevel += c;
	}
	
	@Override
	public float getRainfall()
	{
		return U.Worlds.isCatchingRain(worldObj, xCoord, yCoord, zCoord) ?
				getBiome().getRainfall(worldObj, xCoord, yCoord, zCoord) *
				(1F + worldObj.rainingStrength * 4F) :
					getBiome().getRainfall(worldObj, xCoord, yCoord, zCoord);
	}
	
	@Override
	public float getTemp()
	{
		return U.Worlds.getTemp(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public int getWaterLevel()
	{
		return waterLevel;
	}
	
	@Override
	public int useWater(int amount)
	{
		int c = Math.min(amount, waterLevel);
		waterLevel -= c;
		return c;
	}

	@Override
	public void killCrop()
	{
		removeBlock(0, 0, 0);
	}
	
	@Override
	public void addDebugInformation(List<String> list)
	{
		list.add("Spcie Name : " + card.name());
		list.add("Name : " + card.getName(getDNA()));
		list.add("DNA : " + info.DNA);
		list.add("Buf : " + growBuffer);
		list.add("Stage : " + (stage + 1) + "/" + card.getMaxStage());
		card.addInformation(this, list);
	}

	public boolean canPlantAt()
	{
		return card == null ? true : card.canPlantAt(worldObj, xCoord, yCoord, zCoord);
	}
}