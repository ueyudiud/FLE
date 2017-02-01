package nebula.common.tile;

import nebula.common.network.packet.PacketTESync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TEStatic extends TEBase implements ISynchronizableTile
{
	public boolean initialized = false;
	
	public TEStatic()
	{
		
	}

	@Override
	public void onLoad()
	{
		if(isServer())
		{
			initServer();
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToDescription(nbt);
		nbt.merge(super.getUpdateTag());
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readFromDescription(tag);
		initialized = true;
	}
	
	protected void initServer()
	{
		initialized = true;
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
		//		markBlockUpdate();
		//		markBlockRenderUpdate();
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
		sendToAll(new PacketTESync(world, pos, nbt));
	}
	
	@Override
	public void syncToDim()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToDescription(nbt);
		sendToDim(new PacketTESync(world, pos, nbt));
	}
	
	@Override
	public void syncToNearby()
	{
		float range = getSyncRange();
		if(!world.isAreaLoaded(pos, (int) range)) return;
		NBTTagCompound nbt = new NBTTagCompound();
		writeToDescription(nbt);
		sendToNearby(new PacketTESync(world, pos, nbt), range);
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
		sendToPlayer(new PacketTESync(world, pos, nbt), player);
	}
}