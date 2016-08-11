package farcore.lib.tile;

import farcore.lib.net.tile.PacketTESync;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TEStatic extends TEBase implements ISynchronizableTile
{
	public boolean initialized = false;

	public TEStatic()
	{

	}

	@Override
	public boolean isInitialized()
	{
		return initialized;
	}

	@Override
	public final void readFromDescription(NBTTagCompound nbt)
	{
		readFromDescription1(nbt);
		markBlockUpdate();
		markBlockRenderUpdate();
		initialized = true;
	}

	public void readFromDescription1(NBTTagCompound nbt)
	{

	}

	public void writeToDescription(NBTTagCompound nbt)
	{

	}

	@Override
	public void syncToAll()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToDescription(nbt);
		sendToAll(new PacketTESync(worldObj, pos, nbt));
	}

	@Override
	public void syncToDim()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToDescription(nbt);
		sendToDim(new PacketTESync(worldObj, pos, nbt));
	}

	@Override
	public void syncToNearby()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToDescription(nbt);
		sendToNearby(new PacketTESync(worldObj, pos, nbt), getSyncRange());
	}

	protected float getSyncRange()
	{
		return 32F;
	}

	@Override
	public void syncToPlayer(EntityPlayer player)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToDescription(nbt);
		sendToPlayer(new PacketTESync(worldObj, pos, nbt), player);
	}

	@Override
	public void markDirty()
	{
		if (worldObj != null)
		{
			IBlockState state = worldObj.getBlockState(pos);
			worldObj.markChunkDirty(pos, this);
			if (!state.getBlock().isAir(state, worldObj, pos))
			{
				worldObj.updateComparatorOutputLevel(pos, getBlockType());
			}
		}
	}
}