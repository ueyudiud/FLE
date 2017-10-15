/*
 * copyright© 2016-2017 ueyudiud
 */
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
		if (isServer())
			initServer();
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
		this.initialized = true;
	}
	
	protected void initServer()
	{
		this.initialized = true;
	}
	
	@Override
	public boolean isInitialized()
	{
		return this.initialized;
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
		if (isServer())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeToDescription(nbt);
			sendToAll(new PacketTESync(this.world, this.pos, nbt));
		}
	}
	
	@Override
	public void syncToDim()
	{
		if (isServer())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeToDescription(nbt);
			sendToDim(new PacketTESync(this.world, this.pos, nbt));
		}
	}
	
	@Override
	public void syncToNearby()
	{
		if (isServer())
		{
			float range = getSyncRange();
			if(!this.world.isAreaLoaded(this.pos, (int) range)) return;
			NBTTagCompound nbt = new NBTTagCompound();
			writeToDescription(nbt);
			sendToNearby(new PacketTESync(this.world, this.pos, nbt), range);
		}
	}
	
	protected float getSyncRange()
	{
		return 32F;
	}
	
	@Override
	public void syncToPlayer(EntityPlayer player)
	{
		if (isServer())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeToDescription(nbt);
			sendToPlayer(new PacketTESync(this.world, this.pos, nbt), player);
		}
	}
}