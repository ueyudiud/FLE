package farcore.lib.tile.abstracts;

import farcore.lib.util.Direction;
import farcore.util.U;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TEWiring extends TESynchronization
{
	protected byte connectFlag;//8 direction.
	protected Integer lastWorldDim;
	protected World lastWorld;
	protected BlockPos last;
	protected Integer nextWorldDim;
	protected World nextWorld;
	protected BlockPos next;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setByte("connectFlag", connectFlag);
		if(lastWorldDim != null)
		{
			nbt.setIntArray("lastpos", new int[]{lastWorldDim.intValue(), last.getX(), last.getY(), last.getZ()});
		}
		if(nextWorldDim != null)
		{
			nbt.setIntArray("nextpos", new int[]{nextWorldDim.intValue(), next.getX(), next.getY(), next.getZ()});
		}
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		connectFlag = nbt.getByte("connectFlag");
		if(nbt.hasKey("lastpos"))
		{
			int[] pos = nbt.getIntArray("lastpos");
			if(pos.length != 4)
				throw new RuntimeException("The position length is not 4, the nbt is broken.");
			lastWorldDim = pos[0];
			last = new BlockPos(pos[1], pos[2], pos[3]);
		}
		if(nbt.hasKey("nextpos"))
		{
			int[] pos = nbt.getIntArray("nextpos");
			if(pos.length != 4)
				throw new RuntimeException("The position length is not 4, the nbt is broken.");
			nextWorldDim = pos[0];
			next = new BlockPos(pos[1], pos[2], pos[3]);
		}
	}

	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setByte("cf", connectFlag);
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if(nbt.hasKey("cf"))
		{
			connectFlag = nbt.getByte("cf");
		}
	}

	@Override
	protected void initServer()
	{
		super.initServer();
		if(lastWorldDim != null)
		{
			lastWorld = U.Worlds.world(lastWorldDim);
		}
		if(nextWorldDim != null)
		{
			nextWorld = U.Worlds.world(nextWorldDim);
		}
	}

	public void setNextLink(int nextWorld, BlockPos nextPos)
	{
		World world = U.Worlds.world(nextWorld);
		if(world != null)
		{
			setNextLink(world, nextPos);
		}
		else
		{
			nextWorldDim = nextWorld;//Might the world already not deleted.
			next = nextPos;
		}
	}
	public void setNextLink(World nextWorld, BlockPos nextPos)
	{
		if(nextWorld != null)
		{
			nextWorldDim = nextWorld.provider.getDimension();
			this.nextWorld = nextWorld;
			next = nextPos;
		}
		else
		{
			nextWorldDim = null;
			this.nextWorld = null;
			next = null;
		}
	}
	
	public void setLastLink(int lastWorld, BlockPos lastPos)
	{
		World world;
		if((world = U.Worlds.world(lastWorld)) != null)
		{
			setLastLink(world, lastPos);
		}
		else
		{
			lastWorldDim = lastWorld;//Might the world already not deleted.
			last = lastPos;
		}
	}
	public void setLastLink(World lastWorld, BlockPos lastPos)
	{
		if(nextWorld != null)
		{
			lastWorldDim = lastWorld.provider.getDimension();
			this.lastWorld = lastWorld;
			last = lastPos;
		}
		else
		{
			lastWorldDim = null;
			this.lastWorld = null;
			last = null;
		}
	}

	public void switchConnectState(Direction direction)
	{
		if(isServer())
		{
			connectFlag |= direction.flag;
			syncToNearby();
			markBlockUpdate();
			markDirty();
		}
	}

	public boolean isAllowConnect(Direction direction)
	{
		if(direction == null || direction == Direction.Q)
			return false;
		return (connectFlag & direction.flag) != 0;
	}

	public boolean canConnectWith(Direction direction)
	{
		if(!isAllowConnect(direction))
			return false;
		if(direction.t != 0)
		{
			if(isServer())
			{
				if(direction == Direction.A)
					return canConnectWith(lastWorld, last);
				else if(direction == Direction.B)
					return canConnectWith(nextWorld, next);
				return false;
			}
			else
				return false;//I don't know what uses can take this option affect in client world.
		}
		else
			return canConnectWith(worldObj, direction.offset(pos));
	}
	
	protected boolean canConnectWith(World targetWorld, BlockPos targetPos)
	{
		if(targetWorld == null || targetPos == null)
			return false;
		IBlockState state = targetWorld.getBlockState(targetPos);
		TileEntity tile = targetWorld.getTileEntity(targetPos);
		return canConnectWith(state, tile);
	}
	
	protected abstract boolean canConnectWith(IBlockState state, TileEntity tile);
}