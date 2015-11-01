package fle.core.te.tank;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;
import fle.api.inventory.IInventoryTile;
import fle.api.te.IFluidTanks;
import fle.api.te.TEInventory;
import fle.api.world.BlockPos;
import fle.core.inventory.tank.IConnectableTank;

public abstract class TileEntityAbstractTank<T extends IInventoryTile> extends TEInventory<T> implements IFluidHandler, IFluidTanks
{
	public FluidTank tank;
	
	protected TileEntityAbstractTank mainTile;
	protected boolean isMainTile = false;
	protected final List<BlockPos> pos = new ArrayList();
	
	private ThreadLocal<BlockPos> thread = new ThreadLocal();

	public int width;
	public int height;
	
	public TileEntityAbstractTank(T inv)
	{
		super(inv);
	}

	public FluidTank getMainTank()
	{
		return isMainTile ? tank : mainTile == null ? new FluidTank(0) : mainTile.getMainTank();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		int x = nbt.getInteger("mainX"),
				y = nbt.getInteger("mainY"),
				z = nbt.getInteger("mainZ");
		isMainTile = nbt.getBoolean("Main");
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
		nbt.setBoolean("Main", isMainTile);
	}

	protected abstract void initMainTank(int cap);
	protected abstract void removeMainTank();
	
	@Override
	protected void onPostinit()
	{
		capcity = -1;
		if(isMainTile)
		{
			return;
		}
		BlockPos pos = thread.get();
		if(pos != null)
		{
			TileEntity tile = worldObj.getTileEntity(pos.x, pos.y, pos.z);
			if(tile instanceof TileEntityAbstractTank)
			{
				mainTile = ((TileEntityAbstractTank) tile).markTileConnect(this);
			}
		}
	}

	int checkBuf = 0;
	
	@Override
	protected void updateInventory()
	{
		if(isMainTile)
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
			++checkBuf;
			if(checkBuf > 100)
			{
				checkBuf = 0;
				onNeibourChange(false);
			}
		}
	}
	
	private int capcity = -1;
	
	private boolean checkCanMakeTank()
	{
		boolean flag = false;
		label0:
		for(int bSize = 3; bSize < 16; ++bSize)
			label1:
			for(int ySize = 3; ySize < 20; ++ySize)
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
									if(!canConnectWith(tile))
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
					if(tile instanceof TileEntityAbstractTank)
					{
						((TileEntityAbstractTank) tile).mainTile = this;
						((TileEntityAbstractTank) tile).isMainTile = false;
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
	
	protected abstract boolean canConnectWith(TileEntity tile);
	
	public void onNeibourChange(boolean flag)
	{
		capcity = -1;
		if(!isMainTile && mainTile == null && checkCanMakeTank())
		{
			isMainTile = true;
			mainTile = null;
			initMainTank(capcity);
			capcity = -1;
			return;
		}
		if(isMainTile)
		{
			List<BlockPos> cache = new ArrayList<BlockPos>(pos);
			if(!checkCanMakeTank())
			{
				capcity = -1;
				for(BlockPos tPos : cache)
				{
					TileEntity tile = tPos.getBlockTile();
					if(tile instanceof TileEntityAbstractTank)
					{
						((TileEntityAbstractTank) tile).isMainTile = false;
						((TileEntityAbstractTank) tile).mainTile = null;
					}
				}
				pos.clear();
				isMainTile = false;
				removeMainTank();
			}
		}
		else if(mainTile != null && flag)
		{
			mainTile.onNeibourChange(false);
		}
	}
	
	@Override
	public T getTileInventory()
	{
		if(mainTile == this) mainTile = null;
		return (T) (isMainTile ? super.getTileInventory() : mainTile == null ? super.getTileInventory() : mainTile.getTileInventory());
	}
	
	protected T getThisInventory()
	{
		return super.getTileInventory();
	}
	
	private TileEntityAbstractTank markTileConnect(TileEntityAbstractTank tile)
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
		if(isMainTile)
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
}