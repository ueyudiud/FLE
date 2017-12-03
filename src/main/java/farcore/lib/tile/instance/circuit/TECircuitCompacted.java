/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.tile.instance.circuit;

import java.io.IOException;

import nebula.common.NebulaSynchronizationHandler;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public abstract class TECircuitCompacted extends TECircuitBase implements INetworkedSyncTile
{
	protected static final int notifyNeighbour = 0x1000;
	
	protected byte	lastPower;
	protected byte	power;
	
	protected int updateDelay;
	
	protected byte mode = 0x0;
	
	@Override
	public void writeToClientInitalization(NBTTagCompound nbt)
	{
		super.writeToClientInitalization(nbt);
		nbt.setByte("p", this.power);
	}
	
	@Override
	protected void initClient(NBTTagCompound nbt)
	{
		super.initClient(nbt);
		this.power = nbt.getByte("p");
	}
	
	@Override
	public void writeNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (type)
		{
		case 0:
			buf.writeByte(this.power);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void readNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (0)
		{
		case 0:
			this.power = buf.readByte();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setByte("mo", this.mode);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if (nbt.hasKey("mo"))
		{
			this.mode = nbt.getByte("mo");
			markBlockRenderUpdate();
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setShort("delay", (short) this.updateDelay);
		nbt.setByte("power", this.power);
		nbt.setByte("mode", this.mode);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.updateDelay = nbt.getShort("delay");
		this.power = nbt.getByte("power");
		this.mode = nbt.getByte("mode");
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if (this.updateDelay > 0)
		{
			--this.updateDelay;
			if (this.updateDelay == 0)
			{
				notifyNeighbors();
			}
		}
		updateBody();
		if (this.power != this.lastPower)
		{
			notifyNeighbors();
			markDirty();
		}
		this.lastPower = this.power;
	}
	
	protected void updateBody()
	{
		
	}
	
	protected void markForDelayUpdate(int delay)
	{
		if (this.updateDelay <= 0)
		{
			this.updateDelay = delay;
		}
		else
		{
			this.updateDelay = Math.min(delay, this.updateDelay);
		}
	}
	
	protected void updateCircuit()
	{
		
	}
	
	public void setRedstonePower(int power)
	{
		this.power = (byte) power;
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		super.onBlockPlacedBy(state, placer, facing, stack);
		if (isServer())
		{
			updateCircuit();
			NebulaSynchronizationHandler.markTileEntityForUpdate(this, 0);
			if (this.updateDelay == 0)
			{
				notifyNeighbors();
			}
			this.lastPower = this.power;
		}
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		updateCircuit();
		NebulaSynchronizationHandler.markTileEntityForUpdate(this, 0);
		if (this.updateDelay == 0 && (this.power != this.lastPower))
		{
			super.notifyNeighbors();
		}
	}
	
	@Override
	protected void notifyNeighbors()
	{
		this.syncState |= notifyNeighbour;
	}
	
	@Override
	protected void onCheckingSyncState()
	{
		super.onCheckingSyncState();
		if ((this.syncState & notifyNeighbour) != 0)
		{
			markDirty();
			super.notifyNeighbors();
		}
	}
	
	protected String optional(int id, String t, String f)
	{
		return is(id) ? t : f;
	}
}
