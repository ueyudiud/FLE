package flapi.te;

import java.util.ArrayList;
import java.util.List;

import flapi.te.interfaces.IFluidTanks;
import flapi.te.interfaces.IMultiTankPart;
import flapi.world.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class TEAbstractTank extends TEInventory implements IFluidHandler, IFluidTanks, IMultiTankPart
{
	public FluidTank tank;
	
	protected TEAbstractTank mainTile;
	protected final List<BlockPos> pos = new ArrayList();
	
	private ThreadLocal<BlockPos> thread = new ThreadLocal();

	public int width;
	public int height;
	
	public TEAbstractTank(InventoryTile inv)
	{
		super(inv);
	}

	public FluidTank getMainTank()
	{
		return should(MAINTILE) ? tank : mainTile == null ? new FluidTank(0) : mainTile.getMainTank();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		int x = nbt.getInteger("mainX"),
				y = nbt.getInteger("mainY"),
				z = nbt.getInteger("mainZ");
		if(nbt.getBoolean("Main")) enable(MAINTILE);
		if(y != 0)
			thread.set(new BlockPos(null, x, y, z));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if(mainTile != null)
		{
			nbt.setInteger("mainX", mainTile.xCoord);
			nbt.setInteger("mainY", mainTile.yCoord);
			nbt.setInteger("mainZ", mainTile.zCoord);
		}
		nbt.setBoolean("Main", should(MAINTILE));
	}

	protected abstract void initMainTank(int cap);
	protected abstract void removeMainTank();
	
	@Override
	public void updateEntity()
	{
		capcity = -1;
		if(should(MAINTILE))
		{
			++checkBuf;
			if(checkBuf > 100)
			{
				checkBuf = 0;
				onNeibourChange(false);
			}
		}
		else if(mainTile == null)
		{
			if(thread.get() != null)
				markTileConnect((TEAbstractTank) thread.get().getBlockTile());
			++checkBuf;
			if(checkBuf > 100)
			{
				checkBuf = 0;
				onNeibourChange(false);
			}
		}
		BlockPos pos = thread.get();
		if(pos != null)
		{
			TileEntity tile = worldObj.getTileEntity(pos.x, pos.y, pos.z);
			if(tile instanceof TEAbstractTank)
			{
				mainTile = ((TEAbstractTank) tile).markTileConnect(this);
			}
		}
	}

	int checkBuf = 0;
	
	private int capcity = -1;

	protected int getMaxWidth(){return 16;}
	protected int getMaxHeight(){return 16;}
	
	private final boolean checkCanMakeTank()
	{
		boolean flag = false;
		label0:
		for(int bSize = 3; bSize < getMaxWidth(); ++bSize)
			label1:
			for(int ySize = 3; ySize < getMaxHeight(); ++ySize)
			{
				BlockPos pos = getBlockPos();
				for(int i = -1; i <= bSize; ++i)
					for(int j = -1; j <= ySize; ++j)
						for(int k = -1; k <= bSize; ++k)
						{
							boolean shouldHas1 = i == -1 || j == -1 || k == -1 || i == bSize || k == bSize || j == ySize;
							boolean shouldHas = !shouldHas1 && (i == 0 || j == 0 || k == 0 || i == bSize - 1 || k == bSize - 1);
							TileEntity tile = pos.toPos(i, j, k).getBlockTile();
							if(shouldHas)
							{
								if(tile != null)
								{
									if(!canConnectWith(tile, i, j, k, bSize, ySize))
									{
										this.pos.clear();
										continue label1;
									}
								}
								else
								{
									this.pos.clear();
									continue label1;
								}
							}
							else
							{
								if(!shouldHas1)
								{
									if(!pos.toPos(i, j, k).isAir())
									{
										this.pos.clear();
										continue label1;
									}
								}
								else if(tile == null ? false : canConnectWith(tile))
								{
									this.pos.clear();
									continue label1;
								}
								continue;
							}
							this.pos.add(new BlockPos(worldObj, xCoord + i, yCoord + j, zCoord + k));
						}
				for(BlockPos tPos : this.pos)
				{
					TileEntity tile = tPos.getBlockTile();
					if(tile == this) continue;
					if(tile instanceof IMultiTankPart)
					{
						((IMultiTankPart) tile).onConnect(this, tPos.x - xCoord, tPos.y - yCoord, tPos.z - zCoord, bSize, ySize);
					}
				}
				flag = true;
				capcity = (bSize - 1) * (bSize - 1) * (ySize - 1) * 1000;
				width = bSize;
				height = ySize;
				break label0;
			}
		if(!flag)
		{
			pos.clear();
			width = 0;
			height = 0;
		}
		return flag;
	}
	
	@Override
	public boolean isConnected()
	{
		return should(MAINTILE) || mainTile != null;
	}
	
	@Override
	public void onConnect(TEAbstractTank main, int xPos, int yPos, int zPos,
			int width, int height)
	{
		mainTile = main;
		disable(MAINTILE);
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean canBeConnect(TEAbstractTank main, int xPos,
			int yPos, int zPos, int width, int height)
	{
		return canBeConnect(main);
	}
	
	@Override
	public boolean canBeConnect(TEAbstractTank main)
	{
		return true;
	}

	protected boolean canConnectWith(TileEntity tile, int xPos, int yPos, int zPos, int width, int height)
	{
		return canConnectWith(tile) && (tile instanceof IMultiTankPart ? ((IMultiTankPart) tile).canBeConnect(this, xPos, yPos, zPos, width, height) : true);
	}
	
	protected abstract boolean canConnectWith(TileEntity tile);
	
	public void onNeibourChange(boolean flag)
	{
		capcity = -1;
		if(!should(MAINTILE) && mainTile == null && checkCanMakeTank())
		{
			enable(MAINTILE);
			mainTile = null;
			initMainTank(capcity);
			capcity = -1;
			return;
		}
		if(should(MAINTILE))
		{
			List<BlockPos> cache = new ArrayList<BlockPos>(pos);
			if(!checkCanMakeTank())
			{
				capcity = -1;
				for(BlockPos tPos : cache)
				{
					TileEntity tile = tPos.getBlockTile();
					if(tile instanceof IMultiTankPart)
					{
						((IMultiTankPart) tile).onDisconnect(this);
					}
				}
				pos.clear();
				disable(MAINTILE);
				removeMainTank();
			}
		}
		else if(mainTile != null && flag)
		{
			mainTile.onNeibourChange(false);
		}
	}
	
	@Override
	public void onDisconnect(TEAbstractTank main)
	{
		disable(MAINTILE);
		mainTile = null;
		width = 0;
		height = 0;
	}
	
	private TEAbstractTank markTileConnect(TEAbstractTank tile)
	{
		if(mainTile != null)
		{
			mainTile.pos.add(new BlockPos(mainTile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord));
			return mainTile;
		}
		pos.add(new BlockPos(worldObj, tile.xCoord, tile.yCoord, tile.zCoord));
		return this;
	}
	
	public List<String> getDebugInfo()
	{
		List<String> list = new ArrayList<String>();
		if(should(MAINTILE))
		{
			list.add("This block is main control of this tank.");
		}
		else if(mainTile != null)
		{
			list.add("This block is a simple block of this tank.");
		}
		else
		{
			list.add("structure losing");
		}
		return list;
	}
	
	protected static final int MAINTILE = 2;
}