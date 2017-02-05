package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public abstract class TECircuitCompacted extends TECircuitBase
{
	protected static final int notifyNeighbour = 0x1000;
	
	protected byte lastStrongPower;
	protected byte lastWeakPower;
	protected byte strongPower;
	protected byte weakPower;
	
	protected int updateDelay;
	
	protected byte mode = 0x0;
	
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
		if(nbt.hasKey("mo"))
		{
			this.mode = nbt.getByte("mo");
			markBlockRenderUpdate();
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setShort("delay", (short) this.updateDelay);
		nbt.setByte("strong", this.strongPower);
		nbt.setByte("weak", this.weakPower);
		nbt.setByte("mode", this.mode);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.updateDelay = nbt.getShort("delay");
		this.strongPower = nbt.getByte("strong");
		this.weakPower = nbt.getByte("weak");
		this.mode = nbt.getByte("mode");
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if(this.updateDelay > 0)
		{
			--this.updateDelay;
			if(this.updateDelay == 0)
			{
				notifyNeighbors();
			}
		}
		updateBody();
		if(this.strongPower != this.lastStrongPower || this.weakPower != this.lastWeakPower)
		{
			notifyNeighbors();
			markDirty();
		}
		this.lastStrongPower = this.strongPower;
		this.lastWeakPower = this.weakPower;
	}
	
	protected void updateBody()
	{
		
	}
	
	protected void markForDelayUpdate(int delay)
	{
		if(this.updateDelay == 0)
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
	
	public void setStrongPower(int strongPower)
	{
		this.strongPower = (byte) strongPower;
	}
	
	public void setWeakPower(int weakPower)
	{
		this.weakPower = (byte) weakPower;
	}
	
	@Override
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, Direction facing, ItemStack stack)
	{
		super.onBlockPlacedBy(state, placer, facing, stack);
		if(isServer())
		{
			updateCircuit();
			if(this.updateDelay == 0)
			{
				notifyNeighbors();
			}
			this.lastStrongPower = this.strongPower;
			this.lastWeakPower = this.weakPower;
		}
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		updateCircuit();
		if(this.updateDelay == 0 && (this.strongPower != this.lastStrongPower || this.weakPower != this.lastWeakPower))
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
		if((this.syncState & notifyNeighbour) != 0)
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