package fle.core.te.cultivation;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.cover.Cover;
import fle.api.inventory.InventoryTileBase;
import fle.api.item.IBeeComb;
import fle.api.te.TEBase;
import fle.api.te.TEInventory;
import fle.core.util.LanguageManager;
import fle.core.util.WorldUtil;

public class TileEntityBeeHive extends TEBase implements ISidedInventory
{
	public boolean hasBee = false;
	long[] values = new long[20];
	ItemStack[] stacks = new ItemStack[20];
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList("Slots", 10);
		int i;
		Arrays.fill(stacks, null);
		for(i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			int slotId = nbt1.getByte("SlotId");
			stacks[slotId] = ItemStack.loadItemStackFromNBT(nbt1);
			values[i] = nbt1.getLong("Value");
		}
		markCacheForUpdate();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < stacks.length; ++i)
		{
			NBTTagCompound nbt1;
			if(stacks[i] == null) nbt1 = stacks[i].writeToNBT(new NBTTagCompound());
			else nbt1 = new NBTTagCompound();
			nbt1.setLong("Value", values[i]);
			nbt1.setByte("SlotId", (byte) i);
			list.appendTag(nbt1);
		}
		nbt.setTag("Slots", list);
	}

	private short c;
	private int tempCache;
	private int rainCache;
	private int queenCountCache;
	private int beeCountCache;
	private int beeMaxCache;
	private int larvaCountCache;
	private int larvaMaxCache;
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(!hasBee) return;
		if(c++ > 40)
		{
			c = 0;
			updateCache();
		}
		updateComb();
	}
	
	protected void updateCache()
	{
		tempCache = FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
		rainCache = (int) WorldUtil.getWaterLevel(worldObj, xCoord, yCoord, zCoord);
		int bcc = 0;
		int lcc = 0;
		int bmc = 0;
		int lmc = 0;
		int qc = 0;
		for(int y = 0; y < 4; ++y)
			for(int x = 0; x < 5; ++x)
			{
				bcc += getValue(BEES, x, y);
				lcc += getValue(LARVA, x, y);

				ItemStack stack = getStackWithCoord(x, y);
				if(stack != null)
				{
					if(stack.getItem() instanceof IBeeComb)
					{
						IBeeComb comb = (IBeeComb) stack.getItem();
						bmc += comb.getMaxBeeCap(stack);
						lmc += comb.getMaxLarvaCap(stack);
						qc += comb.getQueenCount(stack);
					}
				}
			}
		beeCountCache = bcc;
		larvaCountCache = lcc;
		queenCountCache = qc;
	}
	
	protected void updateComb()
	{
		for(int y = 0; y < 4; ++y)
			for(int x = 0; x < 5; ++x)
			{
				boolean main = x != 0 && y != 0 && x != 4 && y != 3;
				ItemStack stack = getStackWithCoord(x, y);
				ItemStack output = null;
				if(stack != null)
				{
					if(stack.getItem() instanceof IBeeComb)
					{
						IBeeComb comb = (IBeeComb) stack.getItem();
						if(queenCountCache > 1 && comb.getQueenCount(stack) > 1) output = comb.deQueen(stack);
						short honey = getValue(HONEY, x, y);
						short bees = getValue(BEES, x, y);
						ItemStack newComb = comb.putNewComb(stack, queenCountCache, honey, bees);
						int t = comb.getMaturationTick(stack);
						if(t > 0)
						{
							short age = addAndGetValue(AGE, comb.getGrowSpeed(worldObj, getBlockPos(), stack, beeCountCache), x, y);
							if(age > t)
							{
								output = comb.onMaturation(stack, getValue(LARVA, x, y), honey);
								removeValue(x, y);
							}
						}
						if(newComb != null && main)
						{
							if(addCombIn(x, y + 1, newComb));
							else if(addCombIn(x + 1, y, newComb));
							else if(addCombIn(x - 1, y, newComb));
							else if(addCombIn(x, y - 1, newComb));
						}
					}
					else
					{
						dropItem(stack);
						stacks[x + y * 5] = null;
					}
				}
			}
	}
	
	protected boolean addCombIn(int x, int y, ItemStack comb)
	{
		if(x < 0 || x >= 5 || y < 0 || y >= 4) return false;
		if(comb == null) return true;
		if(stacks[x + y * 5] == null)
		{
			stacks[x + y * 5] = comb.copy();
			return true;
		}
		return false;
	}

	protected static final byte AGE = 0;
	protected static final byte HONEY = 16;
	protected static final byte BEES = 32;
	protected static final byte LARVA = 48;

	protected short getValue(byte type, int x, int y)
	{
		return x < 0 || x >= 5 || y < 0 || y >= 4 ? 0 : (short) ((values[y * 5 + x] >> type) & 0xFFFF);
	}
	protected short addAndGetValue(byte type, int add, int x, int y)
	{
		if(x < 0 || x >= 5 || y < 0 || y >= 4) return 0;
		short s = (short) ((values[y * 5 + x] >> type) & 0xFFFF);
		values[y * 5 + x] |= ~(0xFFFF << type);
		values[y * 5 + x] |= ((short) (s + add)) << type;
		return (short) (s + add);
	}
	protected void setValue(byte type, short contain, int x, int y)
	{
		if(x < 0 || x >= 5 || y < 0 || y >= 4) return;
		values[y * 5 + x] |= ~(0xFFFF << type);
		values[y * 5 + x] |= contain << type;
	}
	protected void removeValue(byte type, int x, int y)
	{
		if(x < 0 || x >= 5 || y < 0 || y >= 4) return;
		values[y * 5 + x] |= ~(0xFFFF << type);
	}
	protected void removeValue(int x, int y)
	{
		if(x < 0 || x >= 5 || y < 0 || y >= 4) return;
		values[y * 5 + x] = 0;
	}
	
	protected void markCacheForUpdate()
	{
		c = 40;
	}

	@Override
	protected boolean canAddCoverWithSide(ForgeDirection dir, Cover cover)
	{
		return false;
	}
	
	@Override
	public int getSizeInventory()
	{
		return stacks.length;
	}
	
	public ItemStack getStackWithCoord(int x, int y)
	{
		return x < 0 || x >= 5 || y < 0 || y >= 4 ? null : stacks[y * 5 + x];
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int size)
	{
		if(stacks[i] == null) return null;
		ItemStack stack = stacks[i].copy();
		stack.stackSize = Math.min(stacks[i].stackSize, size);
		stacks[i].stackSize -= stack.stackSize;
		if(stacks[i].stackSize == 0) stacks[i] = null;
		if(stack.getItem() instanceof IBeeComb)
		{
			IBeeComb comb = (IBeeComb) stacks[i].getItem();
			comb.saveToNBT(stack, getValue(AGE, i % 5, i / 5), getValue(HONEY, i % 5, i / 5));
			removeValue(i % 5, i / 5);
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack stack)
	{
		if(stack != null) stacks[i] = stack.copy();
		else stacks[i] = null;
	}

	@Override
	public String getInventoryName()
	{
		return LanguageManager.regWithRecommendedUnlocalizedName("InventoryBeeHive", "Bee Hive");
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return player.getDistance(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 256;
	}

	@Override
	public void openInventory()
	{
		
	}

	@Override
	public void closeInventory()
	{
		
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack)
	{
		return stack.getItem() instanceof IBeeComb;
	}
	
	private static final int[] is;
	static
	{
		is = new int[20];
		for(int i = 0; i < 20; is[i++] = i);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side == ForgeDirection.DOWN.ordinal() ? is : new int[0];
	}

	@Override
	public boolean canInsertItem(int i, ItemStack stack,
			int side)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack stack,
			int side)
	{
		return stacks[i] != null && side == ForgeDirection.DOWN.ordinal();
	}
}