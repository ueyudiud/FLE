package farcore.lib.tile;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.nbt.NBTSynclizedCompound;
import farcore.lib.net.tile.PacketTEAsk;
import farcore.lib.net.tile.PacketTESAskRender;
import farcore.lib.net.tile.PacketTESync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TESynchronization extends TEBuffered
implements ISynchronizableTile
{
	private boolean initialized = false;
	public NBTSynclizedCompound nbt = new NBTSynclizedCompound();
	
	/**
	 * The sync state.
	 * 1 for mark to all.
	 * 2 for mark to dim.
	 * 4 for mark to near by.
	 * 8 for mark block update.
	 * 16 for mark render update.
	 */
	public long syncState = 0L;
	private List<EntityPlayer> syncAskedPlayer = new ArrayList();
	
	public TESynchronization()
	{
		
	}
	
	public void syncToAll()
	{
		syncState |= 0x1;
	}
	
	public void syncToDim()
	{
		syncState |= 0x2;
	}
	
	public void syncToNearby()
	{
		syncState |= 0x4;
	}
	
	public void syncToPlayer(EntityPlayer player)
	{
		syncAskedPlayer.add(player);
	}
	
	public void markBlockUpdate()
	{
		syncState |= 0x8;
	}
	
	public void markBlockRenderUpdate()
	{
		syncState |= 0x10;
	}
	
	@Override
	public boolean isInitialized()
	{
		return initialized;
	}
	
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
	protected final void updateEntity2()
	{
		if(!isInitialized())
		{
			if(isServer())
			{
				initServer();
			}
			else
			{
				if((timer & 0xF) == 0)
				{
					sendToServer(new PacketTEAsk(worldObj, xCoord, yCoord, zCoord));
				}
			}
		}
		else if(isServer())
		{
			updateServer();
			nbt.reset();
			writeToDescription(nbt);
			if((syncState & 0x1) != 0)
			{
				sendToAll(new PacketTESync(worldObj, xCoord, yCoord, zCoord, nbt.getChanged(true)));
			}
			else if((syncState & 0x2) != 0)
			{
				sendToDim(new PacketTESync(worldObj, xCoord, yCoord, zCoord, nbt.getChanged(true)));
			}
			else if((syncState & 0x4) != 0)
			{
				sendToNearby(new PacketTESync(worldObj, xCoord, yCoord, zCoord, nbt.getChanged(true)), getSyncRange());
			}
			else if((syncState & 0x10) != 0)
			{
				sendToNearby(new PacketTESAskRender(worldObj, xCoord, yCoord, zCoord), getSyncRange());
			}
			if((syncState & 0x8) != 0)
			{
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			syncState = 0;
			for(EntityPlayer player : syncAskedPlayer)
			{
				sendToPlayer(new PacketTESync(worldObj, xCoord, yCoord, zCoord, nbt.asCompound()), player);
			}
			syncAskedPlayer.clear();
		}
		else
		{
			updateClient();
			if((syncState & 0x8) != 0)
			{
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			if((syncState & 0x10) != 0)
			{
				int range = getRenderUpdateRange();
				worldObj.markBlockRangeForRenderUpdate(xCoord - range, yCoord - range, zCoord - range, 
						xCoord + range, yCoord + range, zCoord + range);
			}
			syncState = 0;
			syncAskedPlayer.clear();
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
		initialized = true;
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
		nbt.clear();
	}
}