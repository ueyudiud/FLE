package nebula.common.tile;

import java.util.ArrayList;
import java.util.List;

import nebula.common.nbt.NBTSynclizedCompound;
import nebula.common.network.packet.PacketTESAsk;
import nebula.common.network.packet.PacketTESync;
import nebula.common.util.NBTs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.world.EnumSkyBlock;

public class TESynchronization extends TEBuffered
implements ISynchronizableTile
{
	/**
	 * This a state assistant.
	 * Use to contain boolean state.
	 */
	protected long state;
	private long lastState;
	
	private boolean initialized = false;
	public NBTSynclizedCompound nbt = new NBTSynclizedCompound();
	
	/**
	 * The sync state.
	 * 1 for mark to all.
	 * 2 for mark to dim.
	 * 4 for mark to near by.
	 * 8 for mark block update.
	 * 16 for mark render update.
	 * 32 for mark sky light update.
	 * 64 for mark block light update.
	 * 128 for mark dirty (mark this tile entity ant chunk is required save).
	 */
	public long syncState = 0L;
	private List<EntityPlayer> syncAskedPlayer = new ArrayList<>();
	
	public TESynchronization()
	{
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setLong("state", this.state);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.state = nbt.getLong("state");
	}
	
	@Override
	public void syncToAll()
	{
		this.syncState |= 0x1;
	}
	
	@Override
	public void syncToDim()
	{
		this.syncState |= 0x2;
	}
	
	@Override
	public void syncToNearby()
	{
		this.syncState |= 0x4;
	}
	
	@Override
	public void syncToPlayer(EntityPlayer player)
	{
		this.syncAskedPlayer.add(player);
	}
	
	@Override
	public void markBlockUpdate()
	{
		this.syncState |= 0x8;
	}
	
	@Override
	public void markBlockRenderUpdate()
	{
		this.syncState |= 0x10;
	}
	
	@Override
	public void markDirty()
	{
		this.syncState |= 0x80;
	}
	
	@Override
	public void markLightForUpdate(EnumSkyBlock type)
	{
		if(type == EnumSkyBlock.SKY)
		{
			this.syncState |= 0x20;
		}
		if(type == EnumSkyBlock.BLOCK)
		{
			this.syncState |= 0x40;
		}
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
		//Needn't always update render and block states.
		//		markBlockUpdate();
		//		markBlockRenderUpdate();
		this.initialized = true;
	}
	
	public void readFromDescription1(NBTTagCompound nbt)
	{
		this.state = NBTs.getLongOrDefault(nbt, "s", this.state);
	}
	
	public void writeToDescription(NBTTagCompound nbt)
	{
		nbt.setLong("s", this.state);
	}
	
	//TESynchronization use custom packet for sync.
	@Override
	@Deprecated
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return null;
	}
	
	@Override
	@Deprecated
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
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
		if(!this.initialized)
		{
			initClient(tag);
		}
		else
		{
			readFromDescription(tag);
		}
	}
	
	@Override
	protected final void updateEntity2()
	{
		if(isServer())
		{
			updateServer();
			updateServerStates();
		}
		else
		{
			updateClient();
			updateClientStates();
		}
	}
	
	protected void updateServerStates()
	{
		if(this.lastState != this.state)
		{
			onStateChanged(true);
			this.lastState = this.state;
		}
		this.nbt.reset();
		writeToDescription(this.nbt);
		if((this.syncState & 0x1) != 0)
		{
			sendToAll(new PacketTESync(this.world, this.pos, this.nbt.getChanged(true)));
		}
		else if((this.syncState & 0x2) != 0)
		{
			sendToDim(new PacketTESync(this.world, this.pos, this.nbt.getChanged(true)));
		}
		else if((this.syncState & 0x4) != 0)
		{
			sendToNearby(new PacketTESync(this.world, this.pos, this.nbt.getChanged(true)), getSyncRange());
		}
		if((this.syncState & 0x8) != 0)
		{
			this.world.notifyNeighborsOfStateChange(this.pos, getBlockType());
		}
		if((this.syncState & 0x80) != 0)
		{
			super.markDirty();
		}
		int state = 0;
		if((this.syncState & 0x10) != 0) { state |= 0x1; }
		if((this.syncState & 0x20) != 0) { state |= 0x2; }
		if((this.syncState & 0x40) != 0) { state |= 0x4; }
		sendToNearby(new PacketTESAsk(this.world, this.pos, state), getSyncRange());
		onCheckingSyncState();
		this.syncState = 0;
		if(!this.syncAskedPlayer.isEmpty())
		{
			for(EntityPlayer player : this.syncAskedPlayer)
			{
				sendToPlayer(new PacketTESync(this.world, this.pos, this.nbt.asCompound()), player);
			}
		}
		this.syncAskedPlayer.clear();
	}
	
	protected void updateClientStates()
	{
		if(this.lastState != this.state)
		{
			onStateChanged(false);
			this.lastState = this.state;
		}
		if((this.syncState & 0x8) != 0)
		{
			super.markBlockUpdate();
		}
		if((this.syncState & 0x10) != 0)
		{
			int range = getRenderUpdateRange();
			this.world.markBlockRangeForRenderUpdate(this.pos.add(-range, -range, -range), this.pos.add(range, range, range));
		}
		if((this.syncState & 0x20) != 0)
		{
			super.markLightForUpdate(EnumSkyBlock.SKY);
		}
		if((this.syncState & 0x40) != 0)
		{
			super.markLightForUpdate(EnumSkyBlock.BLOCK);
		}
		onCheckingSyncState();
		this.syncState = 0;
		this.syncAskedPlayer.clear();
	}
	
	
	/**
	 * Called when checking state.
	 */
	protected void onCheckingSyncState()
	{
		
	}
	
	
	protected void onStateChanged(boolean isServerSide)
	{
		if(isServerSide)
		{
			markDirty();
			syncToNearby();
		}
		else
		{
			markBlockUpdate();
			markBlockRenderUpdate();
		}
	}
	
	protected float getSyncRange()
	{
		return 16F;
	}
	
	protected int getRenderUpdateRange()
	{
		return 3;
	}
	
	protected void initServer()
	{
		this.initialized = true;
	}
	
	protected void initClient(NBTTagCompound nbt)
	{
		readFromDescription(nbt);
	}
	
	protected void updateServer()
	{
		
	}
	
	protected void updateClient()
	{
		
	}
	
	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		onRemoveFromLoadedWorld();
	}
	
	@Override
	public void invalidate()
	{
		super.invalidate();
		onRemoveFromLoadedWorld();
	}
	
	public void onRemoveFromLoadedWorld()
	{
		this.nbt.clear();
	}
	
	protected boolean is(int i)
	{
		return (this.state & (1 << i)) != 0;
	}
	
	protected boolean islast(int i)
	{
		return (this.lastState & (1 << i)) != 0;
	}
	
	protected void change(int i)
	{
		this.state ^= 1 << i;
	}
	
	protected void disable(int i)
	{
		this.state &= ~(1 << i);
	}
	
	protected void enable(int i)
	{
		this.state |= (1 << i);
	}
	
	protected void set(int i, boolean flag)
	{
		if(flag)
		{
			enable(i);
		}
		else
		{
			disable(i);
		}
	}
}