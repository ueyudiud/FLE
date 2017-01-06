package farcore.lib.tile.abstracts;

import farcore.lib.tile.IUpdatableTile;
import farcore.lib.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import scala.actors.threadpool.Arrays;

public class TEBuffered extends TEUpdatable implements IUpdatableTile
{
	public long timer = Long.MIN_VALUE;
	public long lastActived = Long.MIN_VALUE;
	
	private TileEntity[] tileNearby = new TileEntity[6];
	
	public TEBuffered()
	{
		
	}
	
	@Override
	public void onLoad()
	{
		super.onLoad();
		regetTileNearby();
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		regetTileNearby();
	}
	
	@Override
	public TileEntity getTE(Direction offset)
	{
		return this.tileNearby[offset.ordinal()];
	}
	
	private void regetTileNearby()
	{
		if(this.worldObj.isBlockLoaded(this.pos))
		{
			for(Direction direction : Direction.DIRECTIONS_3D)
			{
				this.tileNearby[direction.ordinal()] = super.getTE(direction);
			}
		}
		else
		{
			Arrays.fill(this.tileNearby, null);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.timer = nbt.getLong("timer");
		this.lastActived = nbt.getLong("lt");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("timer", this.timer);
		nbt.setLong("lt", this.lastActived);
		return nbt;
	}
	
	@Override
	protected final void updateEntity1()
	{
		if(this.timer == Long.MIN_VALUE)
		{
			this.timer = this.worldObj.getTotalWorldTime();
		}
		updateEntity2();
		this.lastActived = this.worldObj.getTotalWorldTime();
	}
	
	protected void updateEntity2()
	{
		
	}
}