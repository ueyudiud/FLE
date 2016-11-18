package farcore.lib.tile.instance.circuit;

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
		nbt.setByte("mo", mode);
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if(nbt.hasKey("mo"))
		{
			mode = nbt.getByte("mo");
			markBlockRenderUpdate();
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setShort("delay", (short) updateDelay);
		nbt.setByte("strong", strongPower);
		nbt.setByte("weak", weakPower);
		nbt.setByte("mode", mode);
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		updateDelay = nbt.getShort("delay");
		strongPower = nbt.getByte("strong");
		weakPower = nbt.getByte("weak");
		mode = nbt.getByte("mode");
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if(updateDelay > 0)
		{
			--updateDelay;
			if(updateDelay == 0)
			{
				notifyNeighbors();
			}
		}
		updateBody();
		if(strongPower != lastStrongPower || weakPower != lastWeakPower)
		{
			notifyNeighbors();
			markDirty();
		}
		lastStrongPower = strongPower;
		lastWeakPower = weakPower;
	}

	protected void updateBody()
	{

	}
	
	protected void markForDelayUpdate(int delay)
	{
		if(updateDelay == 0)
		{
			updateDelay = delay;
		}
		else
		{
			updateDelay = Math.min(delay, updateDelay);
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
	public void onBlockPlacedBy(IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(state, placer, stack);
		if(isServer())
		{
			updateCircuit();
			if(updateDelay == 0)
			{
				notifyNeighbors();
			}
			lastStrongPower = strongPower;
			lastWeakPower = weakPower;
		}
	}

	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		updateCircuit();
		if(updateDelay == 0 && (strongPower != lastStrongPower || weakPower != lastWeakPower))
		{
			super.notifyNeighbors();
		}
	}

	@Override
	protected void notifyNeighbors()
	{
		syncState |= notifyNeighbour;
	}

	@Override
	protected void onCheckingSyncState()
	{
		super.onCheckingSyncState();
		if((syncState & notifyNeighbour) != 0)
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