/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.tile.abstracts;

import nebula.common.tile.TESynchronization;
import nebula.common.util.Direction;
import nebula.common.util.Worlds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TEWiring extends TESynchronization
{
	protected byte		connectFlag;		// 8 direction.
	protected Integer	lastWorldDim;
	protected World		lastWorld;
	protected BlockPos	last;
	protected Integer	nextWorldDim;
	protected World		nextWorld;
	protected BlockPos	next;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setByte("connectFlag", this.connectFlag);
		if (this.lastWorldDim != null)
		{
			nbt.setIntArray("lastpos", new int[] { this.lastWorldDim.intValue(), this.last.getX(), this.last.getY(), this.last.getZ() });
		}
		if (this.nextWorldDim != null)
		{
			nbt.setIntArray("nextpos", new int[] { this.nextWorldDim.intValue(), this.next.getX(), this.next.getY(), this.next.getZ() });
		}
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.connectFlag = nbt.getByte("connectFlag");
		if (nbt.hasKey("lastpos"))
		{
			int[] pos = nbt.getIntArray("lastpos");
			if (pos.length != 4) throw new RuntimeException("The position length is not 4, the nbt is broken.");
			this.lastWorldDim = pos[0];
			this.last = new BlockPos(pos[1], pos[2], pos[3]);
		}
		if (nbt.hasKey("nextpos"))
		{
			int[] pos = nbt.getIntArray("nextpos");
			if (pos.length != 4) throw new RuntimeException("The position length is not 4, the nbt is broken.");
			this.nextWorldDim = pos[0];
			this.next = new BlockPos(pos[1], pos[2], pos[3]);
		}
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setByte("cf", this.connectFlag);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if (nbt.hasKey("cf"))
		{
			this.connectFlag = nbt.getByte("cf");
		}
	}
	
	@Override
	protected void initServer()
	{
		super.initServer();
		if (this.lastWorldDim != null)
		{
			this.lastWorld = Worlds.world(this.lastWorldDim);
		}
		if (this.nextWorldDim != null)
		{
			this.nextWorld = Worlds.world(this.nextWorldDim);
		}
	}
	
	public void setNextLink(int nextWorld, BlockPos nextPos)
	{
		World world = Worlds.world(nextWorld);
		if (world != null)
		{
			setNextLink(world, nextPos);
		}
		else
		{
			this.nextWorldDim = nextWorld;// Might the world already not
			// deleted.
			this.next = nextPos;
		}
	}
	
	public void setNextLink(World nextWorld, BlockPos nextPos)
	{
		if (nextWorld != null)
		{
			this.nextWorldDim = nextWorld.provider.getDimension();
			this.nextWorld = nextWorld;
			this.next = nextPos;
		}
		else
		{
			this.nextWorldDim = null;
			this.nextWorld = null;
			this.next = null;
		}
	}
	
	public void setLastLink(int lastWorld, BlockPos lastPos)
	{
		World world;
		if ((world = Worlds.world(lastWorld)) != null)
		{
			setLastLink(world, lastPos);
		}
		else
		{
			this.lastWorldDim = lastWorld;// Might the world already not
			// deleted.
			this.last = lastPos;
		}
	}
	
	public void setLastLink(World lastWorld, BlockPos lastPos)
	{
		if (this.nextWorld != null)
		{
			this.lastWorldDim = lastWorld.provider.getDimension();
			this.lastWorld = lastWorld;
			this.last = lastPos;
		}
		else
		{
			this.lastWorldDim = null;
			this.lastWorld = null;
			this.last = null;
		}
	}
	
	public void switchConnectState(Direction direction)
	{
		if (isServer())
		{
			this.connectFlag |= direction.flag;
			syncToNearby();
			markBlockUpdate();
			markDirty();
		}
	}
	
	public boolean isAllowConnect(Direction direction)
	{
		if (direction == null || direction == Direction.Q) return false;
		return (this.connectFlag & direction.flag) != 0;
	}
	
	public boolean canConnectWith(Direction direction)
	{
		if (!isAllowConnect(direction)) return false;
		if (direction.t != 0)
		{
			if (isServer())
			{
				if (direction == Direction.A)
					return canConnectWith(this.lastWorld, this.last);
				else if (direction == Direction.B) return canConnectWith(this.nextWorld, this.next);
				return false;
			}
			else
				return false;// I don't know what uses can take this option
			// affect in client world.
		}
		else
			return canConnectWith(this.world, direction.offset(this.pos));
	}
	
	protected boolean canConnectWith(World targetWorld, BlockPos targetPos)
	{
		if (targetWorld == null || targetPos == null) return false;
		IBlockState state = targetWorld.getBlockState(targetPos);
		TileEntity tile = targetWorld.getTileEntity(targetPos);
		return canConnectWith(state, tile);
	}
	
	protected abstract boolean canConnectWith(IBlockState state, TileEntity tile);
}
