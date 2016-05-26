package farcore.lib.tile;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.interfaces.tile.IDescribableTile;
import farcore.lib.net.tile.PacketTileAskSync;
import farcore.lib.net.tile.PacketTileSyncable;
import farcore.util.FleLog;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumSkyBlock;

public class TileEntitySyncable extends TileEntityBase
implements IDescribableTile
{
	private byte syncFlag;
	private List<EntityPlayer> list = new ArrayList();
	
	public TileEntitySyncable()
	{
		
	}
	
	@Override
	protected void markReinit()
	{
		super.markReinit();
	}
	
	@Override
	protected boolean init()
	{
		super.init();
		return true;
	}
	
	@Override
	protected void initClient()
	{
		FarCoreSetup.network.sendToServer(new PacketTileAskSync(worldObj, xCoord, yCoord, zCoord));
	}

	protected void syncToServer()
	{
		syncFlag |= 0x4;
	}
	
	protected void syncToAll()
	{
		syncFlag |= 0x2;
	}
	
	protected void syncToNearby()
	{
		syncFlag |= 0x1;
	}
	
	@Override
	protected boolean shouldClientNeedSyncable()
	{
		return true;
	}
	
	@Override
	public final void readDescriptionsFromNBT(NBTTagCompound nbt)
	{
		readDescriptionsFromNBT1(nbt);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		int range = updateRange();
		worldObj.markBlockRangeForRenderUpdate(
				xCoord - range, yCoord - range, zCoord - range, 
				xCoord + range, yCoord + range, zCoord + range);
		markInit();		
	}
	
	protected void readDescriptionsFromNBT1(NBTTagCompound nbt)
	{
		
	}
	
	@Override
	public final NBTTagCompound writeDescriptionsToNBT(NBTTagCompound nbt) 
	{
		writeDescriptionsToNBT1(nbt);
		return nbt;
	}
	
	protected void writeDescriptionsToNBT1(NBTTagCompound nbt)
	{
		
	}
	
	@Override
	protected final void updateServer()
	{
		updateServer1();
		updateSync();
	}
	
	protected void updateSync()
	{
		if(syncFlag != 0 || !list.isEmpty())
		{
			if(worldObj.isRemote)
			{
				if((syncFlag & 0x4) != 0)
				{
					FarCoreSetup.network.sendToServer(new PacketTileAskSync(worldObj, xCoord, yCoord, zCoord));
				}
			}
			else
			{
				PacketTileSyncable packet = new PacketTileSyncable(this);
				if((syncFlag & 0x2) != 0)
				{
					FarCoreSetup.network.sendToAll(new PacketTileSyncable(this));
				}
				else
				{
					if((syncFlag & 0x1) != 0)
					{
						int range = updateRange();
						if(range > 0)
						{
							FarCoreSetup.network.sendToNearBy(packet, worldObj.provider.dimensionId, xCoord, yCoord, zCoord, range);
						}
					}
					for(EntityPlayer player : list)
					{
						FarCoreSetup.network.sendToPlayer(packet, player);
					}
				}
			}
			syncFlag = 0;
			list.clear();
		}
	}
	
	protected void updateServer1()
	{
		
	}
	
	protected final void updateClient()
	{
		updateClient1();
		updateSync();
	}

	protected void updateClient1()
	{
		
	}
	
	protected int updateRange()
	{
		return 32;
	}
	
	@Override
	public void markNBTSync(EntityPlayer player)
	{
		if(!worldObj.isRemote)
		{
			list.add(player);
		}
	}
	
	@Override
	public void markUpdate()
	{
		if(worldObj.isRemote)
		{
			syncToServer();
		}
		else
		{
			syncToNearby();
		}
	}
	
	@Override	
	public void setLightValue(int value)
	{
		if(light != value)
		{
			light = value;
			if(!worldObj.isRemote)
			{
				syncToNearby();
			}
			else
			{
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
			}
		}
	}
}