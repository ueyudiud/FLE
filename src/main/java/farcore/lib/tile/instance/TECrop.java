package farcore.lib.tile.instance;

import java.util.List;
import java.util.Random;

import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICrop;
import farcore.lib.crop.ICropAccess;
import farcore.lib.material.Mat;
import farcore.lib.tile.IDebugableTile;
import farcore.lib.tile.IUpdatableTile;
import farcore.lib.tile.TEAged;
import farcore.lib.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class TECrop extends TEAged implements ICropAccess, IDebugableTile, IUpdatableTile
{
	private static final float absorbEffiency = 0.2F;
	private int waterLevel = 6400;
	private boolean isWild = false;
	private int growBuffer;
	private int stage;
	private ICrop card = ICrop.VOID;
	private CropInfo info;

	public TECrop()
	{

	}

	public void initCrop(ICrop crop)
	{
		initCrop(0, crop.makeNativeDNA(), crop);
		isWild = true;
		syncToNearby();
	}
	public void initCrop(int generations, String dna, ICrop crop)
	{
		card = crop;
		info = new CropInfo();
		info.DNA = dna;
		info.generations = generations;
		crop.decodeDNA(this, dna);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("water", waterLevel);
		nbt.setBoolean("isWild", isWild);
		nbt.setInteger("growBuf", growBuffer);
		nbt.setInteger("stage", stage);
		nbt.setString("crop", card.getRegisteredName());
		if(info != null)
			info.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		waterLevel = nbt.getInteger("water");
		isWild = nbt.getBoolean("isWild");
		growBuffer = nbt.getInteger("growBuf");
		stage = nbt.getInteger("stage");
		card = Mat.register.get(nbt.getString("crop")).crop;
		info = new CropInfo();
		info.readFromNBT(nbt);
	}

	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setInteger("w", waterLevel);
		nbt.setBoolean("wi", isWild);
		nbt.setInteger("g", growBuffer);
		nbt.setInteger("s", stage);
		nbt.setString("c", card.getRegisteredName());
		NBTTagCompound nbt1;
		info.writeToNBT(nbt1 = new NBTTagCompound());
		nbt.setTag("i", nbt1);
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if(nbt.hasKey("w"))
			waterLevel = nbt.getInteger("w");
		if(nbt.hasKey("wi"))
			isWild = nbt.getBoolean("wi");
		if(nbt.hasKey("g"))
			growBuffer = nbt.getInteger("g");
		if(nbt.hasKey("s"))
			stage = nbt.getInteger("s");
		if(nbt.hasKey("c"))
			card = Mat.register.get(nbt.getString("c")).crop;
		if(nbt.hasKey("i"))
		{
			info = new CropInfo();
			info.readFromNBT(nbt.getCompoundTag("i"));
		}
	}

	@Override
	protected long getNextUpdateTick(long thisTick)
	{
		return thisTick + card.tickUpdate(this);
	}

	@Override
	protected void updateServer1()
	{
		super.updateServer1();
		card.onUpdate(this);
		syncToNearby();
	}

	@Override
	public String getDNA()
	{
		return info.DNA;
	}

	@Override
	public ICrop crop()
	{
		return card;
	}

	@Override
	public CropInfo info()
	{
		return info;
	}

	@Override
	public Biome biome()
	{
		return worldObj.getBiomeGenForCoords(pos);
	}

	@Override
	public boolean isWild()
	{
		return isWild;
	}

	@Override
	public Random rng()
	{
		return random;
	}

	@Override
	public int stage()
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
				markBlockUpdate();
				markBlockRenderUpdate();
			}
		}
	}

	@Override
	protected int getRenderUpdateRange()
	{
		return 5;
	}

	//	@Override
	//	public int countWater(int rangeXZ, int rangeY, boolean checkSea)
	//	{
	//		int c = 0;
	//		int x = xCoord + rangeXZ, y = yCoord - rangeY, z = zCoord + rangeXZ;
	//		for(int i = xCoord - rangeXZ; i < x; ++i)
	//			for(int k = zCoord - rangeXZ; k < z; ++k)
	//			{
	//				if(!checkSea && worldObj.getBiomeGenForCoords(i, k).getTempCategory() == TempCategory.OCEAN)
	//					continue;
	//				for(int j = yCoord - 1; j >= y; --j)
	//				{
	//					Block block = worldObj.getBlock(i, j, k);
	//					if(block == EnumBlock.water.block())
	//					{
	//						c += ((BlockFluidBase) block).getQuantaValue(worldObj, i, j, k);
	//					}
	//				}
	//			}
	//		return c;
	//	}
	//
	//	@Override
	//	public void absorbWater(int rangeXZ, int rangeY, int amount, boolean checkSea)
	//	{
	//		Block blockRaw = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
	//		int c = 0;
	//		if(blockRaw instanceof IPlantedableBlock)
	//		{
	//			IPlantedableBlock block = (IPlantedableBlock) blockRaw;
	//			if(block.getWaterLevel(worldObj, xCoord, yCoord - 1, zCoord, checkSea) > 0)
	//			{
	//				c += block.absorbWater(worldObj, xCoord, yCoord - 1, zCoord, amount, checkSea);
	//			}
	//		}
	//		if(c != amount)
	//		{
	//			int x = xCoord + rangeXZ, y = yCoord - rangeY, z = zCoord + rangeXZ;
	//			label:
	//			for(int i = xCoord - rangeXZ; i < x; ++i)
	//				for(int k = zCoord - rangeXZ; k < z; ++k)
	//				{
	//					if(!checkSea && worldObj.getBiomeGenForCoords(i, k).getTempCategory() == TempCategory.OCEAN)
	//						continue;
	//					for(int j = yCoord - 1; j >= y; --j)
	//					{
	//						Block block = worldObj.getBlock(i, j, k);
	//						if(block == EnumBlock.water.block())
	//						{
	//							int value = ((BlockFluidBase) block).getQuantaValue(worldObj, i, j, k);
	//							((BlockStandardFluid) block).setQunataValue(worldObj, x, y, z, value - 1);
	//							c += Math.min(1000 * absorbEffiency, amount - c);
	//							if(c == amount) break label;
	//							break;
	//						}
	//					}
	//				}
	//		}
	//		waterLevel += c;
	//	}
	//
	//	@Override
	//	public float getRainfall()
	//	{
	//		return U.Worlds.isCatchingRain(worldObj, xCoord, yCoord, zCoord) ?
	//				getBiome().getRainfall(worldObj, xCoord, yCoord, zCoord) *
	//				(1F + worldObj.rainingStrength * 4F) :
	//					getBiome().getRainfall(worldObj, xCoord, yCoord, zCoord);
	//	}
	//
	//	@Override
	//	public float getTemp()
	//	{
	//		return U.Worlds.getTemp(worldObj, xCoord, yCoord, zCoord);
	//	}

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
		removeBlock();
	}

	@Override
	public void addDebugInformation(EntityPlayer player, Direction side, List<String> list)
	{
		list.add("Spcie Name : " + card.getRegisteredName());
		list.add("Name : " + card.getLocalName(getDNA()));
		list.add("DNA : " + info.DNA);
		list.add("Buf : " + growBuffer);
		list.add("Stage : " + (stage + 1) + "/" + card.getMaxStage());
		card.addInformation(this, list);
	}

	public boolean canPlantAt()
	{
		return card == null ? true : card.canPlantAt(this);
	}

	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		if(!canBlockStay())
		{
			state.getBlock().dropBlockAsItem(worldObj, pos, state, 0);
			killCrop();
		}
	}
}